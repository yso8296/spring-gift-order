package gift.controller.order.dto;

import gift.controller.order.dto.Link;

public record TextTemplate(
    String object_type,
    String text,
    Link link
) {
}
