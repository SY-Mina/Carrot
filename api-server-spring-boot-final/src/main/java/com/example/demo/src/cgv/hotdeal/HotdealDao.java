package com.example.demo.src.hotdeal;


import com.example.demo.src.hotdeal.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class HotdealDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetHotdealFoodRes> getFoods(){
        return this.jdbcTemplate.query("select Food.foodName as name,\n" +
                        "       (case when DATEDIFF(Hotdeal.dDay, now())>1\n" +
                        "        then CONCAT('D-', DATEDIFF(Hotdeal.dDay, now()))\n" +
                        "        when DATEDIFF(Hotdeal.dDay, now())<=1\n" +
                        "        then 'D-DAY'\n" +
                        "        end) as dDay, img, CONCAT(FORMAT(price, 0), '원')\n" +
                        "    as price, CONCAT(FORMAT(discount, 0), '원')\n" +
                        "    as discount,\n" +
                        "       (case when `left`=0\n" +
                        "           then '마감'\n" +
                        "           else CONCAT(`left`, '개 남음')\n" +
                        "           end) as leftNum\n" +
                        "from Food join Hotdeal\n" +
                        "where Hotdeal.foodId = Food.idx;",
                (rs, rowNum) -> new GetHotdealFoodRes(
                        rs.getString("name"),
                        rs.getString("dDay"),
                        rs.getString("img"),
                        rs.getString("price"),
                        rs.getString("discount"),
                        rs.getString("leftNum")));
    }

}
