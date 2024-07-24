package gift.common.util;

import gift.controller.oauth.KakaoProperties;
import java.net.URI;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KakaoUtil {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;

    public KakaoUtil(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
        this.restClient = RestClient.builder().build();
    }

    public String extractEmail(String accessToken) {
        var url = kakaoProperties.userInfoUrl();
        var response = restClient.get()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .body(Map.class);

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.get("kakao_account");
        String email = (String) kakaoAccount.get("email");
        return email;
    }

    public String getAccessToken(String code) {
        var url = kakaoProperties.tokenUrl();
        LinkedMultiValueMap<String, String> body = createBody(code);
        var response = restClient.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .body(Map.class);
        return response.get("access_token").toString();
    }

    public String getRequestUrl() {
        String kakaoLoginUrl = UriComponentsBuilder.fromHttpUrl(kakaoProperties.authUrl())
            .queryParam("scope", "account_email")
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", kakaoProperties.redirectUri())
            .queryParam("client_id", kakaoProperties.clientId())
            .build().toString();
        return kakaoLoginUrl;
    }

   public LinkedMultiValueMap<String, String> createBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_url", kakaoProperties.redirectUri());
        body.add("code", code);
        return body;
    }
}
