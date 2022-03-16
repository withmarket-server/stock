package com.example.demo.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Invest {

//    @Id @GeneratedValue
//    private Long id;

    @Id
    private Long userId;

    private Long productId;
    private String productTitle;

    private Long totalInvestingAmount;
    private Long myInvestingAmount;
    private LocalDateTime createdAt;
}