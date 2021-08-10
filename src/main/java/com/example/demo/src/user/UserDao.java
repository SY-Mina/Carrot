package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        return this.jdbcTemplate.query("select * from User",
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("idx"),
                        rs.getString("userName"),
                        rs.getString("profImg"),
                        rs.getString("location")));
    }

    public GetUserRes getUser(int userIdx){
        return this.jdbcTemplate.queryForObject("select * from User where User.idx=?" ,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("idx"),
                        rs.getString("userName"),
                        rs.getString("profImg"),
                        rs.getString("location")),
                userIdx);
    }


    public int createUser(PostUserReq postUserReq){
        this.jdbcTemplate.update("insert into User (userName, email, password, location) VALUES (?,?,?,?)",
                new Object[]{postUserReq.getUserName(), postUserReq.getEmail(), postUserReq.getPassword(), postUserReq.getLocation()}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }

    public int checkEmail(String email){
        return this.jdbcTemplate.queryForObject("select exists(select email from UserInfo where email = ?)",
                int.class,
                email);

    }

    public int checkUser(String userName) {
        return this.jdbcTemplate.queryForObject("select exists(select idx from User where status='T' and userName=?);",
                int.class, userName);
    }

    public int checkUserExist(String userName) {
        return this.jdbcTemplate.queryForObject("select exists(select idx from User where userName=?);",
                int.class, userName);
    }

    public int checkKakaoUserExist (String nickname, String email) {
        return this.jdbcTemplate.queryForObject("select exists(select idx from User where userName=? and email=?);",
                int.class, nickname, email);
    }

//    public int postLogin(PostLoginReq postLoginReq){
//        this.jdbcTemplate.update("insert into Log (userName, email, password, location) VALUES (?,?,?,?)",
//                new Object[]{postUserReq.getUserName(), postUserReq.getEmail(), postUserReq.getPassword(), postUserReq.getLocation()}
//        );
//        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
//    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update User set userName = ? where idx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public GetUserInfo getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select idx, userName,email,password,location from User where userName = ?";
        String getPwdParams = postLoginReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new GetUserInfo(
                        rs.getInt("idx"),
                        rs.getString("userName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("location")
                ),
                getPwdParams);

    }

    public int getKakaoUser(String nickname, String email){

        return this.jdbcTemplate.queryForObject("select idx from User where userName = ? and email=?",
                int.class, nickname, email);

    }

    public int createKakaoUser(String nickname, String email){
        String kakaoPwd = "kakao";
        this.jdbcTemplate.update("insert into User (userName, email, location, password) VALUES (?,?,?,?)",
                new Object[]{nickname, email, "",kakaoPwd}
        );
        return this.jdbcTemplate.queryForObject("select last_insert_id()",int.class);
    }
}
