package com.walletech.util;

import com.walletech.datahub.DataHubApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisUtil {

    private static RedisTemplate redisTemplate = DataHubApplication.context.getBean(RedisTemplate.class);

    public static void set(String key, Object value) {

        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);

    }

    public static void setBytes(final String Key, final byte[] bytes){
         redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
                byte[] key = redisSerializer.serialize(Key);
                redisConnection.set(key,bytes);
                return true;
            }
        });
    }



    public static void unbind(){
        RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
    }

    public static Object get(String key) {
        Object value =  redisTemplate.opsForValue().get(key);
        return value;
    }

    public static void setList(String key, List<?> value) {
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPush(key, value);
    }

    public static Object getList(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public static void setSet(String key, Set<?> value) {
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add(key, value);
    }

    public static Object getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }


    public static void setHash(String key, Map<String, ?> value) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, value);
    }

    public static Object getHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    public static void delete(String key) {
        redisTemplate.delete(key);
        RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
    }


}
