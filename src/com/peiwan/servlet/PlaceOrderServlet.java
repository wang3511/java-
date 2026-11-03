package com.peiwan.servlet;

import com.peiwan.dao.OrderDao;
import com.peiwan.dao.UserDao;
import com.peiwan.entity.GameOrder;
import com.peiwan.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 玩家下单: GET 显示下单表单, POST 提交订单
 */
@WebServlet("/order/place")
public class PlaceOrderServlet extends HttpServlet {
    private UserDao userDao = new UserDao();
    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 把要下单的陪玩信息带到页面
        String peiwanIdStr = req.getParameter("peiwanId");
        if (peiwanIdStr == null) {
            resp.sendRedirect(req.getContextPath() + "/peiwan/list");
            return;
        }
        User peiwan = userDao.findById(Integer.parseInt(peiwanIdStr));
        if (peiwan == null || !peiwan.isPeiwan()) {
            resp.sendRedirect(req.getContextPath() + "/peiwan/list");
            return;
        }
        req.setAttribute("peiwan", peiwan);
        req.getRequestDispatcher("/WEB-INF/views/order-place.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        if (loginUser == null || !loginUser.isPlayer()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String peiwanIdStr = req.getParameter("peiwanId");
        String hoursStr = req.getParameter("hours");
        String game = req.getParameter("game");

        User peiwan = userDao.findById(Integer.parseInt(peiwanIdStr));
        int hours;
        try {
            hours = Integer.parseInt(hoursStr);
            if (hours <= 0 || hours > 24) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            req.setAttribute("error", "陪玩时长必须是 1~24 小时");
            req.setAttribute("peiwan", peiwan);
            req.getRequestDispatcher("/WEB-INF/views/order-place.jsp").forward(req, resp);
            return;
        }

        double amount = peiwan.getPrice() * hours;

        // 校验余额
        if (loginUser.getBalance() < amount) {
            req.setAttribute("error", "余额不足,当前余额 ¥" + loginUser.getBalance() + ",需要 ¥" + amount);
            req.setAttribute("peiwan", peiwan);
            req.getRequestDispatcher("/WEB-INF/views/order-place.jsp").forward(req, resp);
            return;
        }

        GameOrder order = new GameOrder();
        order.setOrderNo(generateOrderNo());
        order.setPlayerId(loginUser.getId());
        order.setPeiwanId(peiwan.getId());
        order.setGame(game);
        order.setHours(hours);
        order.setAmount(amount);
        order.setStatus("PENDING");
        orderDao.insert(order);

        resp.sendRedirect(req.getContextPath() + "/order/my");
    }

    /** 生成订单号:P + 时间戳 + 3位随机数 */
    private String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        int rand = (int) (Math.random() * 900) + 100;
        return "P" + sdf.format(new Date()) + rand;
    }
}
