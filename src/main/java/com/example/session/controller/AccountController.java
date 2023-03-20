package com.example.session.controller;

import com.example.session.adapter.MemberAdapter;
import com.example.session.model.Board;
import com.example.session.model.Comment;
import com.example.session.model.Member;
import com.example.session.service.impl.BoardServiceImpl;
import com.example.session.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final BoardServiceImpl boardService;

    private final UserServiceImpl userService;

    @GetMapping("/dashBoard")
    public String dashboard(@AuthenticationPrincipal MemberAdapter memberAdapter, Model model, Principal principal) {
        model.addAttribute("user", memberAdapter.getMember());
        model.addAttribute("write", userService.listBoard(principal));
        model.addAttribute("comments", userService.commentList(principal));

        return "dashboard";
    }

    @GetMapping("/adminPage")
    public String adminPage(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Board> list = boardService.listBoard(pageable);
        List<Comment> comments = boardService.listComment();
        List<Member> member = userService.memberList();

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int lastPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", lastPage);
        model.addAttribute("comments", comments);
        model.addAttribute("user", member);


        return "adminPage";
    }

    @PostMapping("/bannerChange")
    public String bannerChange(@RequestParam("files") MultipartFile file) throws IOException {
        boardService.bannerUpdate(file);
        return "redirect:/";
    }

    @PostMapping("/userBan")
    public String banUser(Long id) {

        userService.BanUser(id);

        return "redirect:/adminPage";
    }

    @PostMapping("/clearBan")
    public String clearBan(Long id) {
        userService.ClearUser(id);

        return "redirect:/adminPage";
    }




}
