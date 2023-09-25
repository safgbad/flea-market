package ru.skypro.flea.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.flea.dto.CommentDto;
import ru.skypro.flea.dto.CommentsDto;
import ru.skypro.flea.dto.CreateOrUpdateCommentDto;
import ru.skypro.flea.exception.ResourceWithSpecifiedIdNotFoundException;
import ru.skypro.flea.mapper.CommentMapper;
import ru.skypro.flea.model.Ad;
import ru.skypro.flea.model.Comment;
import ru.skypro.flea.repository.AdRepository;
import ru.skypro.flea.repository.CommentRepository;
import ru.skypro.flea.service.CommentService;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;

    private final AdRepository adRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              AdRepository adRepository,
                              CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentsDto getComments(int id) {
        Optional<Ad> adOptional = adRepository.findById(id);
        if (adOptional.isEmpty()) {
            throw new ResourceWithSpecifiedIdNotFoundException(
                    String.format("Ad with specified id (%d) is not found", id));
        }
        Ad ad = adOptional.get();
        List<Comment> comments = commentRepository.findAllByAd(ad);

        return commentMapper.toCommentsDto(comments);
    }

    @Override
    public CommentDto addComment(int id,
                                 CreateOrUpdateCommentDto dto) {
        Optional<Ad> adOptional = adRepository.findById(id);
        if (adOptional.isEmpty()) {
            throw new ResourceWithSpecifiedIdNotFoundException(
                    String.format("Ad with specified id (%d) is not found", id));
        }
        Ad ad = adOptional.get();
        Comment comment = commentMapper.createCommentFromDto(dto);
        comment.setAd(ad);
        commentRepository.save(comment);

        return commentMapper.toCommentDto(comment);
    }

    @Override
    public void deleteComment(int adId,
                              int commentId) {
        Optional<Ad> adOptional = adRepository.findById(adId);
        if (adOptional.isEmpty()) {
            throw new ResourceWithSpecifiedIdNotFoundException(
                    String.format("Ad with specified id (%d) is not found", adId));
        }
        Ad ad = adOptional.get();
        if (!commentRepository.existsByIdAndAd(commentId, ad)) {
            throw new ResourceWithSpecifiedIdNotFoundException(
                    String.format("Comment with specified id (%d) related to the ad (id = %d) is not found",
                            commentId, adId));
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDto updateComment(int adId,
                                    int commentId,
                                    CreateOrUpdateCommentDto dto) {
        Optional<Ad> adOptional = adRepository.findById(adId);
        if (adOptional.isEmpty()) {
            throw new ResourceWithSpecifiedIdNotFoundException(
                    String.format("Ad with specified id (%d) is not found", adId));
        }
        Ad ad = adOptional.get();
        Optional<Comment> commentOptional = commentRepository.findByIdAndAd(commentId, ad);
        if (commentOptional.isEmpty()) {
            throw new ResourceWithSpecifiedIdNotFoundException(
                    String.format("Comment with specified id (%d) related to the ad (id = %d) is not found",
                            commentId, adId));
        }
        Comment comment = commentOptional.get();
        commentMapper.updateCommentFromDto(comment, dto);
        commentRepository.save(comment);

        return commentMapper.toCommentDto(comment);
    }

}
