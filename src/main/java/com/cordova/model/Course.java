package com.cordova.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "courses")
public class Course {

    @Id
    private String id;
    private String name;
    private String acronym;
    private Boolean status;
}
