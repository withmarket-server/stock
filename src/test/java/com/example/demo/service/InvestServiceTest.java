package com.example.demo.service;

import com.example.demo.Invest.InvestService;
import com.example.demo.Invest.entity.Invest;
import com.example.demo.Invest.entity.InvestProduct;
import com.example.demo.Invest.entity.InvestState;
import com.example.demo.Invest.InvestRedisRepository;
import com.example.demo.Invest.jpaRepository.InvestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
class InvestServiceTest {

    private static final int THREAD_POOL_SIZE = 30;

    @Autowired
    private InvestService investService;

    @Autowired
    private InvestRepository investRepository;

    @Autowired
    private InvestRedisRepository investRedisRepository;

    @Test
    @BeforeEach
    public void dbInit() throws Exception {
        for(int i=1;i<10;i++) {
            InvestProduct investProduct = InvestProduct.builder()
                    .productId(Long.parseLong(String.valueOf(i)))
                    .productTitle("투자의시대" + i)
                    .totalInvestingAmount(100000L)
                    .currentInvestingAmount(0L)
                    .investState(InvestState.ING)
                    .investors(0L)
                    .startedAt("2022-03-1"+ i + " 00:00:00")
                    .finishedAt("2022-03-2"+ i + " 23:59:59")
                    .build();

            boolean isPass = investRedisRepository.saveInvestProduct(investProduct);
            System.out.println("isPass = " + isPass);
        }
    }

    @Test
    public void investServiceTest() throws Exception {
        Collection<InvestProduct> investProducts = investRedisRepository.findInvestProductList();
        System.out.println("investProducts.size() = " + investProducts.size());
        investProducts.forEach(investProduct ->
                System.out.println("investProduct.getProductId() = " + investProduct.getFinishedAt())
        );
    }

    @Test
    public void dateTimeTest() throws Exception {
        System.out.println("LocalDateTime.now() = " + LocalDateTime.now());
    }


    @Test
    public void threadTest() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        Invest invest = Invest.builder()
                .createdAt("")
                .userId(1L)
                .productId(4L)
                .myInvestingAmount(10000L)
                .build();

        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            service.execute(() -> {
                investService.investByRedis(invest);
            });
        }

        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }
}