package com.example.demo.src.items.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCommentReq {
    private int itemIdx;
    private String content;
}
