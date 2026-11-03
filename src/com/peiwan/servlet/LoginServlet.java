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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = userDao.findByUsernameAndPassword(username, password);
        if (user == null) {
            req.setAttribute("error", "用户名或密码错误");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("loginUser", user);

        // 根据角色跳到不同首页
        if (user.isPlayer()) {
            resp.sendRedirect(req.getContextPath() + "/peiwan/list");
        } else {
            resp.sendRedirect(req.getContextPath() + "/order/available");
        }
    }
}
