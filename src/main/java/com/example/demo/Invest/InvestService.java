package com.example.demo.Invest;

import com.example.demo.Invest.entity.Invest;
import com.example.demo.Invest.entity.InvestProduct;
import com.example.demo.Invest.jpaRepository.InvestProductRepository;
import com.example.demo.error.CreateException;
import com.example.demo.Invest.jpaRepository.InvestRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.example.demo.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class InvestService {
    private final InvestRepository investRepository;
    private final InvestProductRepository investProductRepository;
    private final InvestRedisRepository investRedisRepository;
    private final RedissonClient redissonClient;

    public boolean investByRedis(Invest invest) {
        boolean status = false;
        RLock lock = redissonClient.getLock(String.valueOf(invest.getProductId()));
        try {
            boolean isLocked = lock.tryLock(2, 2, TimeUnit.SECONDS);
            if (!isLocked) throw new CreateException(REDIS_LOCK_FAILED);

            // 저장된 investProduct 가져오기
            InvestProduct investProduct = investRedisRepository.findInvestProduct(String.valueOf(invest.getProductId()));
            if (investProduct == null) throw new CreateException(PRODUCT_NOT_FOUND);

            // 업데이트 로직
            investProduct.addAmount(invest.getMyInvestingAmount());
            invest.setTotalInvestingAmount(investProduct.getTotalInvestingAmount());
            invest.setProductTitle(investProduct.getProductTitle());

            // 업데이트, async, transaction 필요
            CompletableFuture<Boolean> isPass = CompletableFuture.supplyAsync(
                    () -> investRedisRepository.updateInvestProduct(investProduct));
            CompletableFuture<Boolean> isPass2 = CompletableFuture.supplyAsync(
                    () -> investRedisRepository.addInvest(invest, String.valueOf(invest.getUserId())));
            if (!isPass.get()) throw new CreateException(NETWORK_FAILED);
            if (!isPass2.get()) throw new CreateException(NETWORK_FAILED);

            status = true;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return status;
    }

    public List<Invest> findInvestListByRedis(Long userId) {
        return investRedisRepository.findInvestByUser(String.valueOf(userId));
    }

    public Collection<InvestProduct> findInvestProductListByRedis() {
//        LocalDateTime currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return investRedisRepository.findInvestProductList();
    }

    public boolean saveInvestProductByRedis(InvestProduct investProduct) {
        return investRedisRepository.saveInvestProduct(investProduct);
    }

    public Invest investByJpa(Invest invest) {
        return investRepository.save(invest);
    }

    public InvestProduct saveInvestProduct(InvestProduct investProduct) {
        return investProductRepository.save(investProduct);
    }

    private static Long parseDate(String time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(time).getTime();
    }
}
