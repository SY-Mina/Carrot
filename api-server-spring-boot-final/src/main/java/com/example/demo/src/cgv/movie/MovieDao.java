package com.example.demo.src.movie;


import com.example.demo.src.movie.model.*;
import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MovieDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetMovieRes> getMovies(){
        return this.jdbcTemplate.query("select distinct movieName, (case when movieAge >= 19\n" +
                        "           then '청불'\n" +
                        "           else movieAge\n" +
                        "           end) as movieAge, moviePosterImg,\n" +
                        "            movieRating,\n" +
                        "            (CONCAT('예매율 ', (select  ROUND(avg(AvailableMovie.movieId)*10)\n" +
                        "                from AvailableMovie\n" +
                        "                where AvailableMovie.movieId=Movie.idx), '%'))\n" +
                        "             as reserveRate\n" +
                        "from AvailableMovie inner join Reserved on Reserved.availableId=AvailableMovie.idx\n" +
                        "    left outer join Movie on AvailableMovie.movieId=Movie.idx\n" +
                        "order by reserveRate desc;\n",
                (rs, rowNum) -> new GetMovieRes(
                        rs.getString("movieName"),
                        rs.getString("movieAge"),
                        rs.getString("moviePosterImg"),
                        rs.getString("movieRating"),
                        rs.getString("reserveRate")));
    }

    public GetMovieDetailRes getMovie(int movieId){
        return this.jdbcTemplate.queryForObject("select distinct movieName, (case when movieAge >= 19\n" +
                        "           then '청불'\n" +
                        "           else movieAge\n" +
                        "           end) as movieAge, moviePosterImg,\n" +
                        "            movieRating,\n" +
                        "            (CONCAT('예매율 ', (select  ROUND(avg(AvailableMovie.movieId)*10)\n" +
                        "                from AvailableMovie\n" +
                        "                where AvailableMovie.movieId=Movie.idx), '%'))\n" +
                        "             as reserveRate\n" +
                        "from AvailableMovie inner join Reserved on Reserved.availableId=AvailableMovie.idx\n" +
                        "    left outer join Movie on AvailableMovie.movieId=Movie.idx\n" +
                        "where Movie.idx=?\n" +
                        "order by reserveRate asc;",
                (rs, rowNum) -> new GetMovieDetailRes(
                        rs.getString("movieName"),
                        rs.getString("movieAge"),
                        rs.getString("moviePosterImg"),
                        rs.getString("movieRating"),
                        rs.getString("reserveRate")),
                movieId);
    }

}
