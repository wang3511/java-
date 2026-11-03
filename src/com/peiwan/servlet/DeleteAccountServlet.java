package com.peiwan.servlet;

import com.peiwan.dao.UserDao;
import com.peiwan.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 注销账号:永久删除账号及其全部订单、评价
 * 已登录:直接注销当前账号;未登录(登录页):需提供用户名+密码
 */
@WebServlet("/account/delete")
public class DeleteAccountServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String ctx = req.getContextPath();
        HttpSession session = req.getSession(false);
        User loginUser = session == null ? null : (User) session.getAttribute("loginUser");

        Integer targetId;
        if (loginUser != null) {
            targetId = loginUser.getId();
        } else {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            User u = (username == null || password == null)
                    ? null
                    : userDao.findByUsernameAndPassword(username.trim(), password);
            if (u == null) {
                resp.sendRedirect(ctx + "/login?deleteErr=1");
                return;
            }
            targetId = u.getId();
        }

        boolean ok = userDao.deleteAccount(targetId);
        if (session != null) {
            session.invalidate();
        }
        resp.sendRedirect(ctx + "/login?" + (ok ? "deleted=1" : "deleteErr=1"));
    }
}
