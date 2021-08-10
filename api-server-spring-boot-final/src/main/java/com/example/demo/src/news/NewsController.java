package com.example.demo.src.news;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.news.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/news")
public class NewsController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final NewsProvider newsProvider;
    @Autowired
    private final NewsService newsService;
    @Autowired
    private final JwtService jwtService;


    public NewsController(NewsProvider newsProvider, NewsService newsService, JwtService jwtService){
        this.newsProvider = newsProvider;
        this.newsService = newsService;
        this.jwtService = jwtService;
    }



    /**
     * 동네생활 조회 API
     * [GET] /news
     * @return BaseResponse<List<GetNewsRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/news
    public BaseResponse<List<GetNewsRes>> getNews() {
        try {
            // Get Users
            List<GetNewsRes> getNewsRes = newsProvider.getNews();
            return new BaseResponse<>(getNewsRes);
        }
        catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네생활 클릭시 API
     * [GET] /news/:newsIdx
     * @return BaseResponse<GetNewsRes>
     */
    //Path-variable
    @ResponseBody
    @GetMapping("/{newsIdx}") // (GET) 127.0.0.1:9000/app/items/:itemIdx
    public BaseResponse<GetNewsDetailRes> getNewsDetail(@PathVariable("newsIdx") int newsIdx) {
        try {
            GetNewsDetailRes getNewsDetailRes = newsProvider.getNewsDetail(newsIdx);
            return new BaseResponse<>(getNewsDetailRes);
        }
        catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네생활 올리기 API
     * [POST] /news
     * @return BaseResponse<String>
     */
    //Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> createNews(@RequestBody PostNewsReq postNewsReq) throws BaseException {

        if (postNewsReq.getContent().length() < 5) {
            return new BaseResponse<>(POST_ITEMS_DESCRIPTION_INVALID);
        }
        try{
            if (jwtService.getJwt()==null) {
                return new BaseResponse<>(EMPTY_JWT);
            }
            else {
                int userIdx = jwtService.getUserId();
                newsService.postNews(postNewsReq, userIdx);
                String result ="Success";
                return new BaseResponse<>(result);

            }

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네생활 수정 API
     * [PATCH] /news/:newsIdx
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PatchMapping("/{newsIdx}") // (GET) 127.0.0.1:9000/app/items/:itemIdx
    public BaseResponse<String> patchNews (@PathVariable("newsIdx") int newsIdx, @RequestBody PatchNewsReq patchNewsReq) throws BaseException {
        if (patchNewsReq.getContent().length() < 5) {
            return new BaseResponse<>(POST_ITEMS_DESCRIPTION_INVALID);
        }
        try{
            if (newsProvider.checkNewsExist(newsIdx) == 0) {
                return new BaseResponse<>(GET_NEWS_EMPTY);//
            }

            if (jwtService.getJwt()==null) {
                return new BaseResponse<>(EMPTY_JWT);
            }
            else {
                int userIdx = jwtService.getUserId();

                if (newsProvider.checkStatus(newsIdx, userIdx)==0) {
                    return new BaseResponse<>(PATCH_USER_INVALID);
                }

                newsService.patchNews(patchNewsReq, newsIdx);

                String result ="Success";
                return new BaseResponse<>(result);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네생활 삭제 API
     * [PATCH] /news/:newsIdx/status
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PatchMapping("/{newsIdx}/status") // (GET) 127.0.0.1:9000/app/news/:newsIdx/status
    public BaseResponse<String> patchNewsStatus (@PathVariable("newsIdx") int newsIdx) throws BaseException {
        try{
            if (newsProvider.checkNewsExist(newsIdx) == 0) {
                return new BaseResponse<>(GET_ITEM_EMPTY);
            }

            if (jwtService.getJwt()==null) {
                return new BaseResponse<>(EMPTY_JWT);
            }
            else {
                int userIdx = jwtService.getUserId();

                if (newsProvider.checkStatus(newsIdx, userIdx)==0) {
                    return new BaseResponse<>(PATCH_USER_INVALID_STATUS);
                }
                newsService.patchNewsStatus(newsIdx, userIdx);

                String result ="Success";
                return new BaseResponse<>(result);
            }

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
