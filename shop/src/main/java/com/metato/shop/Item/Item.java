package com.metato.shop.Item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity @Getter @Setter
@Table(indexes = @Index(columnList = "title", name="작명"))
@ToString // 오브젝트를 출력하면 한번에 보이지 않아 불편하므로 이를 개선해줌
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 고유키라고 선언// 알아서 1씩 증가시켜줌
    private Long id;

    @Column(nullable = false)
    // 또는 괄호안에 columnDefinition = "TEXT"를 넣으면 엄청나게 긴 스트링타입도됨
    // 또는 length = 1000으로 설정
    private String title;
    @Column(nullable = false)
    private Integer price;

    private String text;

    @Column(unique = false)
    private String username;

    private String imgUrl;

    /*
    public String toString(){
        return this.title + this.price;
    }
    */
}
