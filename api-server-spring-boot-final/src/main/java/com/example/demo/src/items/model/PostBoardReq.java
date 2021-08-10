package com.example.demo.src.items.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostBoardReq {
    private String itemName;
    private int price;
    private String description;
    private int typeIdx;
}
