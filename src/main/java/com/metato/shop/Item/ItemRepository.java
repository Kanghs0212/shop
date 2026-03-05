package com.metato.shop.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findPageBy(Pageable page);
    List<Item> findAllByTitleContains(String title);

    @Query(value="select * from item where lower(title) like lower(concat('%', ?1, '%'))", nativeQuery = true)
    List<Item> rawQuery1(String text);
}
