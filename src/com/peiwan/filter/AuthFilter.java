package com.peiwan.filter;

import com.peiwan.dao.UserDao;
import com.peiwan.entity.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录校验:除了登录/注册/静态资源,其他都要先登录
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final String[] WHITE_LIST = {
            "/login", "/register", "/account/delete", "/index.jsp", "/"
    };

    private final UserDao userDao = new UserDao();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.isEmpty()) path = "/";

        // 白名单直接放行
        for (String white : WHITE_LIST) {
            if (path.equals(white) || path.startsWith("/css/") || path.startsWith("/js/")) {
                chain.doFilter(request, response);
                return;
            }
        }

        HttpSession session = req.getSession(false);
        User loginUser = session == null ? null : (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        // 每次请求都从数据库取一次最新资料,保证页面上的余额等数据是最新的
        User fresh = userDao.findById(loginUser.getId());
        if (fresh != null) {
            session.setAttribute("loginUser", fresh);
        }
        chain.doFilter(request, response);
    }
}
