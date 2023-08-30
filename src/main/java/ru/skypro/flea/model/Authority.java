package ru.skypro.flea.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "authorities")
@NoArgsConstructor
@Getter
@Setter
public class Authority implements GrantedAuthority {

    @Id
    private String username;

    @Id
    private String authority;

}
