package ru.itis.karimov.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerOptionEntity {

    private Integer id;
    private Integer questionId;
    private Integer value;
    private String label;

}
