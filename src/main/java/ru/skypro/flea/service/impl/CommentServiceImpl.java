package ru.skypro.flea.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.flea.dto.CommentDto;
import ru.skypro.flea.dto.CommentsDto;
import ru.skypro.flea.dto.CreateOrUpdateCommentDto;
import ru.skypro.flea.exception.ActionForbiddenException;
import ru.skypro.flea.exception.ResourceWithSpecifiedIdNotFoundException;
import ru.skypro.flea.mapper.CommentMapper;
import ru.skypro.flea.model.Ad;
import ru.skypro.flea.model.Comment;
import ru.skypro.flea.model.User;
import ru.skypro.flea.repository.CommentRepository;
import ru.skypro.flea.service.AdService;
import ru.skypro.flea.service.CommentService;
import ru.skypro.flea.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;

    private final AdService adService;

    private final UserService userService;

    @Override
    public CommentsDto getComments(int id) {
        Ad ad = adService.getAdById(id);
        List<Comment> comments = commentRepository.findAllByAdOrderByIdDesc(ad);

        return commentMapper.toCommentsDto(comments);
    }

    @Override
    public CommentDto addComment(int id,
                                 CreateOrUpdateCommentDto dto,
                                 Authentication authentication) {
        Ad ad = adService.getAdById(id);
        User user = userService.getUserByEmail(authentication.getName());
        Comment comment = commentMapper.createCommentFromDto(dto);
        comment.setAd(ad);
        comment.setUser(user);
        commentRepository.save(comment);

        return commentMapper.toCommentDto(comment);
    }

    @Override
    public void deleteComment(int adId,
                              int commentId,
                              Authentication authentication) {
        Ad ad = adService.getAdById(adId);
        Comment comment = getCommentByIdAndAd(commentId, ad);
        checkIfUserHasAccessToManageTheComment(comment, authentication, "delete");
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDto updateComment(int adId,
                                    int commentId,
                                    CreateOrUpdateCommentDto dto,
                                    Authentication authentication) {
        Ad ad = adService.getAdById(adId);
        Comment comment = getCommentByIdAndAd(commentId, ad);
        checkIfUserHasAccessToManageTheComment(comment, authentication, "update");
        commentMapper.updateCommentFromDto(comment, dto);
        commentRepository.save(comment);

        return commentMapper.toCommentDto(comment);
    }

    private Comment getCommentByIdAndAd(int id, Ad ad) {
        Optional<Comment> commentOptional = commentRepository.findByIdAndAd(id, ad);
        if (commentOptional.isEmpty()) {
            String message = String.format("Comment with specified id (%d) related to the ad (id = %d) is not found",
                    id, ad.getId());
            log.info(message);
            throw new ResourceWithSpecifiedIdNotFoundException(message);
        }

        return commentOptional.get();
    }

    private void checkIfUserHasAccessToManageTheComment(Comment comment,
                                                        Authentication authentication,
                                                        String action) {
        if (!comment.getUser().getUsername().equals(authentication.getName())
                && !userService.isAuthorizedUserAdmin(authentication)) {
            String message = String.format("User %s is not allowed to %s the comment (%d)",
                    authentication.getName(), action, comment.getId());
            log.info(message);
            throw new ActionForbiddenException(message);
        }
    }

}
