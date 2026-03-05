package com.metato.shop.Test;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class age {

    private Long id;
    private String name;
    private Integer age;


    public void 한살더하기(){
        this.age++;
    }

    public void 나이설정(Integer age){
        if(age>=0 && age<=100)
            this.age = age;
    }


}

