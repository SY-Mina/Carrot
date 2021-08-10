package com.example.demo.src.hotdeal;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.hotdeal.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/hotdeal")
public class HotdealController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final HotdealProvider hotdealProvider;
    @Autowired
    private final HotdealService hotdealService;
    @Autowired
    private final JwtService jwtService;




    public HotdealController(HotdealProvider hotdealProvider, HotdealService hotdealService, JwtService jwtService){
        this.hotdealProvider = hotdealProvider;
        this.hotdealService = hotdealService;
        this.jwtService = jwtService;
    }



    /**
     * 음식 핫딜 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /movies
     * @return BaseResponse<List<GetMovieRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("/food") // (GET) 127.0.0.1:9000/app/movies
    public BaseResponse<List<GetHotdealFoodRes>> getFoods() {
        try {
            // Get Users
            List<GetHotdealFoodRes> getFoods = hotdealProvider.getFoods();
            return new BaseResponse<>(getFoods);
        }
        catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
