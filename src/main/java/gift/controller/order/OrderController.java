package gift.controller.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.common.annotation.LoginUser;
import gift.common.auth.LoginInfo;
import gift.service.OrderService;
import java.net.URI;
import java.util.Map;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    public ResponseEntity<OrderResponse> order(@LoginUser LoginInfo user, @RequestBody OrderRequest request) {
        OrderResponse response = orderService.order(user.kakaoToken(), request);
        return ResponseEntity.created(URI.create("/api/v1/orders/" + response.id())).body(response);
    }
}
