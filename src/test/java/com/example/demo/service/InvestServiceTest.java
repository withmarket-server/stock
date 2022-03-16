package com.example.demo.service;

import com.example.demo.entity.Invest;
import com.example.demo.entity.InvestProduct;
import com.example.demo.entity.InvestState;
import com.example.demo.repository.InvestRedisRepository;
import com.example.demo.repository.InvestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
class InvestServiceTest {

    @Autowired
    private InvestService investService;
    @Autowired
    private InvestRepository investRepository;

    @Autowired
    private InvestRedisRepository InvestRedisRepository;

    @Test
    public void dbInit() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for(int i=5;i<10;i++) {
            LocalDateTime dateTime = LocalDateTime.parse("2022-03-0"+ i + " 00:00:00", formatter);
            LocalDateTime dateTime2 = LocalDateTime.parse("2022-03-1"+ i + " 23:59:59", formatter);

            InvestProduct investProduct = InvestProduct.builder()
                    .productId(Long.parseLong(String.valueOf(i)))
                    .productTitle("투자의시대" + i)
                    .totalInvestingAmount(100000L)
                    .currentInvestingAmount(0L)
                    .investState(InvestState.ING)
                    .investors(0L)
                    .startedAt(dateTime)
                    .finishedAt(dateTime2)
                    .build();

            boolean isPass = InvestRedisRepository.update(investProduct);
            System.out.println("isPass = " + isPass);
        }
    }

    @Test
    public void investServiceTest() throws Exception {
        Collection<InvestProduct> investProducts = InvestRedisRepository.findInvestProductList();
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
    public void redissonListOpsTest() throws Exception {
        //given
        Long userId = 1515L;
        InvestProduct investProduct = InvestRedisRepository.findInvestProduct("1");
        List<Invest> newList = new ArrayList<>();

        for (int i = 0;i < 10; i++) {
            Invest invest = Invest.builder()
                    .userId(userId)
                    .productId(investProduct.getProductId())
                    .productTitle(investProduct.getProductTitle())
                    .totalInvestingAmount(investProduct.getTotalInvestingAmount())
                    .myInvestingAmount(5000L)
                    .createdAt(LocalDateTime.now())
                    .build();

            newList.add(invest);
        }

        System.out.println("newList.size() = " + newList.size());

        InvestRedisRepository.addAll(newList, String.valueOf(userId));

        List<Invest> invests = InvestRedisRepository.findInvestByUser(String.valueOf(userId));

        invests.forEach(e -> System.out.println("e = " + e.getProductTitle()));
    }
}