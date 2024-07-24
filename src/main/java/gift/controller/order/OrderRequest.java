package gift.controller.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
    @NotNull
    Long optionId,
    @Min(0)
    int quantity,
    @NotBlank
    String message
    ) {

}
