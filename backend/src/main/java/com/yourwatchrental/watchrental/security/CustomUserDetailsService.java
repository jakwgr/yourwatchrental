package com.yourwatchrental.watchrental.security;

import com.yourwatchrental.watchrental.user.User;
import com.yourwatchrental.watchrental.user.UserRepository;
import com.yourwatchrental.watchrental.user.UserStatus;
import com.yourwatchrental.watchrental.user.exceptions.UserDisabledException;
import com.yourwatchrental.watchrental.user.exceptions.UserIdNotFoundExcpetion;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    final private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UserIdNotFoundExcpetion {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new UserIdNotFoundExcpetion());

        if(user.getStatus() == UserStatus.DISABLED)
        {
            throw new UserDisabledException(user.getId());
        }

        List<SimpleGrantedAuthority> auth = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(),
                user.getPassword(),
                auth
        );
    }
}
