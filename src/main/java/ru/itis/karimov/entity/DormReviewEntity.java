package ru.itis.karimov.entity;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DormReviewEntity {

    private Long id;
    private Long userId;
    private Integer dormitoryId;
    private String text;
    private Integer rating;
    private LocalDateTime createdAt;

}
