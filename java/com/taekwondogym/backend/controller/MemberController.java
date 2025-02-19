package com.taekwondogym.backend.controller;

import com.taekwondogym.backend.model.Member;
import com.taekwondogym.backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // Regular User: Get current user's member form
    @GetMapping("/current")
    public ResponseEntity<Member> getCurrentMemberForm(Principal principal) {
        String username = principal.getName();
        Member member = memberService.getMemberForm(username);
        return member != null ? ResponseEntity.ok(member) : ResponseEntity.notFound().build();
    }

    // Regular User: Save or update current user's member form
    @PostMapping("/current")
    public ResponseEntity<Member> saveCurrentMemberForm(Principal principal, @RequestBody Member memberDetails) {
        String username = principal.getName();
        Member savedMember = memberService.saveMemberForm(username, memberDetails);
        return ResponseEntity.ok(savedMember);
    }

    // Admin: Get any user's member form
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Member> getMemberFormById(@PathVariable Long id) {
        Member member = memberService.getMemberFormById(id);
        return member != null ? ResponseEntity.ok(member) : ResponseEntity.notFound().build();
    }

    // Admin: Update any user's member form
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Member> updateMemberFormById(@PathVariable Long id, @RequestBody Member memberDetails) {
        Member updatedMember = memberService.updateMemberFormById(id, memberDetails);
        return updatedMember != null ? ResponseEntity.ok(updatedMember) : ResponseEntity.notFound().build();
    }
    // Admin: Get member by Thai national ID
    @GetMapping("/by-thai-id/{thaiNationalId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Member> getMemberByThaiId(@PathVariable String thaiNationalId) {
        Member member = memberService.getMemberByThaiNationalId(thaiNationalId);
        return member != null ? ResponseEntity.ok(member) : ResponseEntity.notFound().build();
    }
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.findAllMembers();
        return ResponseEntity.ok(members);
    }
}