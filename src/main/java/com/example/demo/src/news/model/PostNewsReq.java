package com.example.demo.src.news.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostNewsReq {
    private String content;
    private int type;
}
