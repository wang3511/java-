package com.peiwan.servlet;

import com.peiwan.dao.UserDao;
import com.peiwan.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String nickname = req.getParameter("nickname");
        String role = req.getParameter("role");
        String game = req.getParameter("game");
        String priceStr = req.getParameter("price");

        // 简单校验
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()
                || role == null || role.trim().isEmpty()) {
            req.setAttribute("error", "用户名/密码/角色不能为空");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        if (userDao.findByUsername(username) != null) {
            req.setAttribute("error", "用户名已被占用");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        User u = new User();
        u.setUsername(username.trim());
        u.setPassword(password.trim());
        u.setNickname(nickname);
        u.setRole(role);
        if ("PEIWAN".equals(role)) {
            u.setGame(game);
            double price = 0;
            try { price = Double.parseDouble(priceStr); } catch (Exception ignored) {}
            u.setPrice(price);
            u.setBalance(0.0);
        } else {
            u.setBalance(1000.0);   // 玩家注册送 1000 体验金,方便下单
        }

        boolean ok = userDao.insert(u);
        if (ok) {
            req.setAttribute("msg", "注册成功,请登录");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", "注册失败,请稍后再试");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        }
    }
}
