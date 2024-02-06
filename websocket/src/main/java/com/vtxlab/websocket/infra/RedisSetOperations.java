package com.vtxlab.websocket.infra;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;
import lombok.NonNull;

@Component
public class RedisSetOperations {
  private final String USER_ID = "USER_ID_";
  private final RedisTemplate<String, String> redisTemplate;
  private final SetOperations<String, String> setOperations;

  public RedisSetOperations(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.setOperations = redisTemplate.opsForSet();
  }

  public boolean add(String key, @NonNull String... values) {
    try {
      setOperations.add(USER_ID + key, values);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public Set<String> members(String key) {
    try {
      return setOperations.members(USER_ID + key);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean remove(String key, @NonNull String... values) {
    try {
      Long removedCount =
          setOperations.remove(USER_ID + key, (Object[]) values);
      return removedCount > 0;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean expire(String key, long time) {
    try {
      if (time > 0) {
        redisTemplate.expire(USER_ID + key, time, TimeUnit.MILLISECONDS);
        return true;
      }
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean hasMember(String key, @NonNull String value) {
    try {
      return setOperations.isMember(USER_ID + key, value);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public Long convertAndSend(String channel, @NonNull String message) {
    return redisTemplate.convertAndSend(channel, message);
  }

  public static String formatKey(String head, String source) {
    return new StringBuilder(head).append(":").append(source).toString();
  }
}
