package db6.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Service for encoding passwords
@Service
public class PasswordService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
