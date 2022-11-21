package de.hsrm.mi.web.projekt.security;

import de.hsrm.mi.web.projekt.projektuser.ProjektUser;
import de.hsrm.mi.web.projekt.projektuser.ProjektUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProjektUserDetailService implements UserDetailsService {
    @Autowired
    private ProjektUserRepository projektUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ProjektUser user = projektUserRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " wurde nicht gefunden"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

}
