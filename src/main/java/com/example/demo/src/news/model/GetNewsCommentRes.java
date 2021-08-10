package com.example.demo.src.news.model;


import com.example.demo.src.user.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetNewsCommentRes {
    private int userIdx;
    private String userName;
    private String location;
    private String time;
    private String content;

}
