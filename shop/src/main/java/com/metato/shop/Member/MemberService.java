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

    public void saveMember(String username, String password, String displayName){
        if(username.length()>3 && password.length()>3 && displayName.length()>0){
            Member member = new Member();
            member.setUsername(username);

            var pw = passwordEncoder.encode(password);
            member.setPassword(pw);

            member.setDisplayName(displayName);
            member.setRole("USER");
            memberRepository.save(member);
        }
    }

    public Optional<Member> findByUsername(String username){
        Optional<Member> result = memberRepository.findByUsername(username);
        return result;
    }

}
