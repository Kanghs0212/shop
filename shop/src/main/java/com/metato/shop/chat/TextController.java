package com.metato.shop.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metato.shop.Member.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TextController {

    private final TextRepository textRepository;
    private final RoomRepository roomRepository;

    @GetMapping("/chat")
    public String comment(@RequestParam Long userId, Model model){
        List<Text> result = textRepository.findByUserId(userId);
        Optional<Room> tmp = roomRepository.findByUserId(userId);

        if(tmp.isEmpty()){
            Room room = new Room();
            room.setUserId(userId);
            roomRepository.save(room);
        }

        if(!result.isEmpty()){
            model.addAttribute("texts", result);
        }
        // 채팅방 번호를 통해 텍스트들 검색 후 불러옴
        return "chat.html";
    }

    @GetMapping("/chat/inform")
    public String inform(Authentication auth, Model model){
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        System.out.println(customUser.getAuthorities());

        model.addAttribute("userId", customUser.id);
        return "inquiry.html";
    }
    @GetMapping("/chat/all")
    public String chatRooms(){
        return "chatRooms.html";
    }


    @PostMapping("/chat/add")
    public ResponseEntity<Void> message(@RequestBody String messageInput, Authentication auth){
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        try{

            Text text = new Text();
            text.setUserId(customUser.id);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(messageInput);
            String message = jsonNode.get("messageInput").asText();

            text.setText(message);
            System.out.println(customUser.getAuthorities());

            if (customUser.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
                text.setAdmin(true);
            } else text.setAdmin(false);

            textRepository.save(text);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
