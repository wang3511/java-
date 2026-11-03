package com.peiwan.servlet;

import com.peiwan.dao.UserDao;
import com.peiwan.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 玩家首页:展示所有陪玩,可以点击"下单"
 */
@WebServlet("/peiwan/list")
public class PeiwanListServlet extends HttpServlet {
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> peiwanList = userDao.listAllPeiwan();
        req.setAttribute("peiwanList", peiwanList);
        req.getRequestDispatcher("/WEB-INF/views/peiwan-list.jsp").forward(req, resp);
    }
}
