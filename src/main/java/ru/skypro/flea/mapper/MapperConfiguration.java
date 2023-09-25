package ru.skypro.flea.mapper;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public AdMapper getAdMapper() {
        return Mappers.getMapper(AdMapper.class);
    }

    @Bean
    public CommentMapper getCommentMapper() {
        return Mappers.getMapper(CommentMapper.class);
    }

}
