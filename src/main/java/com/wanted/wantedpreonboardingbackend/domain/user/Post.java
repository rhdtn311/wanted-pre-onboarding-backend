package com.wanted.wantedpreonboardingbackend.domain.user;

import com.wanted.wantedpreonboardingbackend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
}
