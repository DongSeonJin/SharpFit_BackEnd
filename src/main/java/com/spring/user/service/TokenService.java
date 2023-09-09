package com.spring.user.service;

import com.spring.user.config.jwt.TokenProvider;
import com.spring.user.entity.User;
import com.spring.user.repository.RefreshTokenRepository;
import com.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public String createNewAccessToken(String refreshToken) {
        // !!!! 리프레시 토큰도 JWT스펙으로 만들어지기 때문에, TokenProvider에 의한 유효성 검증이 가능합니다. !!!!
        if(!tokenProvider.validToken(refreshToken)){
            throw new IllegalArgumentException("Unexpected Token!!!");
        }// 유효하지 않은 토큰이면 예외 발생후 종료

        // 유효한 토큰이면 어떤 유저의 토큰인지 먼저 확인
        Long userId = refreshTokenRepository.findByRefreshToken(refreshToken).getUserId();

        User user = userRepository.findById(userId).get();

        // 리프레시 토큰이 유효하며, 유저가 존재한다면 새로운 억세스 토큰을 발급합니다.
        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
