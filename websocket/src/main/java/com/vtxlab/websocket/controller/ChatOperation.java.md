package com.venturenix.project.base_core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.venturenix.project.base_core.Redis.infra.JsonUtil;
import com.venturenix.project.base_core.model.ChatMessage;
import com.venturenix.project.base_core.service.ChatService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatOperation implements ChatController {
  @Value("${redis.channel.msgToAll}")
  private String msgToAll;

  @Value("${redis.set.onlineUsers}")
  private String onlineUsers;

  @Value("${redis.channel.userStatus}")
  private String userStatus;

  @Autowired
  private ChatService chatService;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Override
  @MessageMapping(".sendMessage")
  @SendTo("/topic/public")
  public void sendMessage(ChatMessage chatMessage) {
    try {
      redisTemplate.convertAndSend(msgToAll,
          JsonUtil.parseObjToJson(chatMessage));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  @MessageMapping(".addUser")
  @SendTo("/topic/public")
  public void addUser(ChatMessage chatMessage,
      SimpMessageHeaderAccessor headerAccessor) {

    log.info("User added in Chatroom:" + chatMessage.getSender());
    try {
      headerAccessor.getSessionAttributes().put("username",
          chatMessage.getSender());
      redisTemplate.opsForSet().add(onlineUsers, chatMessage.getSender());
      redisTemplate.convertAndSend(userStatus,
          JsonUtil.parseObjToJson(chatMessage));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
