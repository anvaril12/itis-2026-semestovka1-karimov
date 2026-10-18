package ru.itis.karimov.entity;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DormitoryEntity {

    private Integer id;
    private Integer universityId;
    private String name;
    private String address;

}
