package dev.abhaya.mindstack.Security;

import dev.abhaya.mindstack.model.StackUser;
import dev.abhaya.mindstack.repository.StackUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StackUserDetailsService implements UserDetailsService {

    private final StackUserRepository stackUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        StackUser stackUser = stackUserRepository.findByEmail(email)
                .orElseThrow( () -> new UsernameNotFoundException("User Not Found"));

        return new User(stackUser.getEmail(),stackUser.getPassword(), List.of());
    }

    public StackUser getStackUserById(Long userid) {
        return stackUserRepository.findById(userid)
                .orElseThrow(()->new UsernameNotFoundException("userId not found"));
    }
}
