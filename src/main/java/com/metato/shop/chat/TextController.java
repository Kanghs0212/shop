package com.metato.shop.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metato.shop.Item.Item;
import com.metato.shop.Member.CustomUser;
import com.metato.shop.Member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TextController {

    private final TextRepository textRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    @GetMapping("/chat")
    public String comment(@RequestParam Long userId, Model model){
        Optional<Room> tmp = roomRepository.findByUserId(userId);

        if(tmp.isEmpty()){
            var user = memberRepository.findById(userId);
            Room room = new Room();
            room.setUserId(userId);
            room.setUsername(user.get().getUsername());
            roomRepository.save(room);
            tmp = roomRepository.findByUserId(userId);
        }

        Room room = tmp.get();
        List<Text> result = textRepository.findByRoomId(room.getId());

        model.addAttribute("roomNum", room.getId());
        model.addAttribute("role", "USER");

        if(!result.isEmpty()){
            model.addAttribute("texts", result);
        }
        // 채팅방 번호를 통해 텍스트들 검색 후 불러옴
        return "chat.html";
    }

    @GetMapping("/chat/admin")
    public String commentAdmin(@RequestParam Long userId, Model model, Authentication auth){
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        if (customUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"))) {
            return "error403.html";
        }
        Optional<Room> tmp = roomRepository.findByUserId(userId);
        Room room = tmp.get();

        List<Text> result = textRepository.findByRoomId(room.getId());
        model.addAttribute("roomNum", room.getId());
        model.addAttribute("role", "ADMIN");
        if(!result.isEmpty()){
            model.addAttribute("texts", result);
        }
        // 채팅방 번호를 통해 텍스트들 검색 후 불러옴
        return "chat.html";
    }

    @GetMapping("/chat/inform")
    public String inform(Authentication auth, Model model){
        CustomUser customUser = (CustomUser) auth.getPrincipal();
        model.addAttribute("userId", customUser.id);
        return "inquiry.html";
    }
    @GetMapping("/chat/all/{id}")
    public String chatRooms(Model model, @PathVariable Integer id){
        Page<Room> result = roomRepository.findPageBy(PageRequest.of(id-1, 10));
        model.addAttribute("rooms",result);
        model.addAttribute("cnt", result.getTotalPages());
        model.addAttribute("cnt_now", id);
        return "chatRooms.html";
    }


    @PostMapping("/chat/add")
    public ResponseEntity<Void> message(@RequestBody Map<String, Object> data, Authentication auth){
        CustomUser customUser = (CustomUser) auth.getPrincipal();

        try{
            String messageInput = (String) data.get("messageInput");
            Long roomNum = Long.valueOf(data.get("roomNum").toString());
            Text text = new Text();

            Room room = roomRepository.findById(roomNum)
                    .orElseThrow(() -> new EntityNotFoundException("Room not found"));

            text.setText(messageInput);
            text.setRoom(room);
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

    @DeleteMapping("/chat/delete/{userId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long userId) {
        try {
            // 해당 userId로 Room 찾기
            Optional<Room> roomOptional = roomRepository.findByUserId(userId);
            if (roomOptional.isPresent()) {
                Room room = roomOptional.get();

                roomRepository.delete(room);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 삭제 성공
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 채팅방 없음
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 서버 오류
        }
    }
}
