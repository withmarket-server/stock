package com.example.demo.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class InvestRequestDto {
    private Long userId;
    private Long productId;
    private Long myInvestingAmount;

//    public Invest toEntity() {
//        return Invest.builder()
//                .userId(Long.parseLong(userId))
//                .productId(Long.parseLong(productId))
//                .myInvestingAmount(Long.parseLong(myInvestingAmount))
//                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
//                .build();
//    }
}
