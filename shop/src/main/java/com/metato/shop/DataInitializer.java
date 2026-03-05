package com.metato.shop;

import com.metato.shop.Item.Item;
import com.metato.shop.Item.ItemRepository;
import com.metato.shop.Member.Member;
import com.metato.shop.Member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (memberRepository.count() == 0) {
            Member admin = new Member();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin1234"));
            admin.setDisplayName("관리자");
            admin.setEmail("admin@metato.com");
            admin.setRole("ADMIN");
            admin.setPostcode("12345");
            admin.setRoadAddress("서울시 강남구");
            admin.setJibun("강남동");
            admin.setDetailAddress("101호");
            admin.setExtraAddress("");
            memberRepository.save(admin);

            Member user = new Member();
            user.setUsername("user1");
            user.setPassword(passwordEncoder.encode("user1234"));
            user.setDisplayName("홍길동");
            user.setEmail("user1@metato.com");
            user.setRole("USER");
            user.setPostcode("54321");
            user.setRoadAddress("서울시 서초구");
            user.setJibun("서초동");
            user.setDetailAddress("202호");
            user.setExtraAddress("");
            memberRepository.save(user);
        }

        if (itemRepository.count() == 0) {
            String[][] items = {
                {"트렌디 체크셔츠", "39000", "깔끔한 체크 패턴의 셔츠입니다.", "/images/checkshirt.jpg", "상의"},
                {"클래식 면바지", "49000", "편안한 착용감의 코튼 진입니다.", "/images/cottonjean.jpg", "하의"},
                {"캐주얼 후드티", "35000", "따뜻하고 편안한 후드티입니다.", "/images/hood.jpg", "상의"},
                {"슬림핏 청바지", "45000", "슬림한 핏의 청바지입니다.", "/images/jean.jpg", "하의"},
                {"조거 진", "42000", "활동적인 스타일의 조거 진입니다.", "/images/joggerjean.jpg", "하의"},
                {"베이직 셔츠", "32000", "깔끔한 기본 셔츠입니다.", "/images/shirt.jpg", "상의"},
                {"니트 스웨터", "55000", "따뜻한 니트 스웨터입니다.", "/images/sweater.jpg", "상의"},
                {"라운드 티셔츠", "25000", "기본 라운드넥 티셔츠입니다.", "/images/tshirt.jpg", "상의"},
                {"오버핏 후드집업", "48000", "오버핏 후드집업입니다.", "/images/hood.jpg", "상의"},
                {"와이드 팬츠", "38000", "편안한 와이드 핏 팬츠입니다.", "/images/cottonjean.jpg", "하의"},
                {"스트라이프 셔츠", "36000", "세련된 스트라이프 패턴 셔츠입니다.", "/images/shirt.jpg", "상의"},
                {"데님 자켓", "65000", "클래식한 데님 자켓입니다.", "/images/jean.jpg", "상의"},
            };

            for (String[] data : items) {
                Item item = new Item();
                item.setTitle(data[0]);
                item.setPrice(Integer.parseInt(data[1]));
                item.setText(data[2]);
                item.setImgUrl(data[3]);
                item.setCategory(data[4]);
                item.setUsername("admin");
                itemRepository.save(item);
            }
        }
    }
}
