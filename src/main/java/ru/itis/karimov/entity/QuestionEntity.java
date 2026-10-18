package ru.itis.karimov.entity;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class QuestionEntity {

    private Integer id;
    private String text;
    private boolean isActive;
    private LocalDateTime createdAt;

}
