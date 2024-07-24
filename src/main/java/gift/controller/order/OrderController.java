package gift.controller.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.common.annotation.LoginUser;
import gift.common.auth.LoginInfo;
import java.net.URI;
import java.util.Map;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final RestClient restClient;

    public OrderController() {
        this.restClient = RestClient.builder().build();
    }

    @PostMapping("")
    public void order(@LoginUser LoginInfo user, @RequestBody OrderRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        TextTemplate textTemplate = new TextTemplate("text", "a", new Link("http://localhost:8080"));
        String jsonTemplate = objectMapper.writeValueAsString(textTemplate);
        MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
        map.set("template_object", jsonTemplate);

        var response = restClient.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + user.kakaoToken())
            .body(map)
            .retrieve()
            .toEntity(String.class);

        System.out.println(response);
    }
}
