package com.example.demo.src.items;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.items.model.*;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ItemProvider {

    private final ItemDao itemDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public ItemProvider(ItemDao itemDao, JwtService jwtService) {
        this.itemDao = itemDao;
        this.jwtService = jwtService;
    }

    public List<GetItemsRes> getItems( ) throws BaseException {

        int exist = itemDao.checkItem();

        if (exist == 0) {
            throw new BaseException(BaseResponseStatus.GET_ITEMS_EMPTY);
        }
        try {


            List<GetItemsRes> getItemsRes = itemDao.getItems();
            return getItemsRes;

        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    public GetItemDetailRes getItemDetail(int itemIdx) throws BaseException{

        int exists = itemDao.checkItemExist(itemIdx);

        if (exists == 0) {
            throw new BaseException(BaseResponseStatus.GET_ITEM_EMPTY);
        }
        else {
            try {
                GetItemDetailRes getItemDetailRes = new GetItemDetailRes();

                List<GetImageRes> getImageRes = itemDao.getImages(itemIdx);
                GetItemUserRes getItemUserRes = itemDao.getItemUser(itemIdx);
                GetItemInfoRes getItemInfoRes = itemDao.getItemInfo(itemIdx);

                getItemDetailRes.setUser(getItemUserRes);
                getItemDetailRes.setItem(getItemInfoRes);
                getItemDetailRes.setImages(getImageRes);

                return getItemDetailRes;
            }
            catch (Exception exception) {
                throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
            }
        }

    }

    public List<GetRecommendRes> getItem(int userIdx) throws BaseException{
//        int exists = itemDao.checkItemRecommendExist(userIdx);
//
//        if (exists == 0) {
//            throw new BaseException(BaseResponseStatus.GET_RECOMMEND_EMPTY);
//        }

        try {
            List<GetRecommendRes> getRecommendRes = itemDao.getItem(userIdx);
            return getRecommendRes;
        }
        catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


    public int checkEmail(String email){
        return itemDao.checkEmail(email);
    }

    public int checkHeart(int userIdx, int itemIdx) {
        return itemDao.checkHeart(userIdx, itemIdx);}


    public String checkStatusHeart(int userIdx, int itemIdx) {
        return itemDao.checkStatusHeart(userIdx, itemIdx);}

    public int checkItemExist(int itemIdx){return itemDao.checkItemExist(itemIdx);}

    public int checkStatus (int itemIdx, int userIdx) {
        return itemDao.checkStatus(itemIdx, userIdx);
    }

}
