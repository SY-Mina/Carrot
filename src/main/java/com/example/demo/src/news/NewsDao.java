package com.example.demo.src.news;


import com.example.demo.src.news.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class NewsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetNewsRes> getNews(){
        return this.jdbcTemplate.query("select News.idx as newsIdx, userName, location,\n" +
                        "       (select typeName\n" +
                        "           from NewsType\n" +
                        "           where News.type=NewsType.idx) as type,\n" +
                        "       content,\n" +
                        "       (case when DATEDIFF(now(), News.updateAt)>=1\n" +
                        "           then concat(DATEDIFF(now(), News.updateAt), '일 전')\n" +
                        "           when TIMESTAMPDIFF(Hour, News.updateAt, now())>=1\n" +
                        "           then concat(TIMESTAMPDIFF(Hour, News.updateAt, now()), '시간 전')\n" +
                        "           when TIMESTAMPDIFF(minute, News.updateAt, now())>=1\n" +
                        "           then concat(TIMESTAMPDIFF(minute, News.updateAt, now()), '분 전')\n" +
                        "           else concat(TIMESTAMPDIFF(second , News.updateAt, now()), '초 전')\n" +
                        "           end) as time,\n" +
                        "       concat('댓글 ', (select count(NewsComment.idx)\n" +
                        "           from NewsComment\n" +
                        "           where News.idx=NewsComment.newsIdx)) as comment,\n" +
                        "       (select count(NewsHeart.idx)\n" +
                        "           from NewsHeart\n" +
                        "           where NewsHeart.type=1 and News.idx=NewsHeart.newsIdx) as qurious,\n" +
                        "       (select count(NewsHeart.idx)\n" +
                        "           from NewsHeart\n" +
                        "           where NewsHeart.type=2 and News.idx=NewsHeart.newsIdx) as good\n" +
                        "from News join User\n" +
                        "where News.userIdx=User.idx\n" +
                        "order by News.updateAt desc;",
                (rs, rowNum) -> new GetNewsRes(
                        rs.getInt("newsIdx"),
                        rs.getString("userName"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getString("content"),
                        rs.getString("time"),
                        rs.getString("comment"),
                        rs.getInt("qurious"),
                        rs.getInt("good")));
    }

    public GetNewsUserRes getNewsUser(int newsIdx){
        return this.jdbcTemplate.queryForObject("select User.idx as userIdx, userName, profImg, location\n" +
                        "from News join User\n" +
                        "where News.idx = ? and News.userIdx = User.idx;" ,
                (rs, rowNum) -> new GetNewsUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("profImg"),
                        rs.getString("location")),
                newsIdx);
    }

    public GetNewsInfoRes getNewsInfo(int newsIdx){
        return this.jdbcTemplate.queryForObject("select content,\n" +
                        "       (select typeName\n" +
                        "           from NewsType\n" +
                        "           where News.type=NewsType.idx) as typeName,\n" +
                        "       (select count(NewsHeart.idx)\n" +
                        "           from NewsHeart\n" +
                        "           where NewsHeart.type=1 and News.idx=NewsHeart.newsIdx) as qurious,\n" +
                        "       (select count(NewsHeart.idx)\n" +
                        "           from NewsHeart\n" +
                        "           where NewsHeart.type=2 and News.idx=NewsHeart.newsIdx) as good,\n" +
                        "       (select count(NewsComment.idx)\n" +
                        "           from NewsComment where NewsComment.newsIdx=News.idx) as comment,\n" +
                        "       (select count(Interest.idx)\n" +
                        "           from Interest where Interest.itemIdx=News.idx) as interest,\n" +
                        "       (select count(Views.idx)\n" +
                        "           from Views where Views.itemIdx=News.idx) as views,\n" +
                        "       (case when DATEDIFF(now(), News.updateAt)>=1\n" +
                        "           then concat(DATEDIFF(now(), News.updateAt), '일 전')\n" +
                        "           when TIMESTAMPDIFF(Hour, News.updateAt, now())>=1\n" +
                        "           then concat(TIMESTAMPDIFF(Hour, News.updateAt, now()), '시간 전')\n" +
                        "           when TIMESTAMPDIFF(minute, News.updateAt, now())>=1\n" +
                        "           then concat(TIMESTAMPDIFF(minute, News.updateAt, now()), '분 전')\n" +
                        "           else concat(TIMESTAMPDIFF(second , News.updateAt, now()), '초 전')\n" +
                        "           end) as time\n" +
                        "from News\n" +
                        "where News.idx=?;" ,
                (rs, rowNum) -> new GetNewsInfoRes(
                        rs.getString("content"),
                        rs.getString("typeName"),
                        rs.getInt("qurious"),
                        rs.getInt("good"),
                        rs.getInt("interest"),
                        rs.getInt("comment"),
                        rs.getInt("views"),
                        rs.getString("time")),
                newsIdx);
    }

    public List<GetNewsCommentRes> getNewsComment(int newsIdx){
        return this.jdbcTemplate.query("select User.idx, userName, location,\n" +
                        "       (case when DATEDIFF(now(), NewsComment.updateAt)>=1\n" +
                        "           then concat(DATEDIFF(now(), NewsComment.updateAt), '일 전')\n" +
                        "           when TIMESTAMPDIFF(Hour, NewsComment.updateAt, now())>=1\n" +
                        "           then concat(TIMESTAMPDIFF(Hour, NewsComment.updateAt, now()), '시간 전')\n" +
                        "           when TIMESTAMPDIFF(minute, NewsComment.updateAt, now())>=1\n" +
                        "           then concat(TIMESTAMPDIFF(minute, NewsComment.updateAt, now()), '분 전')\n" +
                        "           else concat(TIMESTAMPDIFF(second , NewsComment.updateAt, now()), '초 전')\n" +
                        "           end) as time,\n" +
                        "       content\n" +
                        "from NewsComment join User\n" +
                        "where NewsComment.userIdx = User.idx and NewsComment.newsIdx = ?;",
                (rs, rowNum) -> new GetNewsCommentRes(
                        rs.getInt("idx"),
                        rs.getString("userName"),
                        rs.getString("location"),
                        rs.getString("time"),
                        rs.getString("content")),
                newsIdx);
    }


    public List<GetImageRes> getImages(int newsIdx){
        return this.jdbcTemplate.query("select image\n" +
                        "from NewsImage join News\n" +
                        "where News.idx=NewsImage.newsIdx and News.idx=?;" ,
                (rs, rowNum) -> new GetImageRes(
                        rs.getString("image")),
                newsIdx);
    }

    public int checkItemExist(int itemIdx) {
        return this.jdbcTemplate.queryForObject("select exists(select idx from News where idx = ?)",
                int.class,
                itemIdx);

    }

    public int patchNews (PatchNewsReq patchNewsReq, int newsIdx) {
        this.jdbcTemplate.update("update News set content = ?, type = ? where News.idx = ?",
                new Object[]{patchNewsReq.getContent(), patchNewsReq.getType(), newsIdx}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int checkNewsExist(int newsIdx) {
        return this.jdbcTemplate.queryForObject("select exists(select idx from News where idx=?)",
                int.class,
                newsIdx);
    }

    public int checkStatus (int newsIdx, int userIdx) {
        return this.jdbcTemplate.queryForObject("select exists(select idx from News where idx = ? and userIdx = ?)",
                int.class,
                newsIdx, userIdx);
    }

    public int postNews(PostNewsReq postNewsReq, int userIdx) {
        this.jdbcTemplate.update("insert into News (userIdx, content, type) VALUE (?,?,?)",
                new Object[]{userIdx, postNewsReq.getContent(), postNewsReq.getType()}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int patchNewsStatus (int newsIdx, int userIdx) {
        this.jdbcTemplate.update("update News set status = 'F' where News.idx = ? and News.userIdx=?",
                new Object[]{newsIdx, userIdx}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }
}
