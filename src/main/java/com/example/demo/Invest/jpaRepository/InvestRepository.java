package com.example.demo.Invest.jpaRepository;

import com.example.demo.Invest.entity.Invest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestRepository extends JpaRepository<Invest, Long> {
    List<Invest> findAllByUserId(Long userId);
}
