package bkav.com.springboot.security.services;

import bkav.com.springboot.models.Entities.Permission;
import bkav.com.springboot.models.Entities.User;
import bkav.com.springboot.repository.PermissionRepository;
import bkav.com.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        List<Permission> permissions = new ArrayList<>();
        if (username.equals("supper_admin")) {
            permissions = permissionRepository.findAll();
        }
        return UserDetailsImpl.build(user, permissions);
    }
}
