package com.taekwondogym.backend.service;

import com.taekwondogym.backend.model.Member;
import com.taekwondogym.backend.model.User;
import com.taekwondogym.backend.repository.MemberRepository;
import com.taekwondogym.backend.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserRepository userRepository;

    // Add or update a member form for the current user
    public Member saveMemberForm(String email, Member memberDetails) {
        User user = userRepository.findByEmail(email); // Use email instead of username
        if (user != null) {
            Member member = memberRepository.findByUserId(user.getId());
            if (member == null) {
                member = new Member();
                member.setUser(user);
            }
            // Update member details
            member.setName(memberDetails.getName());
            member.setSurname(memberDetails.getSurname());
            member.setTelephone(memberDetails.getTelephone());
            member.setThaiNationalId(memberDetails.getThaiNationalId());
            member.setWeight(memberDetails.getWeight());
            member.setHeight(memberDetails.getHeight());
            member.setBelts(memberDetails.getBelts());
            member.setGym(memberDetails.getGym());
            member.setCity(memberDetails.getCity());
            
            return memberRepository.save(member);
        }
        return null; // User not found
    }

    // Retrieve a member form by email
    public Member getMemberForm(String email) {
        User user = userRepository.findByEmail(email); // Use email instead of username
        return user != null ? memberRepository.findByUserId(user.getId()) : null;
    }

    // For admin: Retrieve any member form by user ID
    public Member getMemberFormById(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    // For admin: Update any member form by member ID
    public Member updateMemberFormById(Long memberId, Member updatedMemberDetails) {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member != null) {
            member.setName(updatedMemberDetails.getName());
            member.setSurname(updatedMemberDetails.getSurname());
            member.setTelephone(updatedMemberDetails.getTelephone());
            member.setThaiNationalId(updatedMemberDetails.getThaiNationalId());
            member.setWeight(updatedMemberDetails.getWeight());
            member.setHeight(updatedMemberDetails.getHeight());
            member.setBelts(updatedMemberDetails.getBelts());
            member.setGym(updatedMemberDetails.getGym());
            member.setCity(updatedMemberDetails.getCity());
            
            return memberRepository.save(member);
        }
        return null; // Member not found
    }
    
    // Admin: Get member by Thai national ID
    public Member getMemberByThaiNationalId(String thaiNationalId) {
        return memberRepository.findByThaiNationalId(thaiNationalId).orElse(null);
    }
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }
}