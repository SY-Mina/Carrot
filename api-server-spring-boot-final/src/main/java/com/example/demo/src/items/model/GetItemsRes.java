package com.example.demo.src.items.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetItemsRes {
    private int idx;
    private String itemName;
    private String image;
    private String price;
    private int heart;
    private int comment;
    private String time;
    private String location;

}
