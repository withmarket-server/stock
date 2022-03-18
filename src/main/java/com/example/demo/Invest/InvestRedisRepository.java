package com.example.demo.Invest;

import com.example.demo.Invest.entity.Invest;
import com.example.demo.Invest.entity.InvestProduct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class InvestRedisRepository {

    private final RedissonClient redissonClient;

    public Collection<InvestProduct> findInvestProductList() {
        RMap<String, InvestProduct> map = redissonClient.getMap("Invest");
        return map.values();
    }

    public InvestProduct findInvestProduct(String productId) {
        RMap<String, InvestProduct> map = redissonClient.getMap("Invest");
        return map.get(productId);
    }

    public boolean updateInvestProduct(InvestProduct investProduct) {
        RMap<String, InvestProduct> map = redissonClient.getMap("Invest");
        return map.fastReplace(String.valueOf(investProduct.getProductId()), investProduct);
    }

    public boolean saveInvestProduct(InvestProduct investProduct) {
        RMap<String, InvestProduct> map = redissonClient.getMap("Invest");
        return map.fastPut(String.valueOf(investProduct.getProductId()), investProduct);
    }

    public List<Invest> findInvestByUser(String userId) {
        RList<Invest> invests = redissonClient.getList(userId);
        return invests.readAll();
    }

    public boolean addInvest(Invest invest, String userId) {
        RList<Invest> invests = redissonClient.getList(userId);
        return invests.add(invest);
    }

    public boolean addInvestList(List<Invest> investList, String userId) {
        RList<Invest> invests = redissonClient.getList(userId);
        return invests.addAll(investList);
    }
}
