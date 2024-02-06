package com.venturenix.project.base_core.controller;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import com.venturenix.project.base_core.model.ChatMessage;

public interface ChatController {

  public void sendMessage(@Payload ChatMessage chatMessage);

  public void addUser(@Payload ChatMessage chatMessage, //
      SimpMessageHeaderAccessor headerAccessor);

}
