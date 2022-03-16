package com.example.demo.service;

import com.example.demo.entity.Invest;
import com.example.demo.entity.InvestProduct;
import com.example.demo.error.CustomException;
import com.example.demo.repository.InvestRedisRepository;
import com.example.demo.repository.InvestRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.demo.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class InvestService {
    private final InvestRepository investRepository;
    private final InvestRedisRepository InvestRedisRepository;
    private final RedissonClient redissonClient;

    public Invest invest(Invest invest) {

        RLock lock = redissonClient.getLock(String.valueOf(invest.getProductId()));
        try {
            boolean isLocked = lock.tryLock(1, 2, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new CustomException(REDIS_LOCK_FAILED);
            }

            // 저장된 investProduct 가져오기
            InvestProduct investProduct = InvestRedisRepository.findInvestProduct(String.valueOf(invest.getProductId()));
            if (investProduct == null) {
                throw new CustomException(PRODUCT_NOT_FOUND);
            }

            // 비즈니스 로직
            investProduct.addAmount(invest.getMyInvestingAmount());

            // 업데이트
            boolean isPass = InvestRedisRepository.update(investProduct);
            if (!isPass) {
                throw new CustomException(NETWORK_FAILED);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

//        Invest.builder()
//                .productTitle(investProduct.getProductTitle())
//                .totalInvestingAmount(investProduct.getTotalInvestingAmount())
//                .build();
        return investRepository.save(invest);
    }

    public List<Invest> findInvestList(Long userId) {
        return investRepository.findAllByUserId(userId);
    }

    public Collection<InvestProduct> findInvestProductList() {
//        LocalDateTime currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return InvestRedisRepository.findInvestProductList();
    }

    public boolean saveInvestProduct(InvestProduct investProduct) {
        return InvestRedisRepository.save(investProduct);
    }

    private static Long parseDate(String time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(time).getTime();
    }
}
