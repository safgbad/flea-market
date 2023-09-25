package ru.skypro.flea.service;

import ru.skypro.flea.dto.CommentDto;
import ru.skypro.flea.dto.CommentsDto;
import ru.skypro.flea.dto.CreateOrUpdateCommentDto;

public interface CommentService {

    CommentsDto getComments(int id);

    CommentDto addComment(int id,
                          CreateOrUpdateCommentDto comment);

    void deleteComment(int adId,
                       int commentId);

    CommentDto updateComment(int adId,
                             int commentId,
                             CreateOrUpdateCommentDto dto);
}
