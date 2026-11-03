package com.peiwan.servlet;

import com.peiwan.dao.OrderDao;
import com.peiwan.entity.GameOrder;
import com.peiwan.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 陪玩首页:列出玩家指定给自己的、待接单的订单
 */
@WebServlet("/order/available")
public class AvailableOrderServlet extends HttpServlet {
    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        List<GameOrder> orders = orderDao.listAvailableFor(loginUser.getId());
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/WEB-INF/views/order-available.jsp").forward(req, resp);
    }
}
