package com.example.session.service.impl;

import com.example.session.adapter.MemberAdapter;
import com.example.session.model.*;
import com.example.session.model.dto.BoardDto;
import com.example.session.model.dto.CommentDto;
import com.example.session.repository.*;
import com.example.session.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    @Value("${file.dir}")
    private String fileDir;

    @Value("${file.banner}")
    private String bannerDir;

    private final BoardRepository boardRepository;

    private final CommentRepository commentRepository;

    private final LikeRepository likeRepository;

    private final ImageRepository imageRepository;

    private final BannerRepository bannerRepository;


    @Override
    public void writeBoard(BoardDto boardDto,
                           @AuthenticationPrincipal MemberAdapter memberAdapter,
                           Principal principal) {

        Board board = Board.builder()
                .username(principal.getName())
                .nickname(memberAdapter.getMember().getNickname())
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .count(0L)
                .build();

        boardRepository.save(board);

    }

    @Override
    public void imageUpload(MultipartFile file) throws IOException {
        if(!file.isEmpty()) {
            String origName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String extension = origName.substring(origName.lastIndexOf("."));
            String savedName = uuid + extension;
            String savePath = System.getProperty("user.dir") + fileDir + savedName;

            Image image = Image.builder()
                    .orgNm(origName)
                    .savedNm(savedName)
                    .savedPath(savePath)
                    .boardId(boardRepository.findFirstId())
                    .build();

            file.transferTo(new File(savePath));

            imageRepository.save(image);
        }
    }

    @Override
    public void bannerUpdate(MultipartFile file) throws IOException {
        if(!file.isEmpty()) {
            String directoryPath = System.getProperty("user.dir") + "/src/main/resources/static/banner/";
            File directory = new File(directoryPath);
            System.out.println(directory);
            if(directory.exists()) {
                File[] files = directory.listFiles();
                for(File file1 : files) {
                    file1.delete();
                }
            }

            bannerRepository.truncateTable();

            String origName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String extension = origName.substring(origName.lastIndexOf("."));
            String savedName = uuid + extension;
            String savePath = System.getProperty("user.dir") + bannerDir + savedName;

            Banner banner = Banner.builder()
                    .orgNm(origName)
                    .savedNm(savedName)
                    .savedPath(savePath)
                    .build();

            file.transferTo(new File(savePath));

            bannerRepository.save(banner);


        }
    }

    @Override
    public Page<Board> listBoard(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Override
    public List<Comment> listComment() {
        return commentRepository.findAll();
    }

    @Override
    public Board boardView(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        Board boardModify = boardRepository.findById(id).orElse(null);
        Long count = board.get().getCount() + 1;
        if(count == 30) {
            boardModify.setTitle("[인기] " + boardModify.getTitle());
        }
        boardModify.setCount(count);
        boardRepository.save(boardModify);
        return board.get();
    }

    @Override
    public List<Image> imageView(Long id) {
        return imageRepository.findAllByBoardId(id);
    }

    @Override
    public Banner bannerView() {
        Optional<Banner> bannerOptional = bannerRepository.findById(Long.valueOf(1));
        if(bannerOptional.isPresent()) {
            return bannerOptional.get();
        }
        else {
            return null;
        }
    }

    @Override
    public void boardDelete(BoardDto board) {
        Optional<Board> boardDto = boardRepository.findById(board.getId());
        List<Image> image = imageRepository.findByBoardId(board.getId());
        List<Comment> comments = commentRepository.findByBoardId(board.getId());
        System.out.println("dlalwl : " + image);
        int size = image.size();
        int comSize = comments.size();

        for (int i = 0; i < comSize; i++) {
            commentRepository.delete(comments.get(i));
        }

        for(int i = 0; i < size; i++) {
            File delFile = new File(image.get(i).getSavedPath());
            imageRepository.delete(image.get(i));
            if (delFile.exists()) {
                delFile.delete();
            }
        }

        boardRepository.delete(boardDto.get());
    }

    @Override
    public void writeComment(@AuthenticationPrincipal MemberAdapter memberAdapter, CommentDto commentDto) {
        Comment comment = Comment.builder()
                .username(memberAdapter.getUsername())
                .nickname(memberAdapter.getMember().getNickname())
                .comment(commentDto.getComment())
                .boardId(commentDto.getBoardId())
                .build();

        commentRepository.save(comment);

    }

    @Override
    public void deleteComment(CommentDto commentDto) {
        Optional<Comment> comment = commentRepository.findById(commentDto.getId());
        commentRepository.delete(comment.get());
    }

    @Override
    public void clickLike(BoardDto boardDto, @AuthenticationPrincipal MemberAdapter memberAdapter, Principal principal) {

        Optional<BoardLike> board = likeRepository.findByBoardIdAndUsername(boardDto.getId(), principal.getName());
        System.out.println(board.isPresent());
        if(!board.isPresent()) {
            BoardLike boardLike = BoardLike.builder()
                    .username(principal.getName())
                    .nickname(memberAdapter.getMember().getNickname())
                    .boardId(boardDto.getId())
                    .build();

            likeRepository.save(boardLike);
        }

    }

    @Override
    public Long likeCount(Long id) {
        Long count = likeRepository.countByBoardId(id);
        Board boardModify = boardRepository.findById(id).orElse(null);
        if(count == 10) {
            boardModify.setTitle("[추천] " + boardModify.getTitle());
            boardRepository.save(boardModify);
        }

        return count;
    }

    @Override
    public Page<Board> boardSearch(String keyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(keyword, pageable);
    }

    @Override
    public void modifyBoard(BoardDto boardDto) {

        Board board = boardRepository.findById(boardDto.getId()).orElse(null);
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        boardRepository.save(board);
    }
}
