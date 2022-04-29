package com.cordova.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "menus")
public class Menu {

    @Id
    private String id;

    private String icon;

    private String name;

    private String url;

    private List<String> roles;
}
