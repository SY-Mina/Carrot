package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;

    private JdbcTemplate jdbcTemplate;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public List<GetUserRes> getUsers( ) throws BaseException {

        try {
            List<GetUserRes> getUsersRes = userDao.getUsers();
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
        
    }


    public GetUserRes getUser(int userIdx) {
        GetUserRes getUserRes = userDao.getUser(userIdx);
        return getUserRes;
    }

    public int checkEmail(String email){
        return userDao.checkEmail(email);
    }

    public int checkUser(String userName) {return userDao.checkUser(userName);}

    public int checkUserExist(String userName) {return userDao.checkUserExist(userName);}

//    public List<GetUserRes> getUsersByEmail(String email) throws BaseException{
//        try{
//            List<GetUserRes> getUsersRes = userDao.getUsersByEmail(email);
//            return getUsersRes;
//        }
//        catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
//
//    public GetUserRes getUser(int userIdx) throws BaseException {
//        try {
//            GetUserRes getUserRes = userDao.getUser(userIdx);
//            return getUserRes;
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }
//
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        GetUserInfo user = userDao.getPwd(postLoginReq);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.PASSWORD_DECRYPTION_ERROR);
        }

        if(postLoginReq.getPassword().equals(password)){
            int userIdx = userDao.getPwd(postLoginReq).getUserIdx();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx,jwt);
        }
        else{
            throw new BaseException(BaseResponseStatus.FAILED_TO_LOGIN);
        }

    }
}
