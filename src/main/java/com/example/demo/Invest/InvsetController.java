package com.example.demo.Invest;

import com.example.demo.Invest.dto.InvestRequestDto;
import com.example.demo.Invest.entity.Invest;
import com.example.demo.Invest.entity.InvestProduct;
import com.example.demo.error.CreateException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.example.demo.error.ErrorCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/invests")
public class InvsetController {

    private final InvestService investService;

    @GetMapping("/{type}/myList")
    public List<Invest> findInvestList(
            @RequestHeader Map<String, Object> requestHeader,
            @PathVariable("type") String type
    ) {
        if (requestHeader.get("x-user-id") == null) throw new CreateException(USER_NOT_FOUND);
        Long userId = Long.parseLong(String.valueOf(requestHeader.get("x-user-id")));
        return investService.findInvestListByRedis(userId);
    }

    @GetMapping("/{type}")
    public Collection<InvestProduct> findInvestProductList(
            @PathVariable("type") String type
    ) {
        return investService.findInvestProductListByRedis();
    }

    @PostMapping("/{type}")
    public boolean invest(
            @RequestBody InvestRequestDto investRequestDto,
            @RequestHeader Map<String, Object> requestHeader,
            @PathVariable("type") String type
    ) {
        boolean result = false;

        if (requestHeader.get("x-user-id") == null) throw new CreateException(USER_NOT_FOUND);
        Long userId = Long.parseLong(String.valueOf(requestHeader.get("x-user-id")));
        if (investRequestDto == null) throw new CreateException(CONTENT_TYPE_NOT_FOUND);
        Invest invest = Invest.builder()
                .userId(userId)
                .myInvestingAmount(investRequestDto.getMyInvestingAmount())
                .productId(investRequestDto.getProductId())
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        switch (type) {
            case "redis":
                result = investService.investByRedis(invest);
                break;
            default:
//                result = investService
                break;
        }

        return result;
    }

}
