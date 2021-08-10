package com.example.demo.src.news.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchNewsReq {
    private String content;
    private int type;
}
