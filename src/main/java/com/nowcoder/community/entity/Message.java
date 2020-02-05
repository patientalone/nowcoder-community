package com.nowcoder.community.entity;

import java.util.Date;

@lombok.Setter
@lombok.Getter
@lombok.ToString
public class Message {
    int id;
    int fromId;
    int toId;
    String conversationId;
    String content;
    int status;
    Date createTime;
}
