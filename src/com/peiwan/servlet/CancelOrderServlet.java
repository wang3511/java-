package com.peiwan.servlet;

import com.peiwan.dao.OrderDao;
import com.peiwan.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 玩家撤销尚未被接单的订单 */
@WebServlet("/order/cancel")
public class CancelOrderServlet extends HttpServlet {

    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        String idStr = req.getParameter("orderId");
        boolean ok = false;
        if (idStr != null) {
            try {
                // DAO 里同时校验了订单归属和状态,不能撤销别人的单或已接的单
                ok = orderDao.cancel(Integer.valueOf(idStr), loginUser.getId());
            } catch (NumberFormatException ignored) {
            }
        }
        resp.sendRedirect(req.getContextPath() + "/order/my" + (ok ? "?cancelOk=1" : ""));
    }
}
