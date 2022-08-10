package com.Lycle.Server.config.auth.service;

import com.Lycle.Server.config.auth.UserPrincipal;
import com.Lycle.Server.domain.User.User;
import com.Lycle.Server.domain.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
       User user =  userRepository.findByEmail(name)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        UserPrincipal userPrincipal = new UserPrincipal(user);
        //UserDetail을 구현한 UserPrincipal을 구현
        return userPrincipal;
    }

}
