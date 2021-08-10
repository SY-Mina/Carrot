package com.example.demo.src.news.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetNewsRes {
    private int newIdx;
    private String userName;
    private String location;
    private String type;
    private String content;
    private String time;
    private String comment;
    private int qurious;
    private int good;
}
