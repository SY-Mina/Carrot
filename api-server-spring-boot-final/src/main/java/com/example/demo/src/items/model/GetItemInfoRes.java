package com.example.demo.src.items.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetItemInfoRes {
    private int idx;
    private String itemName;
    private String price;
    private String typeName;
    private String description;
    private int heart;
    private int interest;
    private int views;
    private String time;

}
