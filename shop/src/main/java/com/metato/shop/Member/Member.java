package com.metato.shop.Member;
import com.metato.shop.sales.Sales;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter
@ToString
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 고유키라고 선언// 알아서 1씩 증가시켜줌
    private Long id;

    @Column(nullable = false, unique = true )
    private String username;
    @Column(nullable = false, unique = true )
    private String password;
    @Column(nullable = false, unique = true )
    private String displayName;
    @Column(nullable = false, unique = true )
    private String email;

    private String postcode;
    private String roadAddress;
    private String jibun;
    private String detailAddress;
    private String extraAddress;

    @Column(nullable = false)
    private String role;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Sales> sales = new ArrayList<>();
}