package com.example.demo.repository;

import com.example.demo.entity.Invest;
import com.example.demo.entity.InvestProduct;
import com.example.demo.redis.RedisProvider;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RList;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class InvestRedisRepository {

    private final RedisProvider redisProvider;
//    private final RMap<String, InvestProduct> investProductRmap;

    public Collection<InvestProduct> findInvestProductList() {
        return redisProvider.map().values();
    }

    public InvestProduct findInvestProduct(String productId) {
        return redisProvider.map().get(productId);
    }

    public boolean update(InvestProduct investProduct) {
        return redisProvider.map().fastReplace(String.valueOf(investProduct.getProductId()), investProduct);
    }

    public boolean save(InvestProduct investProduct) {
        return redisProvider.map().fastPut(String.valueOf(investProduct.getProductId()), investProduct);
    }

    public List<Invest> findInvestByUser(String userId) {
        RList<Invest> invests = redisProvider.getRedissonClient().getList(userId);
        return invests.readAll();
    }

    public boolean addAll(List<Invest> investList, String userId) {
        RList<Invest> invests = redisProvider.getRedissonClient().getList(userId);
        return invests.addAll(investList);
    }
}
