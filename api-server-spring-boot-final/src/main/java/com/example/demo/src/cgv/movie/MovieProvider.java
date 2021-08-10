package com.example.demo.src.movie;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.movie.model.*;
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
public class MovieProvider {

    private final MovieDao movieDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public MovieProvider(MovieDao movieDao, JwtService jwtService) {
        this.movieDao = movieDao;
        this.jwtService = jwtService;
    }

    public List<GetMovieRes> getMovies( ) throws BaseException {
        try {
            List<GetMovieRes> getMovieRes = movieDao.getMovies();
            return getMovieRes;
        }
        catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    public GetMovieDetailRes getMovie(int movieId) {
        GetMovieDetailRes getMovieDetailRes = movieDao.getMovie(movieId);
        return getMovieDetailRes;
    }

}
