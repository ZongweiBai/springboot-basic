package com.baymin.springboot.adminserver.filer;

import com.baymin.springboot.adminserver.constant.WebConstant;
import com.baymin.springboot.store.entity.Admin;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获得在下面代码中要用的request,response,session对象
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        HttpSession session = servletRequest.getSession();

        // 获得用户请求的URI
        String path = servletRequest.getRequestURI();

        // 从session里取账号
        Admin user = (Admin) session.getAttribute(WebConstant.ADMIN_USER_SESSION);
        if (user == null) {
            user = (Admin) session.getAttribute(WebConstant.SELLER_USER_SESSION);
        }

        // 以下页面无需过滤
        for (String str : WebConstant.NOT_IN_FILTER) {
            if (str.equals(path)) {
                chain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        // 判断如果没有取到登录信息,就跳转到登陆页面
        if (user == null) {
            servletResponse.sendRedirect(servletRequest.getContextPath() + "/nologin");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }

}
