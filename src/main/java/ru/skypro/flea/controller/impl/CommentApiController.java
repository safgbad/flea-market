package ru.skypro.flea.controller.impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.flea.controller.CommentApi;
import ru.skypro.flea.dto.CommentDto;
import ru.skypro.flea.dto.CommentsDto;
import ru.skypro.flea.dto.CreateOrUpdateCommentDto;
import ru.skypro.flea.service.CommentService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Comments")
public class CommentApiController implements CommentApi {

    private final CommentService commentService;

    @Override
    public ResponseEntity<CommentsDto> getComments(int id) {
        CommentsDto comments = commentService.getComments(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(comments);
    }

    @Override
    public ResponseEntity<CommentDto> addComment(int id,
                                                 CreateOrUpdateCommentDto dto) {
        CommentDto comment = commentService.addComment(id, dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(comment);
    }

    @Override
    public ResponseEntity<Void> deleteComment(int adId,
                                              int commentId) {
        commentService.deleteComment(adId, commentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ResponseEntity<CommentDto> updateComment(int adId,
                                                    int commentId,
                                                    CreateOrUpdateCommentDto dto) {
        CommentDto comment = commentService.updateComment(adId, commentId, dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(comment);
    }

}
