package ru.skypro.flea.service;

import org.springframework.security.core.Authentication;
import ru.skypro.flea.dto.CommentDto;
import ru.skypro.flea.dto.CommentsDto;
import ru.skypro.flea.dto.CreateOrUpdateCommentDto;

public interface CommentService {

    CommentsDto getComments(int id);

    CommentDto addComment(int id,
                          CreateOrUpdateCommentDto comment,
                          Authentication authentication);

    void deleteComment(int adId,
                       int commentId,
                       Authentication authentication);

    CommentDto updateComment(int adId,
                             int commentId,
                             CreateOrUpdateCommentDto dto,
                             Authentication authentication);
}
