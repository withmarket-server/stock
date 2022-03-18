package com.example.demo.Invest.entity;

import com.example.demo.error.CreateException;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

import static com.example.demo.error.ErrorCode.EXCEED_TOTAL_AMOUNT;
import static com.example.demo.error.ErrorCode.SOLD_OUT;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class InvestProduct {

    @Id
    private Long productId;

    private String productTitle;
    private Long totalInvestingAmount;
    private Long currentInvestingAmount;
    private Long investors;
    private InvestState investState;

    private String startedAt;
    private String finishedAt;

    public void addAmount(Long amount) {
        if (investState == InvestState.COMPLETE) throw new CreateException(SOLD_OUT);
        if (currentInvestingAmount + amount > totalInvestingAmount) throw new CreateException(EXCEED_TOTAL_AMOUNT);
        currentInvestingAmount += amount;
        investors++;
        if (totalInvestingAmount - currentInvestingAmount < 1000) investState = InvestState.COMPLETE;
    }
}
