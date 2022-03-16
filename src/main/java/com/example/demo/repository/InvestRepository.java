package com.example.demo.repository;

import com.example.demo.entity.Invest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestRepository extends JpaRepository<Invest, Long> {
    List<Invest> findAllByUserId(Long userId);
}
