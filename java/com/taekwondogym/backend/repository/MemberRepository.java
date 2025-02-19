package com.taekwondogym.backend.repository;

import com.taekwondogym.backend.model.Member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // Find Member by the associated User's ID
    Member findByUserId(Long userId);
    Optional<Member> findByThaiNationalId(String thaiNationalId);
}
