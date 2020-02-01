package com.nowcoder.community.entity;

import java.util.Date;

@lombok.Getter
@lombok.Setter
@lombok.ToString
public class DiscussPost {

    private int id;
    private int userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private Date createTime;
    private int commentCount;
    private double score;
}
