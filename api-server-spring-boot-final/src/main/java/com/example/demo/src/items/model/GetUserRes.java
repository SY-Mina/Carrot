package com.example.demo.src.items.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private String userName;
    private String profImg;
    private String location;
    private String manner;
}
