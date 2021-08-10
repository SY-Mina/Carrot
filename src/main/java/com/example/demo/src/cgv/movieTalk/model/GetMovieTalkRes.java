package com.example.demo.src.movieTalk.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMovieTalkRes {
    private String title;
    private String time;
    private String postThumbnail;
    private String postContent;
    private int heart;
    private int comment;

}
