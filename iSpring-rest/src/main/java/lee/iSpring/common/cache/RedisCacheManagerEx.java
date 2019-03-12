package lee.iSpring.common.cache;

import lee.iSpring.common.util.SpringContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import lee.iSpring.common.bean.PropertiesObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RedisCacheManagerEx extends AbstractTransactionSupportingCacheManager {

	private Object objLockHelper = new Object();
	
	private final Log logger = LogFactory.getLog(RedisCacheManager.class);

	@SuppressWarnings("rawtypes")//
	private final RedisOperations redisOperations;

	private boolean usePrefix = false;
	private RedisCachePrefix cachePrefix = new DefaultRedisCachePrefix();
	private boolean loadRemoteCachesOnStartup = false;
	@SuppressWarnings("unused")
	private boolean dynamic = true;

	// 0 - never expire
	private long defaultExpiration = 0;
	private Map<String, Long> expires = null;

	private Set<String> configuredCacheNames;

	/**
	 * Construct a {@link RedisCacheManager}.
	 * 
	 * @param redisOperations
	 */
	@SuppressWarnings("rawtypes")
	public RedisCacheManagerEx(RedisOperations redisOperations) {
		this(redisOperations, Collections.<String> emptyList());
	}

	/**
	 * Construct a static {@link RedisCacheManager}, managing caches for the specified cache names only.
	 * 
	 * @param redisOperations
	 * @param cacheNames
	 * @since 1.2
	 */
	@SuppressWarnings("rawtypes")
	public RedisCacheManagerEx(RedisOperations redisOperations, Collection<String> cacheNames) {
		this.redisOperations = redisOperations;
		setCacheNames(cacheNames);
	}

	@Override
	public Cache getCache(String name) {
		
		Cache cache = super.getCache(name);
	
		if (cache == null) {
			synchronized (objLockHelper) {
				
				if(expires == null){
					expires = new ConcurrentHashMap<String, Long>();
				}
				
				String[] items = name.split("_");
				//在获取Cache之前创建过期map
				if(items.length > 1){
					if(!this.expires.containsKey(name))
						this.expires.put(name, Long.parseLong(items[items.length - 1]));//取最后一个				     
				}else {
					this.setDefaultExpiration(SpringContextHolder.getBean(PropertiesObject.class).getRedisExpiration());
				}
			}
			
			return createAndAddCache(name);
		}

		return cache;
	}

	/**
	 * Specify the set of cache names for this CacheManager's 'static' mode. <br>
	 * The number of caches and their names will be fixed after a call to this method, with no creation of further cache
	 * regions at runtime. <br>
	 * Calling this with a {@code null} or empty collection argument resets the mode to 'dynamic', allowing for further
	 * creation of caches again.
	 */
	public void setCacheNames(Collection<String> cacheNames) {

		Set<String> newCacheNames = CollectionUtils.isEmpty(cacheNames) ? Collections.<String> emptySet()
				: new HashSet<String>(cacheNames);

		this.configuredCacheNames = newCacheNames;
		this.dynamic = newCacheNames.isEmpty();
	}

	public void setUsePrefix(boolean usePrefix) {
		this.usePrefix = usePrefix;
	}

	/**
	 * Sets the cachePrefix. Defaults to 'DefaultRedisCachePrefix').
	 * 
	 * @param cachePrefix the cachePrefix to set
	 */
	public void setCachePrefix(RedisCachePrefix cachePrefix) {
		this.cachePrefix = cachePrefix;
	}

	/**
	 * Sets the default expire time (in seconds).
	 * 
	 * @param defaultExpireTime time in seconds.
	 */
	public void setDefaultExpiration(long defaultExpireTime) {
		this.defaultExpiration = defaultExpireTime;
	}

	/**
	 * Sets the expire time (in seconds) for cache regions (by key).
	 * 
	 * @param expires time in seconds
	 */
	public void setExpires(Map<String, Long> expires) {
		this.expires = (expires != null ? new ConcurrentHashMap<String, Long>(expires) : null);
	}

	/**
	 * If set to {@code true} {@link RedisCacheManager} will try to retrieve cache names from redis server using
	 * {@literal KEYS} command and initialize {@link RedisCache} for each of them.
	 * 
	 * @param loadRemoteCachesOnStartup
	 * @since 1.2
	 */
	public void setLoadRemoteCachesOnStartup(boolean loadRemoteCachesOnStartup) {
		this.loadRemoteCachesOnStartup = loadRemoteCachesOnStartup;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.cache.support.AbstractCacheManager#loadCaches()
	 */
	@Override
	protected Collection<? extends Cache> loadCaches() {

		Assert.notNull(this.redisOperations, "A redis template is required in order to interact with data store");
		return addConfiguredCachesIfNecessary(loadRemoteCachesOnStartup ? loadAndInitRemoteCaches() : Collections
				.<Cache> emptyList());
	}

	/**
	 * Returns a new {@link Collection} of {@link Cache} from the given caches collection and adds the configured
	 * {@link Cache}s of they are not already present.
	 * 
	 * @param caches must not be {@literal null}
	 * @return
	 */
	protected Collection<? extends Cache> addConfiguredCachesIfNecessary(Collection<? extends Cache> caches) {

		Assert.notNull(caches, "Caches must not be null!");

		Collection<Cache> result = new ArrayList<Cache>(caches);

		for (String cacheName : getCacheNames()) {

			boolean configuredCacheAlreadyPresent = false;

			for (Cache cache : caches) {

				if (cache.getName().equals(cacheName)) {
					configuredCacheAlreadyPresent = true;
					break;
				}
			}

			if (!configuredCacheAlreadyPresent) {
				result.add(getCache(cacheName));
			}
		}

		return result;
	}

	protected Cache createAndAddCache(String cacheName) {
		addCache(createCache(cacheName));
		return super.getCache(cacheName);
	}

	@SuppressWarnings("unchecked")
	protected RedisCache createCache(String cacheName) {
		long expiration = computeExpiration(cacheName);
		return new RedisCache(cacheName, (usePrefix ? cachePrefix.prefix(cacheName) : null), redisOperations, expiration);
	}

	protected long computeExpiration(String name) {
		Long expiration = null;
		if (expires != null) {
			expiration = expires.get(name);
		}
		return (expiration != null ? expiration.longValue() : defaultExpiration);
	}

	protected List<Cache> loadAndInitRemoteCaches() {

		List<Cache> caches = new ArrayList<Cache>();

		try {
			Set<String> cacheNames = loadRemoteCacheKeys();
			if (!CollectionUtils.isEmpty(cacheNames)) {
				for (String cacheName : cacheNames) {
					if (null == super.getCache(cacheName)) {
						caches.add(createCache(cacheName));
					}
				}
			}
		} catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.warn("Failed to initialize cache with remote cache keys.", e);
			}
		}

		return caches;
	}

	@SuppressWarnings("unchecked")
	protected Set<String> loadRemoteCacheKeys() {
		return (Set<String>) redisOperations.execute(new RedisCallback<Set<String>>() {

			@Override
			public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {

				// we are using the ~keys postfix as defined in RedisCache#setName
				Set<byte[]> keys = connection.keys(redisOperations.getKeySerializer().serialize("*~keys"));
				Set<String> cacheKeys = new LinkedHashSet<String>();

				if (!CollectionUtils.isEmpty(keys)) {
					for (byte[] key : keys) {
						cacheKeys.add(redisOperations.getKeySerializer().deserialize(key).toString().replace("~keys", ""));
					}
				}

				return cacheKeys;
			}
		});
	}

	@SuppressWarnings("rawtypes")
	protected RedisOperations getRedisOperations() {
		return redisOperations;
	}

	protected RedisCachePrefix getCachePrefix() {
		return cachePrefix;
	}

	protected boolean isUsePrefix() {
		return usePrefix;
	}

	/**
	 * The number of caches and their names will be fixed after a call to this method, with no creation of further cache
	 * regions at runtime.
	 * 
	 * @see org.springframework.cache.support.AbstractCacheManager#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {

		if (!CollectionUtils.isEmpty(configuredCacheNames)) {

			for (String cacheName : configuredCacheNames) {
				createAndAddCache(cacheName);
			}

			configuredCacheNames.clear();
		}

		super.afterPropertiesSet();
	}

	/* (non-Javadoc)
	* @see
	org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager#decorateCache(org.springframework.cache.Cache)
	*/
	@Override
	protected Cache decorateCache(Cache cache) {

		if (isCacheAlreadyDecorated(cache)) {
			return cache;
		}

		return super.decorateCache(cache);
	}

	protected boolean isCacheAlreadyDecorated(Cache cache) {
		return isTransactionAware() && cache instanceof TransactionAwareCacheDecorator;
	}

	
}
