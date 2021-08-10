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
public class GetNewsDetailRes {
    private GetNewsUserRes user;
    private GetNewsInfoRes news;
    private List<GetImageRes> images = new ArrayList<>();
    private List<GetNewsCommentRes> comments = new ArrayList<>();
    public GetNewsDetailRes() {}
}
