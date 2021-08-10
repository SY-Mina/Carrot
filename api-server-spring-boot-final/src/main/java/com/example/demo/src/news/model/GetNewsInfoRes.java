package com.example.demo.src.news.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetNewsInfoRes {
    private String content;
    private String typeName;
    private int qurious;
    private int good;
    private int comment;
    private int interest;
    private int views;
    private String time;

}
