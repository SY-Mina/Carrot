package com.example.demo.src.movieTalk;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.movieTalk.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/movieTalk")
public class MovieTalkController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MovieTalkProvider movieTalkProvider;
    @Autowired
    private final MovieTalkService movieTalkService;
    @Autowired
    private final JwtService jwtService;


    public MovieTalkController(MovieTalkProvider movieTalkProvider, MovieTalkService movieTalkService, JwtService jwtService){
        this.movieTalkProvider = movieTalkProvider;
        this.movieTalkService = movieTalkService;
        this.jwtService = jwtService;
    }



    /**
     * 무비톡 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /movies
     * @return BaseResponse<List<GetMovieTalkRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/movies
    public BaseResponse<List<GetMovieTalkRes>> getMovieTalks() {
        try {
            // Get Users
            List<GetMovieTalkRes> getMovieTalkRes = movieTalkProvider.getMovieTalks();
            return new BaseResponse<>(getMovieTalkRes);
        }
        catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
