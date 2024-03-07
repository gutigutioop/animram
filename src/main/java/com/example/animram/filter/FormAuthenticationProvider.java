package com.example.animram.filter;

/*
 * 
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.animram.entity.User;
import com.example.animram.repository.UserRepository;

@Configuration
public class FormAuthenticationProvider implements AuthenticationProvider {

    
    @Autowired
    private UserRepository repository;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

   //入力された情報をフィールドに代入する。
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String name = auth.getName();
        String password = auth.getCredentials().toString();

        //入力された名前とパスワードが空えないか見てる
        if ("".equals(name) || "".equals(password)) {
            throw new AuthenticationCredentialsNotFoundException("ログイン情報に不備があります。");
        }

        User entity = repository.findByName(name);
        if (entity == null) {
            throw new AuthenticationCredentialsNotFoundException("ログイン情報が存在しません。");
        }

        if (!passwordEncoder.matches(password, entity.getPassword())) {
        //if(!password.equals(entity.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException("ログイン情報に不備があります。");
        }

        return new UsernamePasswordAuthenticationToken(entity, password, entity.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}