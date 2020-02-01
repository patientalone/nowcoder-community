package com.nowcoder.community.entity;

import java.util.Date;

@lombok.Getter
@lombok.Setter
@lombok.ToString
public class Comment {
    private int id;
    private int userId;
    private int entityType;
    private int entityId;
    private int targetId;
    private String content;
    private int status;
    private Date createTime;
}
