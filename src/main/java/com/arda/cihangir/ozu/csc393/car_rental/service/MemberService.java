package com.arda.cihangir.ozu.csc393.car_rental.service;

import com.arda.cihangir.ozu.csc393.car_rental.model.Member;
import com.arda.cihangir.ozu.csc393.car_rental.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<Member> getAll() {
        return memberRepository.findAll();
    }
    public Member create(Member m) {
        return memberRepository.save(m);
    }

    public Member getById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }
}