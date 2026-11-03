package com.peiwan.servlet;

import com.peiwan.dao.EvaluationDao;
import com.peiwan.dao.OrderDao;
import com.peiwan.entity.Evaluation;
import com.peiwan.entity.GameOrder;
import com.peiwan.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 玩家对已完成的订单进行评价
 */
@WebServlet("/order/evaluate")
public class EvaluateServlet extends HttpServlet {
    private OrderDao orderDao = new OrderDao();
    private EvaluationDao evaluationDao = new EvaluationDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer orderId = Integer.parseInt(req.getParameter("orderId"));
        GameOrder order = orderDao.findById(orderId);
        if (order == null || !"FINISHED".equals(order.getStatus())) {
            resp.sendRedirect(req.getContextPath() + "/order/my");
            return;
        }
        req.setAttribute("order", order);
        req.getRequestDispatcher("/WEB-INF/views/order-evaluate.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        Integer orderId = Integer.parseInt(req.getParameter("orderId"));
        Integer score = Integer.parseInt(req.getParameter("score"));
        String content = req.getParameter("content");

        GameOrder order = orderDao.findById(orderId);
        if (order == null || !order.getPlayerId().equals(loginUser.getId())) {
            resp.sendRedirect(req.getContextPath() + "/order/my");
            return;
        }

        Evaluation ev = new Evaluation();
        ev.setOrderId(orderId);
        ev.setPlayerId(loginUser.getId());
        ev.setPeiwanId(order.getPeiwanId());
        ev.setScore(score);
        ev.setContent(content);
        evaluationDao.insert(ev);

        orderDao.markEvaluated(orderId);
        resp.sendRedirect(req.getContextPath() + "/order/my");
    }
}
