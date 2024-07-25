package gift.service;

import gift.common.auth.LoginInfo;
import gift.common.exception.ErrorCode;
import gift.common.exception.OptionException;
import gift.common.util.KakaoUtil;
import gift.controller.order.dto.OrderRequest;
import gift.controller.order.dto.OrderResponse;
import gift.model.Option;
import gift.model.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private KakaoUtil kakaoUtil;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        WishRepository wishRepository, KakaoUtil kakaoUtil) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.kakaoUtil = kakaoUtil;
    }

    @Transactional
    public OrderResponse order(LoginInfo user, OrderRequest orderRequest) {
        Option option = optionRepository.findById(orderRequest.optionId())
            .orElseThrow(() -> new OptionException(ErrorCode.OPTION_NOT_FOUND));

        Order order = orderRepository.save(orderRequest.toEntity(option));
        option.subtractQuantity(orderRequest.quantity());

        if (wishRepository.existsByProductIdAndUserId(option.getProduct().getId(), user.id())) {
            wishRepository.deleteByProductIdAndUserId(option.getProduct().getId(), user.id());
        }

        kakaoUtil.sendMessage(user.kakaoToken(), orderRequest.message());

        return OrderResponse.from(order);
    }
}
