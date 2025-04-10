package db6.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import db6.domain.AppUser;
import db6.domain.repository.AppUserRepository;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user with email " + email));

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole()));
    }

}