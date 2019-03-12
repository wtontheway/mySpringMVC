package lee.iSpring.common.cache;

import lee.iSpring.common.bean.PropertiesObject;
import lee.iSpring.common.util.SpringContextHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 默认使用redis缓存
 * <p>如果需要使用guava缓存，需要指定</p>
 * <p>eg: {@literal @Cacheable(cacheManager = CacheConfig.GUAVA_CACHE_MANAGER, value = "xxx_123")}</p>
 * <p>表示使用guava缓存, 有效期123秒</p>
 */
//@EnableCaching
//@Configuration
public class CacheConfig extends CachingConfigurerSupport {

	private Logger logger = LogManager.getLogger(getClass());
	
	//缓存管理器，与有@Bean注解的方法对应
	public final static String GUAVA_CACHE_MANAGER = "guavaCacheManager";
	public final static String REDIS_CACHE_MANAGER = "redisCacheManager";
	
	@Bean
	public CacheManager guavaCacheManager() {
		logger.info("init guava cache manager");
		GuavaCacheManager cacheManager = new GuavaCacheManagerEx();
		return cacheManager;
	}
	
	@Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();  
        redisConnectionFactory.setHostName(SpringContextHolder.getBean(PropertiesObject.class).getRedisHost());
        redisConnectionFactory.setPort(SpringContextHolder.getBean(PropertiesObject.class).getRedisPort());
        redisConnectionFactory.setPassword(SpringContextHolder.getBean(PropertiesObject.class).getRedisPassword());
        return redisConnectionFactory;  
    }  
  
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();  
//        RedisSerializer<Object> jackson2JsonRedisSerializer =new GenericJackson2JsonRedisSerializer();
//        redisTemplate.setKeySerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(cf);  
        return redisTemplate;  
    }  
  
    /**
     * 作为默认缓存
     * @param redisTemplate
     * @return
     */
    @Bean
    @Primary
    public CacheManager redisCacheManager(RedisTemplate<String, Object> redisTemplate) {
    	logger.info("init redis cache manager");
    	RedisCacheManagerEx cacheManager = new RedisCacheManagerEx(redisTemplate); 
    
    	//去掉默认的超时时间
        //cacheManager.setDefaultExpiration(SpringContextHolder.getBean(PropertiesObject.class).getRedisExpiration()); // Sets the default expire time (in seconds)  
        return cacheManager;  
    }  

}
