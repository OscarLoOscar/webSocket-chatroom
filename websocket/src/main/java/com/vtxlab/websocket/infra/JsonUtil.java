package com.vtxlab.websocket.infra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonUtil {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * 把Java对象转换成json字符串
   *
   * @param object 待转化为JSON字符串的Java对象
   * @return json串 or null
   */
  public static String parseObjToJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      return null;
    }
  }

  /**
   * 将Json字符串信息转换成对应的Java对象
   *
   * @param json json字符串对象
   * @param c 对应的类型
   */
  public static <T> T parseJsonToObj(String json, Class<T> c) {
    try {
      return objectMapper.readValue(json, c);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage());
      return null;
    }
  }

}
