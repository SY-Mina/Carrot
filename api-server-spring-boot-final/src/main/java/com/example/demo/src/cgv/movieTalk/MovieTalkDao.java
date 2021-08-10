package com.example.demo.src.movieTalk;


import com.example.demo.src.movieTalk.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class MovieTalkDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetMovieTalkRes> getMovieTalks(){
        return this.jdbcTemplate.query("select Movie.movieName as title,\n" +
                        "    (case when DATEDIFF(now(), MovieTalk.createdAt)<1\n" +
                        "            then concat(TIMESTAMPDIFF(HOUR, now(), MovieTalk.createdAt), '시간 전')\n" +
                        "        when DATEDIFF(now(), MovieTalk.createdAt)<2\n" +
                        "            then '어제'\n" +
                        "        when DATEDIFF(now(), MovieTalk.createdAt)<35\n" +
                        "            then '한 달전'\n" +
                        "        else DATE_FORMAT(MovieTalk.createdAt, '%c월 %d일')\n" +
                        "        end) as time,\n" +
                        "       MovieTalk.movieIdx, postThumbnail, postContent,\n" +
                        "       (select COUNT(idx)\n" +
                        "           from Heart\n" +
                        "           where Heart.postId=MovieTalk.idx) as heart,\n" +
                        "       (select COUNT(Comment.commentId)\n" +
                        "           from Comment\n" +
                        "           where Comment.postId = MovieTalk.idx) as comment\n" +
                        "from MovieTalk inner join Movie\n" +
                        "where MovieTalk.movieIdx = Movie.idx;",
                (rs, rowNum) -> new GetMovieTalkRes(
                        rs.getString("title"),
                        rs.getString("time"),
                        rs.getString("postThumbnail"),
                        rs.getString("postContent"),
                        rs.getInt("heart"),
                        rs.getInt("comment")));
    }

}
