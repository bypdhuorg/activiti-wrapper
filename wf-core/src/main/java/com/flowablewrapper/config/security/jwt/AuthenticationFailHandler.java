package com.flowablewrapper.config.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * 登录失败处理类
 * @author meibo
 */
@Slf4j
@Component
public class AuthenticationFailHandler  extends SimpleUrlAuthenticationFailureHandler {
}
