package com.example.demo.src.items.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRecommendRes {
    private int idx;
    private String userName;
    private String itemName;
    private String image;
    private String price;
}
