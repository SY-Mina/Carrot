package com.example.demo.src.hotdeal.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetHotdealFoodRes {
    private String name;
    private String dDay;
    private String img;
    private String price;
    private String discount;
    private String leftNum;
}
