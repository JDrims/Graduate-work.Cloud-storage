package ru.jdrims.cloudservice.service;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.jdrims.cloudservice.dto.LoginInRequest;
import ru.jdrims.cloudservice.dto.LoginInResponse;
import ru.jdrims.cloudservice.entities.TokenEntity;
import ru.jdrims.cloudservice.entities.UserEntity;
import ru.jdrims.cloudservice.exceptions.AuthorizationException;
import ru.jdrims.cloudservice.exceptions.BadCredentialsException;
import ru.jdrims.cloudservice.repository.TokenRepository;
import ru.jdrims.cloudservice.repository.UserRepository;

import java.util.Random;

@Service
public class AuthorizationService {
    private final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final Random random = new Random();

    public AuthorizationService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public LoginInResponse login(@Valid LoginInRequest loginInRequest) {
        final String loginFromRequest = loginInRequest.getLogin();
        final UserEntity user = userRepository.findById(loginFromRequest).orElseThrow(() -> {
            logger.error("User with login {} not found", loginFromRequest);
            return new BadCredentialsException("User with login " + loginFromRequest + " not found");
        });

        if (!user.getPassword().equals(loginInRequest.getPassword())) {
            logger.error("Incorrect password for user {}", loginFromRequest);
            throw new BadCredentialsException("Incorrect password for user " + loginFromRequest);
        }
        final String authToken = String.valueOf(random.nextLong());
        tokenRepository.save(new TokenEntity(authToken));
        logger.info("User {} entered with token {}", loginFromRequest, authToken);
        return new LoginInResponse(authToken);
    }

    public void logout(String authToken) {
        tokenRepository.deleteById(authToken.split(" ")[1].trim());
        logger.info("User {} logout", authToken.split(" ")[1].trim());
    }

    public void checkToken(String authToken) {
        if (!tokenRepository.existsById(authToken.split(" ")[1].trim())) {
            logger.error("User is not authorized");
            throw new AuthorizationException();
        }
    }
}