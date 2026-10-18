package ru.itis.karimov.entity;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserAnswerEntity {

    private Long userId;
    private Integer questionId;
    private Integer value;
    private Integer importance;
    private boolean isDealBreaker;

}
