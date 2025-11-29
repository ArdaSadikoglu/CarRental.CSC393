package com.arda.cihangir.ozu.csc393.car_rental.service;

import com.arda.cihangir.ozu.csc393.car_rental.model.Member;
import com.arda.cihangir.ozu.csc393.car_rental.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testCreateAndFind() {

        Member m = new Member(
                null,
                "Arda",
                "Istanbul",
                "arda@mail.com",
                "5321112233",
                "DL001",
                null
        );

        memberService.create(m);

        Member found = memberService.getById(m.getId());

        assertNotNull(found);
        assertEquals("Arda", found.getName());
        assertEquals("Istanbul", found.getAddress());
    }
}