package com.example.demo.Invest.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Invest {

    @Id @GeneratedValue
    private Long id;

    private Long userId;

    private Long productId;
    private String productTitle;

    private Long totalInvestingAmount;
    private Long myInvestingAmount;
    private String createdAt;
}