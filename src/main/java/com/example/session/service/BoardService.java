package com.example.session.service;

import com.example.session.adapter.MemberAdapter;
import com.example.session.model.Banner;
import com.example.session.model.Board;
import com.example.session.model.Comment;
import com.example.session.model.Image;
import com.example.session.model.dto.BoardDto;
import com.example.session.model.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Service

public interface BoardService {

    void writeBoard(BoardDto boardDto, @AuthenticationPrincipal MemberAdapter memberAdapter, Principal principal);

    void imageUpload(MultipartFile file) throws IOException;

    void bannerUpdate(MultipartFile file) throws IOException;

    Page<Board> listBoard(Pageable pageable);

    List<Comment> listComment();

    Board boardView(Long id);

    List<Image> imageView(Long id);

    Banner bannerView();

    void boardDelete(BoardDto board);

    void writeComment(@AuthenticationPrincipal MemberAdapter memberAdapter, CommentDto commentDto);

    void deleteComment(CommentDto commentDto);

    void clickLike(BoardDto boardDto, @AuthenticationPrincipal MemberAdapter memberAdapter, Principal principal);

    Long likeCount(Long id);

    Page<Board> boardSearch(String keyword, Pageable pageable);

    void modifyBoard(BoardDto boardDto);

}
