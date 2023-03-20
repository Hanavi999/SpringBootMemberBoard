package com.example.session.controller;

import com.example.session.adapter.MemberAdapter;
import com.example.session.model.Banner;
import com.example.session.model.Board;
import com.example.session.model.Comment;
import com.example.session.model.Image;
import com.example.session.model.dto.BoardDto;
import com.example.session.model.dto.CommentDto;
import com.example.session.service.impl.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardServiceImpl boardService;

    @GetMapping("/")
    public String main(Model model,
                       @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable, String keyword) {

        Page<Board> list = null;
        Banner banner = boardService.bannerView();


        if(Objects.isNull(keyword)) {
            list = boardService.listBoard(pageable);
            log.info("실패");
        } else {
            list = boardService.boardSearch(keyword, pageable);
            log.info("성공");
        }

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int lastPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", lastPage);
        model.addAttribute("banner", banner);

        return "main";
    }

    @GetMapping("/input")
    public String input() {
        return "input";
    }

    @PostMapping("/inputOk")
    public String inputOk(BoardDto boardDto,
                          @AuthenticationPrincipal MemberAdapter memberAdapter,
                          Principal principal,
                          @RequestParam("files") List<MultipartFile> files) throws IOException {

        boardService.writeBoard(boardDto, memberAdapter, principal);

        for(MultipartFile multipartFile : files) {
            boardService.imageUpload(multipartFile);
        }

        return "redirect:/";
    }


    @GetMapping("/view")
    public String view(@AuthenticationPrincipal MemberAdapter memberAdapter, Model model, Long id) {
        List<Comment> comments = boardService.listComment();
        List<Image> img = boardService.imageView(id);

        model.addAttribute("board", boardService.boardView(id));
        model.addAttribute("user", memberAdapter.getMember());
        model.addAttribute("comments", comments);
        model.addAttribute("like", boardService.likeCount(id));
        model.addAttribute("imgList", img);

        return "view";
    }

    @PostMapping("/del")
    public String del(BoardDto board) {
        boardService.boardDelete(board);
        return "redirect:/";
    }

    @PostMapping("/comment")
    public String comment(@AuthenticationPrincipal MemberAdapter memberAdapter, CommentDto commentDto) {
        boardService.writeComment(memberAdapter, commentDto);
        return "redirect:/view?id=" + commentDto.getBoardId();
    }

    @PostMapping("/del-comment")
    public String delComment(CommentDto commentDto) {
        boardService.deleteComment(commentDto);
        return "redirect:/view?id=" + commentDto.getBoardId();
    }

    @PostMapping("/like")
    public String like(BoardDto boardDto, @AuthenticationPrincipal MemberAdapter memberAdapter, Principal principal) {
        boardService.clickLike(boardDto, memberAdapter, principal);
        return "redirect:/view?id=" + boardDto.getId();
    }

    @PostMapping("/modify")
    public String modify(Long id, Model model) {
        model.addAttribute("board", boardService.boardView(id));
        return "modify";
    }

    @PostMapping("/modifyOk")
    public String modifyOk(BoardDto boardDto) {
        boardService.modifyBoard(boardDto);
        return "redirect:/";
    }



}
