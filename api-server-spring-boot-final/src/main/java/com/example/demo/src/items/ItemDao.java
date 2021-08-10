package com.example.demo.src.items;


import com.example.demo.src.items.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class ItemDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetItemsRes> getItems() {
        return this.jdbcTemplate.query("select Item.idx, itemName, (select ItemImage.image\n" +
                        "            from ItemImage where Item.idx=ItemImage.itemIdx\n" +
                        "    group by itemName limit 1) as image,\n" +
                        "       concat(FORMAT(price,0), '원') as price,\n" +
                        "       (select count(Heart.idx)\n" +
                        "           from Heart\n" +
                        "           where Item.idx=Heart.itemIdx) as heart,\n" +
                        "       (select count(Comment.idx)\n" +
                        "           from Comment\n" +
                        "           where Item.idx = Comment.itemIdx) as comment,\n" +
                        "       (case when DATEDIFF(now(), Item.updateAt)>=1\n" +
                        "           then concat(DATEDIFF(now(), Item.updateAt), '일 전')\n" +
                        "           when TIMESTAMPDIFF(Hour, Item.updateAt, now())>=1\n" +
                        "           then concat(TIMESTAMPDIFF(Hour, Item.updateAt, now()), '시간 전')\n" +
                        "           when TIMESTAMPDIFF(minute, Item.updateAt, now())>=1\n" +
                        "           then concat(TIMESTAMPDIFF(minute, Item.updateAt, now()), '분 전')\n" +
                        "           else concat(TIMESTAMPDIFF(second , Item.updateAt, now()), '초 전')\n" +
                        "           end) as time,\n" +
                        "       location\n" +
                        "from Item join User\n" +
                        "where Item.userIdx = User.idx\n" +
                        "order by Item.updateAt desc;",
                (rs, rowNum) -> new GetItemsRes(
                        rs.getInt("idx"),
                        rs.getString("itemName"),
                        rs.getString("image"),
                        rs.getString("price"),
                        rs.getInt("heart"),
                        rs.getInt("comment"),
                        rs.getString("time"),
                        rs.getString("location")));
    }

    public GetItemUserRes getItemUser(int itemIdx){
        return this.jdbcTemplate.queryForObject("select User.idx, userName, profImg, location,\n" +
                        "        (case when manner >0\n" +
                        "        then concat(manner, '''C')\n" +
                        "        else concat(36.5, '''C') end) as manner\n" +
                        "from Item join User\n" +
                        "where Item.idx = ? and Item.userIdx = User.idx;" ,
                (rs, rowNum) -> new GetItemUserRes(
                        rs.getInt("idx"),
                        rs.getString("userName"),
                        rs.getString("profImg"),
                        rs.getString("location"),
                        rs.getString("manner")),
                itemIdx);
    }

    public GetItemInfoRes getItemInfo(int itemIdx){
        return this.jdbcTemplate.queryForObject("select Item.idx, itemName, concat(FORMAT(price,0), '원') as price,\n" +
                        "       (select typeName\n" +
                        "           from ItemType\n" +
                        "           where Item.typeIdx=ItemType.idx) as typeName,\n" +
                        "       Item.description,\n" +
                        "       (select count(Heart.idx)\n" +
                        "           from Heart where Heart.itemIdx=Item.idx) as heart,\n" +
                        "       (select count(Interest.idx)\n" +
                        "           from Interest where Interest.itemIdx=Item.idx) as interest,\n" +
                        "       (select count(Views.idx)\n" +
                        "           from Views where Views.itemIdx=Item.idx) as views,\n" +
                        "       (case when DATEDIFF(now(), Item.updateAt)>=1\n" +
                        "           then concat(DATEDIFF(now(), Item.updateAt), '일 전')\n" +
                        "           when TIMESTAMPDIFF(Hour, Item.updateAt, now())>=1\n" +
                        "           then concat(TIMESTAMPDIFF(Hour, Item.updateAt, now()), '시간 전')\n" +
                        "           when TIMESTAMPDIFF(minute, Item.updateAt, now())>=1\n" +
                        "           then concat(TIMESTAMPDIFF(minute, Item.updateAt, now()), '분 전')\n" +
                        "           else concat(TIMESTAMPDIFF(second , Item.updateAt, now()), '초 전')\n" +
                        "           end) as time\n" +
                        "from Item\n" +
                        "where Item.idx=?;" ,
                (rs, rowNum) -> new GetItemInfoRes(
                        rs.getInt("idx"),
                        rs.getString("itemName"),
                        rs.getString("price"),
                        rs.getString("typeName"),
                        rs.getString("description"),
                        rs.getInt("heart"),
                        rs.getInt("interest"),
                        rs.getInt("views"),
                        rs.getString("time")),
                itemIdx);
    }

    public List<GetImageRes> getImages(int itemIdx){
        return this.jdbcTemplate.query("select image\n" +
                        "from ItemImage join Item\n" +
                        "where Item.idx=ItemImage.itemIdx and Item.idx=?;" ,
                (rs, rowNum) -> new GetImageRes(
                        rs.getString("image")),
                itemIdx);
    }

    public List<GetRecommendRes> getItem(int userIdx) {
        return this.jdbcTemplate.query("select Item.idx,\n" +
                        "       (select userName\n" +
                        "       from User where User.idx = Item.userIdx) as userName,\n" +
                        "       itemName, ItemImage.image,\n" +
                        "            concat(FORMAT(price,0), '원') as price\n" +
                        "from Item join ItemImage\n" +
                        "where Item.userIdx = ? and Item.status='T' group by itemName;",
                (rs, rowNum) -> new GetRecommendRes(
                        rs.getInt("idx"),
                        rs.getString("userName"),
                        rs.getString("itemName"),
                        rs.getString("image"),
                        rs.getString("price")),
                userIdx);
    }

    public int postItem(PostBoardReq postBoardReq, int userIdx) {
        this.jdbcTemplate.update("insert into Item (userIdx, itemName, price, description, typeIdx) VALUE (?,?,?,?,?)",
                new Object[]{userIdx, postBoardReq.getItemName(), postBoardReq.getPrice(), postBoardReq.getDescription(), postBoardReq.getTypeIdx()}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int postHeart(int userIdx, int itemIdx) {
        this.jdbcTemplate.update("insert into Heart (userIdx, itemIdx) VALUE (?,?)",
                new Object[]{userIdx, itemIdx}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int postComment(PostCommentReq postCommentReq,int userIdx) {
        this.jdbcTemplate.update("insert into Comment (itemIdx, userIdx, content) VALUE (?,?,?)",
                new Object[]{postCommentReq.getItemIdx(), userIdx, postCommentReq.getContent()}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int checkEmail(String email) {
        return this.jdbcTemplate.queryForObject("select exists(select email from User where email = ?)",
                int.class,
                email);

    }

    public int checkItem() {
        return this.jdbcTemplate.queryForObject("select exists(select idx from Item)",
                int.class);
    }

    public int checkItemExist(int itemIdx) {
        return this.jdbcTemplate.queryForObject("select exists(select idx from Item where idx=?)",
                int.class,
                itemIdx);
    }


    public int checkItemRecommendExist(int userIdx) {
        return this.jdbcTemplate.queryForObject("select exists(select idx from Item where userIdx = ?)",
                int.class,
                userIdx);

    }

    public int checkHeart(int userIdx, int itemIdx) {
        return this.jdbcTemplate.queryForObject("select exists(select idx from Heart where userIdx = ? and itemIdx = ?)",
                int.class,
                userIdx, itemIdx);
    }

    public String checkStatusHeart(int userIdx, int itemIdx) {
        return this.jdbcTemplate.queryForObject("select status from Heart where userIdx = ? and itemIdx = ?;" ,
                (rs, rowNum) -> new String(
                        rs.getString("status")),
                userIdx, itemIdx);
    }
    
    public int patchItem (PatchItemReq patchItemReq, int itemIdx) {
        this.jdbcTemplate.update("update Item set itemName = ?, price = ?, description = ?, typeIdx = ? where Item.idx = ?",
                new Object[]{patchItemReq.getItemName(), patchItemReq.getPrice(), patchItemReq.getDescription(), patchItemReq.getTypeIdx(), itemIdx}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int patchItemStatus (int itemIdx,int userIdx) {
        this.jdbcTemplate.update("update Item set status = 'F' where Item.idx = ? and Item.userIdx=?",
                new Object[]{itemIdx, userIdx}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int patchHeart (String status, int userIdx, int itemIdx) {
        this.jdbcTemplate.update("update Heart set status = ? where Heart.userIdx=? and Heart.itemIdx=?",
                new Object[]{status, userIdx, itemIdx}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int checkStatus (int itemIdx, int userIdx) {
        return this.jdbcTemplate.queryForObject("select exists(select idx from Item where idx = ? and userIdx = ?)",
                int.class,
                itemIdx, userIdx);
    }
}
