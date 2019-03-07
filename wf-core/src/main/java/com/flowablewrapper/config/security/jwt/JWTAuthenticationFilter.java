package com.flowablewrapper.config.security.jwt;

import com.flowablewrapper.common.constant.SecurityConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Exrickx
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header_userid = request.getHeader(SecurityConstant.USERID_HEADER);
        String header_username = request.getHeader(SecurityConstant.USERNAME_HEADER);

        Boolean notValid = StringUtils.isBlank(header_userid) || StringUtils.isBlank(header_username);
        if (notValid) {
            chain.doFilter(request, response);
            return;
        }
        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(header_userid,header_username, response);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception e){
            e.toString();
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String headeruserid,String headername, HttpServletResponse response) {

        // 用户名
        String username = null;
        // 权限
        List<GrantedAuthority> authorities = new ArrayList<>();

        User principal = new User(headeruserid, username, authorities);
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);

    }
}
