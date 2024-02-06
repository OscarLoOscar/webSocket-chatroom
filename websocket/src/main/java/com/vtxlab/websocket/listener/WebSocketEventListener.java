package com.vtxlab.websocket.listener;

import java.net.Inet4Address;
import java.net.InetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import com.vtxlab.websocket.infra.ChatMessage;
import com.vtxlab.websocket.infra.JsonUtil;
import com.vtxlab.websocket.infra.RedisSetOperations;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebSocketEventListener {

  @Value("${server.port}")
  private String serverPort;

  @Value("${redis.set.onlineUsers}")
  private String onlineUsers;

  @Value("${redis.channel.userStatus}")
  private String userStatus;

  @Autowired
  RedisSetOperations redisSetOperations;

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    InetAddress localHost;
    try {
      localHost = Inet4Address.getLocalHost();
      log.info("Received a new web socket connection from:"
          + localHost.getHostAddress() + ":" + serverPort);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

    StompHeaderAccessor headerAccessor =
        StompHeaderAccessor.wrap(event.getMessage());

    String username =
        (String) headerAccessor.getSessionAttributes().get("username");

    if (username != null) {
      log.info("User Disconnected : " + username);
      ChatMessage chatMessage = new ChatMessage();
      chatMessage.setType(ChatMessage.MessageType.LEAVE);
      chatMessage.setSender(username);
      try {
        redisSetOperations.remove(onlineUsers, username);
        redisSetOperations.convertAndSend(userStatus,
            JsonUtil.parseObjToJson(chatMessage));
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }

    }
  }
}
