package com.example.session.service.impl;

import com.example.session.adapter.MemberAdapter;
import com.example.session.model.Board;
import com.example.session.model.Comment;
import com.example.session.model.Member;
import com.example.session.model.dto.MemberDto;
import com.example.session.model.Role;
import com.example.session.repository.BoardRepository;
import com.example.session.repository.CommentRepository;
import com.example.session.repository.UserRepository;
import com.example.session.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final BoardRepository boardRepository;

    private final CommentRepository commentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            log.info("USER FOUND IN THE DATABASE: {}", username);

            if (userOpt.get().getRole().equals("ROLE_BAN")) {
                log.info("This user is BAN");
                return null;
            }

            return new MemberAdapter(userOpt.get());
        }
        throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
    }

    @Override
    public void createAccount(MemberDto memberDto) {
        Member member = Member.builder()
                        .username(memberDto.getUsername())
                        .password(passwordEncoder.encode(memberDto.getPassword()))
                        .nickname(memberDto.getNickname())
                        .role(Role.USER.getKey()).build();
        userRepository.save(member);
    }

    @Override
    public List<Board> listBoard(Principal principal) {
        return boardRepository.findByUsername(principal.getName());
    }

    @Override
    public List<Comment> commentList(Principal principal) {
        return commentRepository.findByUsername(principal.getName());
    }

    @Override
    public List<Member> memberList() {
        return userRepository.findAll();
    }

    @Override
    public void BanUser(Long id) {
        Member member = userRepository.findById(id).orElse(null);
        member.setRole("ROLE_BAN");
        userRepository.save(member);

    }

    @Override
    public void ClearUser(Long id) {
        Member member = userRepository.findById(id).orElse(null);
        member.setRole("ROLE_USER");
        userRepository.save(member);
    }
}
