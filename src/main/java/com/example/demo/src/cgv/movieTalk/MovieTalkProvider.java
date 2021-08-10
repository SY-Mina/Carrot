package com.example.demo.src.movieTalk;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.movieTalk.model.*;
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
public class MovieTalkProvider {

    private final MovieTalkDao movieTalkDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public MovieTalkProvider(MovieTalkDao movieTalkDao, JwtService jwtService) {
        this.movieTalkDao = movieTalkDao;
        this.jwtService = jwtService;
    }

    public List<GetMovieTalkRes> getMovieTalks( ) throws BaseException {
        try {
            List<GetMovieTalkRes> getMovieTalkRes = movieTalkDao.getMovieTalks();
            return getMovieTalkRes;
        }
        catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

}
