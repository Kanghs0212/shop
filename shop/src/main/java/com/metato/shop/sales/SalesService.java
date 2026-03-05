package com.metato.shop.sales;


import com.metato.shop.Member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesRepository salesRepository;

    public void saveSales(String title, Integer price, Integer orderCount, Long id){
        Sales sales = new Sales();
        sales.setItemName(title);
        sales.setPrice(price*orderCount);
        sales.setCount(orderCount);

        var member = new Member();
        member.setId(id);
        sales.setMember(member);

        salesRepository.save(sales);
    }


    public List<Sales> customFindAll(){
        List<Sales> result = salesRepository.customFindAll();
        return result;
    }
}
