package com.metato.shop.sales;

import com.metato.shop.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;
    private String title;
    private Integer price;
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",
                foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;
}
