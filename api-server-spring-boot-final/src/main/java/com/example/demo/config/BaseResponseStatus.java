package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    LOGIN_USERS_ID_INVALID(false, 2018, "아이디 값은 4자 이상이어야 합니다."),
    LOGIN_USERS_PASSWORD_INVALID(false, 2019, "비밀번호 값은 4자 이상이어야 합니다. "),


    // [POST] /items
    POST_ITEMS_DESCRIPTION_INVALID(false, 2020, "게시물은 5자 이상이어야 합니다."),
    PATCH_ITEM_FAIL(false, 2021, "수정에 실패하였습니다"),

    LOGIN_USERS_INVALID(false, 2030, "비활성화되거나 탈퇴한 유저입니다"),
    LOGIN_USERS_EXIST(false, 2031, "존재하지 않는 유저입니다."),

    SIGNIN_USERS_EXIST(false, 2040, "중복된 아이디 값입니다."),

    PATCH_USER_INVALID_STATUS(false, 2041, "삭제할 권한이 없습니다."),
    PATCH_USER_INVALID(false, 2042, "수정할 권한이 없습니다."),
    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    // [GET] /items
    GET_ITEMS_EMPTY(false, 3001, "불러올 제품이 없습니다."),
    GET_NEWS_EMPTY(false, 3004, "불러올 게시글이 없습니다."),

    // [GET] /items/:itemIdx
    GET_ITEM_EMPTY(false, 3002, "존재하지 않는 제품입니다."),

    // [GET] /items/:userIdx/recommend
    GET_RECOMMEND_EMPTY(false, 3003, "사용자의 다른 제품이 없습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
