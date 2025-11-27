package com.arda.cihangir.ozu.csc393.car_rental.repository;

import com.arda.cihangir.ozu.csc393.car_rental.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}