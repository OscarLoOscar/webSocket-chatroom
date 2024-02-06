package com.vtxlab.websocket.infra;

import lombok.Data;
import lombok.Getter;

@Data
public class ChatMessage {
  private MessageType type;
  private String content;
  private String sender;

  @Getter
  public enum MessageType {
    CHAT, //
    JOIN, //
    LEAVE
  }
}
