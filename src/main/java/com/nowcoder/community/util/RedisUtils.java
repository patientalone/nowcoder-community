package com.nowcoder.community.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Closeable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
	public static final FastDateFormat DATE_HHmmss = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	private static final String lockPrefix = "LOCK:";


    @Autowired  
    private StringRedisTemplate redisTemplate;
    public StringRedisTemplate redisTemplate() {
    	return this.redisTemplate;
    }
    /**
     * 缓存是否存在
     * @param key 
     * @return 
     */  
    public boolean exists(final String key) {  
        return redisTemplate.hasKey(key);
    }

    /** 
     * 读取缓存 
     * @param key 
     * @return 
     */  
    public String get(final String key) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

	public List<String> mget(final String... keys) {
		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		return operations.multiGet(Arrays.asList(keys));
	}

	public <T> T get(final String key, Class<T>clazz) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return fromString(operations.get(key), clazz);
    }
    /**
     * 读取缓存 
     * @param key 
     * @return 
     */  
    public Long incAndGet(final String key) {  
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return operations.increment(key, 1);
    }
    
    /** 
     * 读取缓存 
     * @param key 
     * @return 
     */  
    public Long incAndGet(final String key, long delta) {  
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return operations.increment(key, delta);
    }

    /** 
     * 写入缓存 
     *  
     * @param key 
     * @param value 
     * @return 
     */  
    public void set(final String key, Object value) {  
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(key, toString(value));
    }
    
    /** 
     * 写入缓存 
     *  
     * @param key 
     * @param value 
     * @return 
     */  
    public void set(final String key, Object value, long expireTime) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(key, toString(value), expireTime, TimeUnit.SECONDS);
    }

    public boolean setIfAbsent(final String key, Object value, long expireTime) {
		ValueOperations<String, String> operations = redisTemplate.opsForValue();
		return operations.setIfAbsent(key, toString(value), expireTime, TimeUnit.SECONDS);
	}

	public Cursor<String> scan(String pattern, int limit) {
		ScanOptions options = ScanOptions.scanOptions().match(pattern).count(limit).build();
		RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
		return (Cursor) redisTemplate.executeWithStickyConnection(new RedisCallback<Closeable>() {
			@Override
			public Closeable doInRedis(RedisConnection connection) throws DataAccessException {
				return new ConvertingCursor<>(connection.scan(options), redisSerializer::deserialize);
			}
		});
	}

    /** 
     * 批量删除对应的value 
     *  
     * @param keys 
     */  
    public void remove(final String... keys) {  
        for (String key : keys) {  
            remove(key);
        }
    }

    /** 
     * 批量删除key 
     *  
     * @param pattern 
     */  
    public void removePattern(final String pattern) {  
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)  
            redisTemplate.delete(keys);
    }

    /** 
     * 删除对应的value 
     * @param key 
     */  
    public void remove(final String key) {  
    	redisTemplate.delete(key);
    }

	public void delete(final Collection<String> keys) {
		redisTemplate.delete(keys);
	}


	public void delete(final String... keys) {
		redisTemplate.delete(Arrays.asList(keys));
	}

    public void delete(final String key) {
    	this.remove(key);
    }

    
    /** 
     *  
     * @Author Ron
     * @param key 
     * @param hashKey 
     * @return 
     */  
    public Object hashGet(final String key, final String hashKey){  
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        return operations.get(key, hashKey);
    }
    
    /** 
     *  
     * @Author Ron
     * @param key 
     * @param hashKeys
     * @return 
     */  
    public List<String> hashGet(final String key, final List<String> hashKeys){  
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        return operations.multiGet(key, hashKeys);
    }
    
    
    /** 
     *  
     * @Author Ron
     * @param key 
     * @param hashKey 
     * @return 
     */  
    public Long hashIncAndGet(final String key, final String hashKey){  
        HashOperations<String, String, Object> operations = redisTemplate.opsForHash();
        return operations.increment(key, hashKey, 1);
    }
    
    /** 
     *  
     * @Author Ron
     * @param key 
     * @param hashKey 
     * @return 
     */  
    public Long hashIncAndGet(final String key, final String hashKey, long delta){  
        HashOperations<String, String, Object> operations = redisTemplate.opsForHash();
        return operations.increment(key, hashKey, delta);
    }

    /** 
     *  
     * @Author Ron 
     * @param key 
     * @param hashKey 
     * @param value 
     * @return 
     */  
    public void hashSet(final String key, final String hashKey, Object value) {  
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        operations.put(key, hashKey, toString(value));
    }

    public void hashSet(final String key, final Map<String, Object>kvs) {  
        HashOperations<String, String, Object> operations = redisTemplate.opsForHash();
        operations.putAll(key, kvs);
    }

	public long hashDelete(final String key, final String hashKey){
		HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
		return operations.delete(key, hashKey);
	}

	public void setAdd(String key, List<String> values) {
        SetOperations<String, String> operations = redisTemplate.opsForSet();
        operations.add(key, (String[])values.toArray());
	}
	
	public void setAdd(String key, String... values) {
        SetOperations<String, String> operations = redisTemplate.opsForSet();
        operations.add(key, values);
	}

	public Cursor<String> setScan(String key, int limit) {
		ScanOptions options = ScanOptions.scanOptions().match("*").count(limit).build();
		SetOperations<String, String> operations = redisTemplate.opsForSet();
		return operations.scan(key, options);
	}

	public long setCount(String key){
    	return redisTemplate.opsForSet().size(key);
	}

	public long setDelete(String key, String member) {
		SetOperations<String, String> operations = redisTemplate.opsForSet();
		return operations.remove(key, member);
	}

	public Boolean isMember(String key, String values) {
        SetOperations<String, String> operations = redisTemplate.opsForSet();
		return operations.isMember(key, values);
	}
	
	public void expire(String key, long expire) {
		if (expire != -1) {
			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
		}
	}

	public void zsetAdd(String key, String member, double score) {
		ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
		operations.add(key, member, score);
	}

	/**
	 *
	 * @param key
	 * @param start 0-based included
	 * @param end 0-based included
	 */
	public void zsetRemoveByRange(String key, long start, long end) {
		ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
		operations.removeRange(key, start, end);
	}

	/**
	 *
	 * @param key
	 * @param min min score, included
	 * @param max max score, included
	 */
	public void zsetRemoveByScore(String key, double min, double max) {
		ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
		operations.removeRangeByScore(key, min, max);
	}

	/**
	 *
	 * @param key
	 * @param min min score, included
	 * @param max max score, included
	 */
	public Set<String> zsetRangeByScore(String key, double min, double max) {
		ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
		return operations.rangeByScore(key, min, max);
	}

	public Set<String> zsetRange(String key, long start, long end) {
		ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
		return operations.range(key, start, end);
	}


	public long zsetCard(String key) {
		ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
		return operations.zCard(key);
	}

	/**
	 * 对资源上锁
	 * @param resource 资源名称
	 * @param maxLockTimeSec 成功获得锁后，最大的上锁时间
	 * @param maxWaitTimeSec 最长等待锁的时间
	 * @return 锁ID
	 */
	public String lock(String resource, int maxLockTimeSec, int maxWaitTimeSec) {
    	long startTime = System.nanoTime();
		String token;
    	do {
			token = tryLock(resource, maxLockTimeSec);
			if (token != null) {
				return token;
			}

			if (System.nanoTime() - startTime > TimeUnit.SECONDS.toNanos(maxWaitTimeSec)) {
				return null;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				//NOP
			}
		} while (token == null);

		return null;
	}

	/**
	 * 尝试对资源上锁
	 * @param resource 资源名称
	 * @param maxLockTimeSec 成功获得锁后，最大的上锁时间
	 * @return 锁ID
	 */
	public String tryLock(String resource, int maxLockTimeSec) {
		String token = CommunityUtil.generateUUID();
		if (this.setIfAbsent(lockPrefix + resource, token, maxLockTimeSec)) {
			return token;
		}
		return null;
	}

	/**
	 * 解锁
	 * @param resource
	 * @param lockId
	 * @return
	 */
	public boolean unlock(String resource, String lockId) {
		String redisKey = lockPrefix + resource;
		String token = this.get(redisKey);
		if (token != null && lockId.equals(token)) {
			this.remove(redisKey);
			return true;
		}
		return false;
	}

	
	@SuppressWarnings("unchecked")
	private <T> T fromString(String str, Class<T> clazz) {
		if (clazz == String.class) {
			return (T)str;
		}
		
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		
		if (clazz == Integer.class || clazz == int.class) {
			return (T)Integer.valueOf(str);
		}
		else if (clazz == Long.class || clazz == long.class) {
			return (T)Long.valueOf(str);
		}
		else if (clazz == Float.class || clazz == float.class) {
			return (T)Float.valueOf(str);
		}
		else if (clazz == Double.class || clazz == double.class) {
			return (T)Double.valueOf(str);
		}
		else if (clazz == Boolean.class || clazz == boolean.class) {
			return (T)Boolean.valueOf(str);
		}
		else if (clazz == Byte.class || clazz == byte.class) {
			return (T)Byte.valueOf(str);
		}
		else if (clazz == Short.class || clazz == short.class) {
			return (T)Short.valueOf(str);
		}
		else if (clazz == BigDecimal.class) {
			return (T)new BigDecimal(str);
		}
		else if (clazz == BigInteger.class) {
			return (T)new BigInteger(str);
		}
		else if (clazz == Character.class || clazz == char.class) {
			return (T)(Character)str.charAt(0);
		}
		else if (clazz == Date.class) {
			try {
				return (T)DATE_HHmmss.parse(str);
			}
			catch (Exception e) {
				throw new IllegalArgumentException("failed to parse date", e);
			}
		}
		else {
			return JSON.parseObject(str, clazz);
		}
	}
	
	private String toString(Object obj) {
		if (obj == null) {
			return null;
		}
		
		if (obj instanceof String) {
			return (String)obj;
		}
		else if (obj instanceof Integer) {
			return ((Integer)obj).toString();
		}
		else if (obj instanceof Long) {
			return ((Long)obj).toString();
		}
		else if (obj instanceof Float) {
			return ((Float)obj).toString();
		}
		else if (obj instanceof Double) {
			return ((Double)obj).toString();
		}
		else if (obj instanceof Boolean) {
			return ((Boolean)obj).toString();
		}
		else if (obj instanceof Byte) {
			return ((Byte)obj).toString();
		}
		else if (obj instanceof Short) {
			return ((Short)obj).toString();
		}
		else if (obj instanceof BigDecimal) {
			return ((BigDecimal)obj).toPlainString();
		}
		else if (obj instanceof BigInteger) {
			return ((BigInteger)obj).toString();
		}
		else if (obj instanceof Character) {
			return ((Character)obj).toString();
		}
		else if (obj instanceof Date) {
			return DATE_HHmmss.format((Date)obj);
		}
		else {
			return JSON.toJSONString(obj);
		}
	}
}