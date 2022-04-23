package com.cordova.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "students")
public class Student {

    @Id
    private String id;

    @Size(min = 3)
    private String names;

    @Size(min = 3)
    private String lastNames;

    @NotNull
    @Size(min = 8)
    private String dni;

    private Integer age;
}
