package gift.service;

import gift.common.exception.ErrorCode;
import gift.common.exception.OptionException;
import gift.common.util.KakaoUtil;
import gift.controller.order.OrderRequest;
import gift.controller.order.OrderResponse;
import gift.model.Option;
import gift.model.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private KakaoUtil kakaoUtil;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        KakaoUtil kakaoUtil) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.kakaoUtil = kakaoUtil;
    }

    @Transactional
    public OrderResponse order(String kakaoToken, OrderRequest orderRequest) {
        Option option = optionRepository.findById(orderRequest.optionId())
            .orElseThrow(() -> new OptionException(ErrorCode.OPTION_NOT_FOUND));

        Order order = orderRepository.save(orderRequest.toEntity(option));
        option.subtractQuantity(orderRequest.quantity());

        kakaoUtil.sendMessage(kakaoToken, orderRequest.message());

        return OrderResponse.from(order);
    }
}
