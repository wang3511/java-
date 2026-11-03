<%@ page contentType="text/html;charset=UTF-8" import="java.util.List,com.peiwan.entity.User,com.peiwan.entity.Evaluation,java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>我的评价</title>
    <style>
        body { font-family: "Microsoft YaHei", sans-serif; background: #f5f5f5; margin: 0; }
        .header { background: #2196F3; color: white; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        .header a { color: white; text-decoration: none; margin-left: 15px; }
        .container { max-width: 900px; margin: 20px auto; padding: 0 20px; }
        .card { background: white; padding: 20px; margin-bottom: 15px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .head { display: flex; justify-content: space-between; margin-bottom: 10px; color: #666; }
        .stars { color: #FFC107; font-size: 18px; }
        .content { color: #333; line-height: 1.6; padding: 10px; background: #f9f9f9; border-radius: 4px; }
        .empty { text-align: center; color: #999; padding: 50px; background: white; border-radius: 8px; }
    </style>
</head>
<body>
<%
    User loginUser = (User) session.getAttribute("loginUser");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>
<div class="header">
    <div>欢迎,<b><%=loginUser.getNickname() != null ? loginUser.getNickname() : loginUser.getUsername()%></b>(陪玩) | 余额:¥<%=String.format("%.2f", loginUser.getBalance())%></div>
    <div>
        <a href="<%=request.getContextPath()%>/order/available">可接订单</a>
        <a href="<%=request.getContextPath()%>/order/taken">我接的单</a>
        <a href="<%=request.getContextPath()%>/evaluation/my">我的评价</a>
        <a href="<%=request.getContextPath()%>/logout">退出</a>
        <a href="<%=request.getContextPath()%>/account/delete" onclick="return confirm('注销后账号及全部订单、评价将被永久删除,不可恢复!\n\n确认注销当前账号?')">注销账号</a>
    </div>
</div>

<div class="container">
    <h2>我收到的评价</h2>
    <%
        List<Evaluation> list = (List<Evaluation>) request.getAttribute("evaluations");
        if (list == null || list.isEmpty()) {
            out.println("<div class='empty'>还没有收到评价</div>");
        } else {
            for (Evaluation ev : list) {
                StringBuilder stars = new StringBuilder();
                for (int i = 0; i < ev.getScore(); i++) stars.append("★");
                for (int i = ev.getScore(); i < 5; i++) stars.append("☆");
    %>
    <div class="card">
        <div class="head">
            <span>订单号:<%=ev.getOrderNo()%> | 玩家:<%=ev.getPlayerNickname()%></span>
            <span class="stars"><%=stars.toString()%></span>
        </div>
        <div class="content"><%=ev.getContent() == null || ev.getContent().isEmpty() ? "该玩家没有填写评价内容" : ev.getContent()%></div>
        <div style="text-align:right;color:#999;font-size:12px;margin-top:8px;">
            <%=ev.getCreateTime() != null ? sdf.format(ev.getCreateTime()) : ""%>
        </div>
    </div>
    <%
            }
        }
    %>
</div>
</body>
</html>
