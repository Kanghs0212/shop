package com.metato.shop.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public String saveMember(String username, String password,
                           String displayName, String email, String postcode,
                           String roadAddress, String jibun, String detailAddress,
                           String extraAddress){
        if(username.length() <= 3) return "아이디는 4자 이상이어야 합니다.";
        if(password.length() <= 3) return "비밀번호는 4자 이상이어야 합니다.";
        if(displayName.isEmpty()) return "닉네임을 입력해주세요.";

        if(memberRepository.findByUsername(username).isPresent())
            return "이미 사용 중인 아이디입니다.";
        if(memberRepository.findByDisplayName(displayName).isPresent())
            return "이미 사용 중인 닉네임입니다.";
        if(memberRepository.findByEmail(email).isPresent())
            return "이미 사용 중인 이메일입니다.";

        Member member = new Member();
        member.setUsername(username);
        member.setPassword(passwordEncoder.encode(password));
        member.setDisplayName(displayName);
        member.setEmail(email);
        member.setPostcode(postcode);
        member.setRoadAddress(roadAddress);
        member.setJibun(jibun);
        member.setDetailAddress(detailAddress);
        member.setExtraAddress(extraAddress);
        member.setRole("USER");

        memberRepository.save(member);
        return null;
    }

    public Optional<Member> findByUsername(String username){
        return memberRepository.findByUsername(username);
    }

}
