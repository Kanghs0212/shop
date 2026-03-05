package com.metato.shop.sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByMemberId(Long memberId);
    @Transactional
    void deleteByIdAndMemberId(Long id, Long memberId);
}
