package com.example.demo.src.movie;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.movie.model.*;
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
public class MovieService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MovieDao movieDao;
    private final MovieProvider movieProvider;
    private final JwtService jwtService;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public MovieService(MovieDao movieDao, MovieProvider movieProvider, JwtService jwtService) {
        this.movieDao = movieDao;
        this.movieProvider = movieProvider;
        this.jwtService = jwtService;

    }
}
