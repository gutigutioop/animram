package com.example.animram.service;

/*入力されてきた情報をusersのDBと見比べて、入力されたemailが空か見てる。その後emailを元にユーザー情報一人分の情報をentityに詰め、
 *  UserDetailsに戻ってる。
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.animram.entity.User;
import com.example.animram.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository repository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	
        if (email == null || "".equals(email)) {
            throw new UsernameNotFoundException("email is empty");
        }
        User entity = repository.findByEmail(email);

        return entity;
    }

}