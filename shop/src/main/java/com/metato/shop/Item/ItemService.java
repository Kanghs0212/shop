package com.metato.shop.Item;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    public void saveItem(String title, Integer price, String text, String imgURL, Authentication auth){
        var item = new Item();

        if(title!=null && price!=null && price<=100000000){
            item.setTitle(title);
            item.setPrice(price);
            item.setText(text);
            item.setImgUrl(imgURL);
            item.setUsername(auth.getName());
            itemRepository.save(item);
        }
    }

    public void updateItem(String title, Integer price, Long id, Authentication auth){

        if(title!=null && price!=null && price<=100000000 && price>=0 && title.length()<100){
            Item item = new Item();
            item.setId(id);
            item.setTitle(title);
            item.setPrice(price);
            item.setUsername(auth.getName());
            itemRepository.save(item);
        }
    }

    public void deleteItem(Long id){

        itemRepository.deleteById(id);
        System.out.println(1);
    }

    public Page<Item> findPageBy(Integer pageNum){
        Page<Item> result = itemRepository.findPageBy(PageRequest.of(pageNum-1, 5));
        return result;
    }

    public Page<Item> findPageThree(){
        Page<Item> result = itemRepository.findPageBy(PageRequest.of(0, 3));
        return result;
    }

    public List<Item> findAllItem(){
        List<Item> result = itemRepository.findAll();
        return result;
    }

    public Optional<Item> findById(Long id){
        Optional<Item> result = itemRepository.findById(id);
        return result;
    }

    public List<Item> searchText(String searchText){
        List<Item> result  = itemRepository.rawQuery1(searchText);
        return result;
    }

}
