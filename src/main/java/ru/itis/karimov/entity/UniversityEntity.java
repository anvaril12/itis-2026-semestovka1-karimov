package ru.itis.karimov.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniversityEntity {

    private Integer id;
    private String name;
    private String city;

}
