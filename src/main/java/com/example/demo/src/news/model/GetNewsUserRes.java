package com.example.demo.src.news.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetNewsUserRes {
    private int userIdx;
    private String userName;
    private String profImg;
    private String location;

}
