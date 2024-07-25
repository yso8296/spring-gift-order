package gift.service;

import gift.common.auth.JwtTokenProvider;
import gift.common.util.KakaoUtil;
import gift.model.User;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoUtil kakaoUtil;

    public OAuthService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, KakaoUtil kakaoUtil) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoUtil = kakaoUtil;
    }

    public String register(String code) {
        String accessToken = kakaoUtil.getAccessToken(code);

        String email = kakaoUtil.extractEmail(accessToken);
        //
        User user = userRepository.findByEmail(email).orElseGet(() -> userRepository.save(new User("", email)));
        return jwtTokenProvider.createToken(user.getEmail(), accessToken);
    }
}
