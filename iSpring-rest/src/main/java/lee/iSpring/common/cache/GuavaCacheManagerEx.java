package lee.iSpring.common.cache;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.guava.GuavaCacheManager;

import java.util.concurrent.TimeUnit;

/**
 * 重写GuavaCacheManager, 支持过期时间的cache，
 */
public class GuavaCacheManagerEx extends GuavaCacheManager {
	
	@Override
	protected Cache createGuavaCache(String name) {
		if(!name.matches("^.+_[0-9]+$")) {
			return super.createGuavaCache(name);
		}
		String[] items = name.split("_");
		long duration = Long.valueOf(items[items.length - 1]);
		GuavaCache guavaCache = new GuavaCache(name, CacheBuilder.newBuilder().expireAfterWrite(duration, TimeUnit.SECONDS).build());
		return guavaCache;
	}

}
