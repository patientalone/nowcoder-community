package com.nowcoder.community.entity;

import java.util.HashMap;
import java.util.Map;

@lombok.Setter
@lombok.Getter
@lombok.AllArgsConstructor
public class Entity {
    private String topic;
    private int userId;
    private int entityType;
    private int entityId;
    private int entityUserId;
    private Map<String,Object> map=new HashMap<>();
}
