package com.vtxlab.websocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import com.vtxlab.websocket.infra.ChatMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatService implements ChatServiceImpl {


  @Autowired
  private SimpMessageSendingOperations simpMessageSendingOperations;

  public void sendMsg(ChatMessage chatMessage) {
    log.info(
        "Send msg by simpMessageSendingOperations:" + chatMessage.toString());
    simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
  }

  public void alertUserStatus(ChatMessage chatMessage) {
    log.info("Alert user online by simpMessageSendingOperations:"
        + chatMessage.toString());
    simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
  }
}
