package com.walletech.util;

import com.walletech.datahub.DataHubApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

    public static void refreshCache(String key, Integer value) {
        setBytes(key, serialize(value));
//        // 修改key的值，与name绑定
//        String _key = String.valueOf(key);// key必须是String 类型
//        // 将数据存入redis
//        if (null != value) {
//            Serializable val = (Serializable) value;// value必须是Serializable类型
//            redisTemplate.opsForValue().set(_key.getBytes(), redisTemplate.getValueSerializer().serialize(val));
//        }
    }

    public static byte[] serialize(Object object) {
        if (object == null) {
            return null;
        }
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
