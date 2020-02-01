package com.nowcoder.community.util;

public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS=0;
    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT=1;
    /**
     * 激活失败
     */
    int ACTIVATION_FAILED=2;
    /**
     * 默认状态的登录凭证超时时间
     */
    int DEFAULT_EXPRIEDSECOND=3600*12;
    /**
     *记住状态下登陆超时时间
     */
    int REMEMBER_EXPRIEDSECOND=3600*24*100;

    /**
     * 帖子类型
     */
    int ENTITY_TYPE_POST=1;

    int ENTITY_TYPE_COMMENT=2;
}
