package com.example.demo.src.items.model;


import com.example.demo.src.user.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetItemDetailRes {
    private GetItemUserRes user;
    private GetItemInfoRes item;
    private List<GetImageRes> images = new ArrayList<>();
    public GetItemDetailRes() {}
}
