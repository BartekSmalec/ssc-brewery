package guru.sfg.brewery.security.listners;

import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginFailureRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationFailureListener {

    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;

    @EventListener
    public void listen(AuthenticationFailureBadCredentialsEvent event) {
        log.debug("Authentication failure");
        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();

            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
            if (token.getPrincipal() instanceof String && token.getCredentials() instanceof String) {
                String username = (String) token.getPrincipal();
                builder.username(username);
                if (userRepository.findByUsername(username).isPresent()) {
                    builder.user(userRepository.findByUsername(username).get());
                }

                log.debug("User name: {}  and credentials: {}", token.getPrincipal(), token.getCredentials());
            }
            if (token.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
                builder.sourceIp(details.getRemoteAddress());
                log.debug("Source IP: " + details.getRemoteAddress());
            }

            LoginFailure loginFailure = loginFailureRepository.save(builder.build());
            log.debug("Login failure id: {}", loginFailure.getId());

            if (loginFailure.getUser() != null) {
                lockUserAccount(loginFailure.getUser());
            }
        }
    }

    private void lockUserAccount(User user) {
        List<LoginFailure> loginFailureList = loginFailureRepository.findAllByUserAndCreatedDateIsAfter(user, Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        if(loginFailureList.size() > 3)
        {
            log.debug("Locking user account: ", user.getUsername());
            user.setAccountNonLocked(false);
            userRepository.save(user);
        }
    }
}
