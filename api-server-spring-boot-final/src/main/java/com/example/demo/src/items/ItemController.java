package com.example.demo.src.items;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.items.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/app/items")
public class ItemController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ItemProvider itemProvider;
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final JwtService jwtService;




    public ItemController(ItemProvider itemProvider, ItemService itemService, JwtService jwtService){
        this.itemProvider = itemProvider;
        this.itemService = itemService;
        this.jwtService = jwtService;
    }



    /**
     * 중고물품 조회 API
     * [GET] /items
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users?userIdx= && Email=
     * @return BaseResponse<List<GetItemsRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/items
    public BaseResponse<List<GetItemsRes>> getItems() {
        try {
            // Get Items
            List<GetItemsRes> getItemsRes = itemProvider.getItems();
            return new BaseResponse<>(getItemsRes);
        }
        catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 물품 1개 조회 API
     * [GET] /items/:itemIdx
     * @return BaseResponse<GetItemsRes>
     */
    //Path-variable
    @ResponseBody
    @GetMapping("/{itemIdx}") // (GET) 127.0.0.1:9000/app/items/:itemIdx
    public BaseResponse<GetItemDetailRes> getItemDetail(@PathVariable("itemIdx") int itemIdx) {
        if (itemProvider.checkItemExist(itemIdx) == 0) {
            return new BaseResponse<>(GET_ITEM_EMPTY);
        }
        try {
            GetItemDetailRes getItemDetailRes = itemProvider.getItemDetail(itemIdx);
            return new BaseResponse<>(getItemDetailRes);
        }
        catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 판매자의 다른 상품들 조회 API
     * [GET] /items/:userIdx/recommend
     * @return BaseResponse<GetRecommendRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}/recommend") // (GET) 127.0.0.1:9000/app/items/:userIdx/recommend
    public BaseResponse<List<GetRecommendRes>> getItem(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try {
            List<GetRecommendRes> getRecommendRes = itemProvider.getItem(userIdx);
            return new BaseResponse<>(getRecommendRes);
        }
        catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 판매할 물건 올리기 API
     * [POST] /items
     * @return BaseResponse<PostItemRes>
     */
    //Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> createItem(@RequestBody PostBoardReq postBoardReq) throws BaseException {

        if (postBoardReq.getDescription().length() < 5) {
            return new BaseResponse<>(POST_ITEMS_DESCRIPTION_INVALID);
        }
        try{
            if (jwtService.getJwt()==null) {
                return new BaseResponse<>(EMPTY_JWT);
            }
            else {
                int userIdx = jwtService.getUserId();
                itemService.postItem(postBoardReq, userIdx);
                String result ="Success";
                return new BaseResponse<>(result);
            }

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 좋아요 API
     * [POST] /items/heart
     * @return BaseResponse<GetRecommendRes>
     */
    // Path-variable
    @ResponseBody
    @PostMapping("/heart") // (GET) 127.0.0.1:9000/app/items/heart
    public BaseResponse<String> postHeart(@RequestBody PostHeartReq postHeartReq) throws BaseException {
        try{
            if (itemProvider.checkItemExist(postHeartReq.getItemIdx()) == 0) {
                return new BaseResponse<>(GET_ITEM_EMPTY);
            }
            if (jwtService.getJwt()==null) {
                return new BaseResponse<>(EMPTY_JWT);
            }
            else {
                int userIdx = jwtService.getUserId();

                // 하트 새로 만듬.
                if (itemProvider.checkHeart(userIdx, postHeartReq.getItemIdx())==0) {
                    itemService.postHeart(userIdx, postHeartReq.getItemIdx());
                    String result ="T";
                    return new BaseResponse<>(result);
                }
                else {
                    // 하트 T->F
                    if (itemProvider.checkStatusHeart(userIdx, postHeartReq.getItemIdx()).equals("T")) {
                        itemService.patchHeart("F", userIdx, postHeartReq.getItemIdx());

                        String result ="F";
                        return new BaseResponse<>(result);
                    }
                    // 하트 F->T
                    else {
                        itemService.patchHeart("T", userIdx, postHeartReq.getItemIdx());

                        String result ="T";
                        return new BaseResponse<>(result);
                    }

                }

            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 댓글 달기 API
     * [POST] /items/comment
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PostMapping("/comment") // (GET) 127.0.0.1:9000/app/items/comment
    public BaseResponse<String> postHeart(@RequestBody PostCommentReq postCommentReq) throws BaseException {
        try{
            if (itemProvider.checkItemExist(postCommentReq.getItemIdx()) == 0) {
                return new BaseResponse<>(GET_ITEM_EMPTY);
            }
            if (jwtService.getJwt()==null) {
                return new BaseResponse<>(EMPTY_JWT);
            }
            else {
                int userIdx = jwtService.getUserId();
                itemService.postComment(postCommentReq, userIdx);

                String result ="Success";
                return new BaseResponse<>(result);
            }

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 수정 API
     * [PATCH] /items/:itemIdx
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PatchMapping("/{itemIdx}") // (GET) 127.0.0.1:9000/app/items/:itemIdx
    public BaseResponse<String> patchItem(@PathVariable("itemIdx") int itemIdx, @RequestBody PatchItemReq patchItemReq) throws BaseException {
        if (patchItemReq.getDescription().length() < 5) {
            return new BaseResponse<>(POST_ITEMS_DESCRIPTION_INVALID);
        }
        try{
            if (itemProvider.checkItemExist(itemIdx) == 0) {
                return new BaseResponse<>(GET_ITEM_EMPTY);
            }

            if (jwtService.getJwt()==null) {
                return new BaseResponse<>(EMPTY_JWT);
            }
            else {
                int userIdx = jwtService.getUserId();

                if (itemProvider.checkStatus(itemIdx, userIdx)==0) {
                    return new BaseResponse<>(PATCH_USER_INVALID);
                }

                itemService.patchItem(patchItemReq, itemIdx);

                String result ="Success";
                return new BaseResponse<>(result);
            }
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 삭제 API
     * [PATCH] /items/:itemIdx/status
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PatchMapping("/{itemIdx}/status") // (GET) 127.0.0.1:9000/app/items/:itemIdx/status
    public BaseResponse<String> patchItemStatus (@PathVariable("itemIdx") int itemIdx) throws BaseException {
        try{
            if (itemProvider.checkItemExist(itemIdx) == 0) {
                return new BaseResponse<>(GET_ITEM_EMPTY);
            }

            if (jwtService.getJwt()==null) {
                return new BaseResponse<>(EMPTY_JWT);
            }
            else {
                int userIdx = jwtService.getUserId();

                if (itemProvider.checkStatus(itemIdx, userIdx)==0) {
                    return new BaseResponse<>(PATCH_USER_INVALID_STATUS);
                }
                itemService.patchItemStatus(itemIdx, userIdx);

                String result ="Success";
                return new BaseResponse<>(result);
            }

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
