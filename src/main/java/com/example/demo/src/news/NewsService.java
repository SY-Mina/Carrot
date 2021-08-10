package com.example.demo.src.news;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.news.model.*;
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
public class NewsService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final NewsDao newsDao;
    private final NewsProvider newsProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public NewsService(NewsDao newsDao, NewsProvider newsProvider, JwtService jwtService) {
        this.newsDao = newsDao;
        this.newsProvider = newsProvider;
        this.jwtService = jwtService;

    }

    //PATCH
    public void patchNews(PatchNewsReq patchNewsReq, int newsIdx) throws BaseException {

        try{
            int result = newsDao.patchNews(patchNewsReq, newsIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    //POST
    public void postNews(PostNewsReq postNewsReq, int userIdx) throws BaseException {
        try{
            newsDao.postNews(postNewsReq, userIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    //PATCH
    public void patchNewsStatus(int newsIdx, int userIdx) throws BaseException {

        try{
            int result = newsDao.patchNewsStatus(newsIdx, userIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

}
