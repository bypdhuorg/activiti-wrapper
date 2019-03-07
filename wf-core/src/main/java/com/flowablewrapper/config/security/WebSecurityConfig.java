package com.flowablewrapper.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * Security 核心配置类
 * 开启控制权限至Controller
 * @author meibo
 */
@Slf4j
//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();

//
//        registry.and()
//                //前后端分离采用JWT 不需要session
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                //添加JWT过滤器 除已配置的其它请求都需经过此过滤器
//                .addFilter(new JWTAuthenticationFilter(authenticationManager()));
    }
}
