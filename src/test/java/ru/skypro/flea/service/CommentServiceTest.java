package ru.skypro.flea.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.flea.dto.CommentDto;
import ru.skypro.flea.dto.CommentsDto;
import ru.skypro.flea.dto.CreateOrUpdateCommentDto;
import ru.skypro.flea.exception.ResourceWithSpecifiedIdNotFoundException;
import ru.skypro.flea.mapper.CommentMapper;
import ru.skypro.flea.model.Ad;
import ru.skypro.flea.model.Comment;
import ru.skypro.flea.repository.AdRepository;
import ru.skypro.flea.repository.CommentRepository;
import ru.skypro.flea.service.impl.CommentServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentMapper mapperMock;

    @Mock
    private CommentRepository commentRepositoryMock;

    @Mock
    private AdRepository adRepositoryMock;

    @InjectMocks
    private CommentServiceImpl out;

    @Test
    public void getCommentsShouldThrowExceptionWhenAdWithSpecifiedIdNotFoundTest() {
        when(adRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceWithSpecifiedIdNotFoundException.class, () -> out.getComments(1));
    }

    @Test
    public void getCommentsShouldReturnCorrectDtoWhenAdWithSpecifiedIdFoundTest() {
        int adId = 1;
        Ad ad = new Ad();
        ad.setId(adId);

        int commentId = 2;
        Comment comment = new Comment();
        comment.setId(commentId);
        List<Comment> commentList = Collections.singletonList(comment);

        CommentDto commentDto = new CommentDto();
        commentDto.setPk(comment.getId());
        List<CommentDto> commentDtoList = Collections.singletonList(commentDto);

        CommentsDto commentsDto = new CommentsDto();
        commentsDto.setResults(commentDtoList);
        commentsDto.setCount(commentsDto.getResults().size());

        when(adRepositoryMock.findById(adId)).thenReturn(Optional.of(ad));
        when(commentRepositoryMock.findAllByAd(ad)).thenReturn(commentList);
        when(mapperMock.toCommentsDto(commentList)).thenReturn(commentsDto);

        CommentsDto result = out.getComments(adId);

        assertEquals(result, commentsDto);
    }

    @Test
    public void addCommentShouldThrowExceptionWhenAdWithSpecifiedIdNotFoundTest() {
        when(adRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceWithSpecifiedIdNotFoundException.class, () -> out.addComment(1, null));
    }

    @Test
    public void addCommentShouldReturnCorrectDtoWhenAdWithSpecifiedIdFoundTest() {
        int adId = 1;
        Ad ad = new Ad();
        ad.setId(adId);

        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto();
        createOrUpdateCommentDto.setText("smth.");

        Comment comment = new Comment();
        comment.setText(createOrUpdateCommentDto.getText());

        CommentDto commentDto = new CommentDto();
        commentDto.setText(createOrUpdateCommentDto.getText());

        when(adRepositoryMock.findById(adId)).thenReturn(Optional.of(ad));
        when(mapperMock.createCommentFromDto(createOrUpdateCommentDto)).thenReturn(comment);
        when(mapperMock.toCommentDto(comment)).thenReturn(commentDto);

        CommentDto result = out.addComment(adId, createOrUpdateCommentDto);

        assertEquals(result, commentDto);
        assertEquals(ad, comment.getAd());

        verify(commentRepositoryMock).save(comment);
    }

    @Test
    public void deleteCommentShouldThrowExceptionWhenAdWithSpecifiedIdNotFoundTest() {
        when(adRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceWithSpecifiedIdNotFoundException.class, () -> out.deleteComment(1, 2));
    }

    @Test
    public void deleteCommentShouldThrowExceptionWhenCommentWithSpecifiedIdRelatedToAdNotFoundTest() {
        int adId = 1;
        Ad ad = new Ad();
        ad.setId(adId);

        int commentId = 2;

        when(adRepositoryMock.findById(any())).thenReturn(Optional.of(ad));
        when(commentRepositoryMock.existsByIdAndAd(commentId, ad)).thenReturn(false);

        assertThrows(ResourceWithSpecifiedIdNotFoundException.class, () -> out.deleteComment(adId, commentId));
    }

    @Test
    public void deleteCommentShouldCallDeleteByIdRepositoryMethodWhenAdIdAndCommentIdAreCorrectTest() {
        int adId = 1;
        Ad ad = new Ad();
        ad.setId(adId);

        int commentId = 2;

        when(adRepositoryMock.findById(any())).thenReturn(Optional.of(ad));
        when(commentRepositoryMock.existsByIdAndAd(commentId, ad)).thenReturn(true);

        out.deleteComment(adId, commentId);

        verify(commentRepositoryMock).deleteById(commentId);
    }

    @Test
    public void updateCommentShouldThrowExceptionWheAdWithSpecifiedIdNotFoundTest() {
        when(adRepositoryMock.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceWithSpecifiedIdNotFoundException.class, () -> out.updateComment(1, 2, null));
    }

    @Test
    public void updateCommentShouldThrowExceptionWhenCommentWithSpecifiedIdRelatedToAdNotFoundTest() {
        int adId = 1;
        Ad ad = new Ad();
        ad.setId(adId);

        int commentId = 2;

        when(adRepositoryMock.findById(any())).thenReturn(Optional.of(ad));
        when(commentRepositoryMock.findByIdAndAd(commentId, ad)).thenReturn(Optional.empty());

        assertThrows(ResourceWithSpecifiedIdNotFoundException.class, () -> out.updateComment(adId, commentId, null));
    }

    @Test
    public void updateCommentShouldReturnCorrectDtoWhenAdIdAndCommentIdAreCorrectTest() {
        int adId = 1;
        Ad ad = new Ad();
        ad.setId(adId);

        int commentId = 2;
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setText("smth.");

        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto();
        createOrUpdateCommentDto.setText("updated");

        CommentDto commentDto = new CommentDto();
        commentDto.setPk(commentId);
        commentDto.setText(createOrUpdateCommentDto.getText());

        when(adRepositoryMock.findById(any())).thenReturn(Optional.of(ad));
        when(commentRepositoryMock.findByIdAndAd(commentId, ad)).thenReturn(Optional.of(comment));

        doNothing().when(mapperMock).updateCommentFromDto(comment, createOrUpdateCommentDto);
        comment.setText(createOrUpdateCommentDto.getText());

        when(mapperMock.toCommentDto(comment)).thenReturn(commentDto);

        CommentDto result = out.updateComment(adId, commentId, createOrUpdateCommentDto);

        assertEquals(result, commentDto);

        verify(mapperMock).updateCommentFromDto(comment, createOrUpdateCommentDto);
        verify(commentRepositoryMock).save(comment);
    }

}
