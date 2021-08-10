package com.example.demo.src.movie;

import com.example.demo.src.user.model.GetUserRes;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.movie.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/movies")
public class MovieController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MovieProvider movieProvider;
    @Autowired
    private final MovieService movieService;
    @Autowired
    private final JwtService jwtService;




    public MovieController(MovieProvider movieProvider, MovieService movieService, JwtService jwtService){
        this.movieProvider = movieProvider;
        this.movieService = movieService;
        this.jwtService = jwtService;
    }



    /**
     * 영화 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /movies
     * @return BaseResponse<List<GetMovieRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/movies
    public BaseResponse<List<GetMovieRes>> getMovies() {
        try {
            // Get Users
            List<GetMovieRes> getMovieRes = movieProvider.getMovies();
            return new BaseResponse<>(getMovieRes);
        }
        catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 영화 1개 조회 API
     * [GET] /movies/:movieId
     * @return BaseResponse<GetMoiveDetailRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{movieId}") // (GET) 127.0.0.1:9000/app/movies/:movieId
    public BaseResponse<GetMovieDetailRes> getMovie(@PathVariable("movieId") int movieId) {
        // Get Users
        GetMovieDetailRes getMovieDetailRes = movieProvider.getMovie(movieId);
        return new BaseResponse<>(getMovieDetailRes);
    }


}
