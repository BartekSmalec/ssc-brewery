package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserDataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (authorityRepository.count() == 0) {
            loadSecurityData();
        }
    }

    private void loadSecurityData() {
        Authority adminAuth = authorityRepository.save(Authority.builder().role("ADMIN").build());
        Authority userAuth = authorityRepository.save(Authority.builder().role("USER").build());
        Authority customerAuth = authorityRepository.save(Authority.builder().role("CUSTOMER").build());


        userRepository.save(User.builder()
                .username("spring")
                .password(passwordEncoder.encode("guru"))
                .authority(adminAuth)
                .build());
        userRepository.save(User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .authority(userAuth)
                .build());
        userRepository.save(User.builder()
                .username("scott")
                .password(passwordEncoder.encode("tiger"))
                .authority(customerAuth)
                .build());

        log.debug("Users loaded: " + userRepository.count());

    }
}
