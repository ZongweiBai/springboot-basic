package com.baymin.springboot.adminserver.constant;

/**
 * Created by baymin on 2016/4/26.
 */
public class WebConstant {

    public static final String ADMIN_USER_SESSION = "ADMIN_USER_SESSION";
    public static final String SELLER_USER_SESSION = "SELLER_USER_SESSION";
    public static final String SELLER_INFO_SESSION = "SELLER_INFO_SESSION";

    public static int SUCCESS = 200; // 成功
    public static int FAULT = 500; // 失败

    public static final String RESULT = "result";
    public static final String MESSAGE = "message";
    public static final String INFO = "info";
    public static final String ROWS = "rows";
    public static final String TOTAL = "total";

    public static final String[] NOT_IN_FILTER = {
            "/manage/",
            "/manage/index.jsp",
            "/manage/login.jsp",
            "/manage/system/sysUser/login.do",
            "/manage/assets/page/404.jsp",
            "/manage/assets/page/405.jsp",
            "/manage/assets/page/500.jsp",
            "/manage/notlogin"};

}
