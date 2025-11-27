package com.arda.cihangir.ozu.csc393.car_rental.controller;

import com.arda.cihangir.ozu.csc393.car_rental.model.Member;
import com.arda.cihangir.ozu.csc393.car_rental.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAll();
    }
}