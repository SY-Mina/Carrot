package com.example.demo.src.movie.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMovieDetailRes {
    private String movieName;
    private String movieAge;
    private String moviePosterImg;
    private String movieRating;
    private String reserveRate;

}
