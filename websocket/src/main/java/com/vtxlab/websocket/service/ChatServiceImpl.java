package com.vtxlab.websocket.service;

import org.springframework.messaging.handler.annotation.Payload;
import com.vtxlab.websocket.infra.ChatMessage;

public interface ChatServiceImpl {
  public void sendMsg(@Payload ChatMessage chatMessage);

  public void alertUserStatus(@Payload ChatMessage chatMessage);
}
