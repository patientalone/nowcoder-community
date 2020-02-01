package com.nowcoder.community.entity;

import java.util.Date;

@lombok.Getter
@lombok.Setter
@lombok.ToString
public class User {

    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status;
    private String activationCode;
    public String headerUrl;
    private Date createTime;


}
