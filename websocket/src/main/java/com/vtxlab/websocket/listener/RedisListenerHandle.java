package com.vtxlab.websocket.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;
import com.vtxlab.websocket.infra.ChatMessage;
import com.vtxlab.websocket.infra.JsonUtil;
import com.vtxlab.websocket.service.ChatService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisListenerHandle extends MessageListenerAdapter {
  @Value("${redis.channel.msgToAll}")
  private String msgToAll;

  @Value("${redis.channel.userStatus}")
  private String userStatus;

  @Value("${server.port}")
  private String serverPort;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private ChatService chatService;

  /**
   * 收到监听消息
   * 
   * @param message
   * @param bytes
   */
  @Override
  public void onMessage(Message message, byte[] bytes) {
    byte[] body = message.getBody();
    byte[] channel = message.getChannel();
    String rawMsg;
    String topic;
    try {
      rawMsg = redisTemplate.getStringSerializer().deserialize(body);
      topic = redisTemplate.getStringSerializer().deserialize(channel);
      log.info("Received raw message from topic:" + topic
          + ", raw message content：" + rawMsg);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return;
    }


    if (msgToAll.equals(topic)) {
      log.info("Send message to all users:" + rawMsg);
      ChatMessage chatMessage =
          JsonUtil.parseJsonToObj(rawMsg, ChatMessage.class);
      if (chatMessage != null) {
        chatService.sendMsg(chatMessage);
      }
    } else if (userStatus.equals(topic)) {
      ChatMessage chatMessage =
          JsonUtil.parseJsonToObj(rawMsg, ChatMessage.class);
      if (chatMessage != null) {
        chatService.alertUserStatus(chatMessage);
      }
    } else {
      log.warn("No further operation with this topic!");
    }
  }
}
