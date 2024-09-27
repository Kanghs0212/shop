package com.metato.shop.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TextRepository extends JpaRepository<Text, Long> {
    List<Text> findByUserId(Long userId);
}
