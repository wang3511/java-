package com.peiwan.servlet;

import com.peiwan.dao.OrderDao;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 陪玩接单
 */
@WebServlet("/order/take")
public class TakeOrderServlet extends HttpServlet {
    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer orderId = Integer.parseInt(req.getParameter("orderId"));
        orderDao.takeOrder(orderId);
        resp.sendRedirect(req.getContextPath() + "/order/taken");
    }
}
