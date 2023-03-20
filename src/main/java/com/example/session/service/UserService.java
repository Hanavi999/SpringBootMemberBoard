package com.example.session.service;

import com.example.session.model.Board;
import com.example.session.model.Comment;
import com.example.session.model.Member;
import com.example.session.model.dto.MemberDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface UserService {

    void createAccount(MemberDto memberDto);

    List<Board> listBoard(Principal principal);

    List<Comment> commentList(Principal principal);

    List<Member> memberList();

    void BanUser(Long id);

    void ClearUser(Long id);



}
