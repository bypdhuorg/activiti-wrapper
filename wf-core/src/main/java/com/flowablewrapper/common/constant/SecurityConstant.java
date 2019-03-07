package com.flowablewrapper.common.constant;

public interface SecurityConstant {
    /**
     * token分割
     */
    String TOKEN_SPLIT = "Bearer ";

    /**
     * JWT签名加密key
     */
    String JWT_SIGN_KEY = "wf";

    /**
     * token参数头
     */
    String HEADER = "accessToken";

    /**
     * token参数头
     */
    String USERID_HEADER = "userid";

    /**
     * token参数头
     */
    String USERNAME_HEADER = "username";

    /**
     * 权限参数头
     */
    String AUTHORITIES = "authorities";

    /**
     * 用户选择JWT保存时间参数头
     */
    String SAVE_LOGIN = "saveLogin";

    /**
     * 交互token前缀key
     */
    String TOKEN_PRE = "WF_TOKEN_PRE:";

    /**
     * 用户token前缀key 单点登录使用
     */
    String USER_TOKEN = "WF_USER_TOKEN:";

}
