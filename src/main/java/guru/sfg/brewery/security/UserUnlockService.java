package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserUnlockService {
    private final UserRepository userRepository;

    @Scheduled(fixedRate = 5000)
    public void unlockAccounts() {
        log.debug("Running unlock accounts");
        List<User> lockedUser = userRepository
                .findAllByAccountNonLockedAndLastModifiedDateIsBefore(false,
                        Timestamp.valueOf(LocalDateTime.now().minusSeconds(30)));

        if (lockedUser.size() > 0) {
            log.debug("Locked accounts found, unlocking");
            lockedUser.forEach(user -> user.setAccountNonLocked(true));
            userRepository.saveAll(lockedUser);
        }
    }
}
