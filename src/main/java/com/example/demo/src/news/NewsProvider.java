package com.example.demo.src.news;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.news.model.*;
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
public class NewsProvider {

    private final NewsDao newsDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public NewsProvider(NewsDao newsDao, JwtService jwtService) {
        this.newsDao = newsDao;
        this.jwtService = jwtService;
    }

    public List<GetNewsRes> getNews( ) throws BaseException {

        try {
            List<GetNewsRes> getNewsRes = newsDao.getNews();
            return getNewsRes;
        }
        catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    public GetNewsDetailRes getNewsDetail(int newsIdx) throws BaseException{

        int exist = newsDao.checkItemExist(newsIdx);

        if (exist == 0) {
            throw new BaseException(BaseResponseStatus.GET_ITEMS_EMPTY);
        }

        try {
            GetNewsDetailRes getNewsDetailRes = new GetNewsDetailRes();

            List<GetImageRes> getImageRes = newsDao.getImages(newsIdx);
            GetNewsUserRes getItemUserRes = newsDao.getNewsUser(newsIdx);
            GetNewsInfoRes getItemInfoRes = newsDao.getNewsInfo(newsIdx);
            List<GetNewsCommentRes> getNewsCommentRes = newsDao.getNewsComment(newsIdx);

            getNewsDetailRes.setUser(getItemUserRes);
            getNewsDetailRes.setNews(getItemInfoRes);
            getNewsDetailRes.setImages(getImageRes);
            getNewsDetailRes.setComments(getNewsCommentRes);

            return getNewsDetailRes;
        }
        catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    public int checkNewsExist(int newsIdx){return newsDao.checkNewsExist(newsIdx);}

    public int checkStatus (int newsIdx, int userIdx) {
        return newsDao.checkStatus(newsIdx, userIdx);
    }
}
