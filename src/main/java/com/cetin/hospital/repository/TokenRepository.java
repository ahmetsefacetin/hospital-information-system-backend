package com.cetin.hospital.repository;

import com.cetin.hospital.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByUserId(Long userId);
}