package com.metato.shop.Test;


import jakarta.persistence.*;

import java.util.Date;

@Entity
public class test {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String title;
    public String date;
}
