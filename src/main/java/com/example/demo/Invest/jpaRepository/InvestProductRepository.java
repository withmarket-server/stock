package com.example.demo.Invest.jpaRepository;

import com.example.demo.Invest.entity.InvestProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestProductRepository extends JpaRepository<InvestProduct, Long> {
}
