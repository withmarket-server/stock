package com.example.demo.controller;

import com.example.demo.dto.InvestRequestDto;
import com.example.demo.entity.Invest;
import com.example.demo.entity.InvestProduct;
import com.example.demo.error.CustomException;
import com.example.demo.service.InvestService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import javax.persistence.SecondaryTable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.example.demo.error.ErrorCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/invest")
public class InvsetController {

    private final InvestService investService;
    private final RedissonClient redissonClient;

    @GetMapping("/mypage")
    public List<Invest> findInvestList(@RequestHeader Map<String, Object> requestHeader) {
        Long userId = (Long) requestHeader.get("x-user-id");
        if (userId == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
        return investService.findInvestList(userId);
    }

    @GetMapping("/")
    public Collection<InvestProduct> findInvestProductList() {
        return investService.findInvestProductList();
    }

//    @PostMapping("/")
//    public Invest invest(@RequestBody InvestRequestDto req,
//                         @RequestHeader Map<String, Object> requestHeader) {
//        Long userId = (Long) requestHeader.get("x-user-id");
//        if (userId == null) {
//            throw new CustomException(USER_NOT_FOUND);
//        }
//
//        if (req == null) {
//            throw new CustomException(BODY_NOT_FOUND);
//        }
//
//        System.out.println(req.getProductId());
//        System.out.println(req.getMyInvestingAmount());
//        return investService.invest(invest);
//    }
}
