package com.example.demo.src.items;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.items.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ItemService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ItemDao itemDao;
    private final ItemProvider itemProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public ItemService(ItemDao itemDao, ItemProvider itemProvider, JwtService jwtService) {
        this.itemDao = itemDao;
        this.itemProvider = itemProvider;
        this.jwtService = jwtService;

    }

    //POST
    public void postItem(PostBoardReq postBoardReq, int userIdx) throws BaseException {
        try{
            itemDao.postItem(postBoardReq, userIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    //POST
    public void postHeart(int userIdx, int itemIdx) throws BaseException {

        try{
            itemDao.postHeart(userIdx, itemIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public void patchHeart(String status, int userIdx, int itemIdx) throws BaseException {

        try{
            itemDao.patchHeart(status, userIdx, itemIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    //POST
    public void postComment(PostCommentReq postCommentReq,int userIdx) throws BaseException {

        try{
            int result = itemDao.postComment(postCommentReq, userIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    //PATCH
    public void patchItem(PatchItemReq patchItemReq, int itemIdx) throws BaseException {

        try{
            int result = itemDao.patchItem(patchItemReq, itemIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    //PATCH
    public void patchItemStatus(int itemIdx, int userIdx) throws BaseException {

        try{
            int result = itemDao.patchItemStatus(itemIdx, userIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
