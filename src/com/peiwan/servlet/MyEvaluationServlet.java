package com.peiwan.servlet;

import com.peiwan.dao.EvaluationDao;
import com.peiwan.entity.Evaluation;
import com.peiwan.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 陪玩查看自己收到的所有评价
 */
@WebServlet("/evaluation/my")
public class MyEvaluationServlet extends HttpServlet {
    private EvaluationDao evaluationDao = new EvaluationDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        List<Evaluation> list = evaluationDao.listByPeiwan(loginUser.getId());
        req.setAttribute("evaluations", list);
        req.getRequestDispatcher("/WEB-INF/views/evaluation-my.jsp").forward(req, resp);
    }
}
