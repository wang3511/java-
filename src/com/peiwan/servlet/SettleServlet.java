package com.peiwan.servlet;

import com.peiwan.dao.OrderDao;
import com.peiwan.dao.UserDao;
import com.peiwan.entity.GameOrder;
import com.peiwan.entity.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 陪玩结算:订单被玩家评价后,陪玩把钱收进自己余额
 */
@WebServlet("/order/settle")
public class SettleServlet extends HttpServlet {
    private OrderDao orderDao = new OrderDao();
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        Integer orderId = Integer.parseInt(req.getParameter("orderId"));
        GameOrder order = orderDao.findById(orderId);

        if (order != null && "EVALUATED".equals(order.getStatus())
                && order.getPeiwanId().equals(loginUser.getId())) {
            boolean ok = orderDao.settle(orderId);
            if (ok) {
                // 陪玩余额 +amount,玩家余额 -amount
                userDao.addBalance(order.getPeiwanId(), order.getAmount());
                userDao.addBalance(order.getPlayerId(), -order.getAmount());
            }
        }
        resp.sendRedirect(req.getContextPath() + "/order/taken");
    }
}
