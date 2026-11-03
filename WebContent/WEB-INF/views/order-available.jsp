<%@ page contentType="text/html;charset=UTF-8" import="java.util.List,com.peiwan.entity.User,com.peiwan.entity.GameOrder,java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>可接订单</title>
    <style>
        body { font-family: "Microsoft YaHei", sans-serif; background: #f5f5f5; margin: 0; }
        .header { background: #2196F3; color: white; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        .header a { color: white; text-decoration: none; margin-left: 15px; }
        .container { max-width: 1100px; margin: 20px auto; padding: 0 20px; }
        table { width: 100%; background: white; border-collapse: collapse; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #eee; }
        th { background: #2196F3; color: white; }
        .btn { background: #4CAF50; color: white; padding: 5px 12px; text-decoration: none; border-radius: 3px; font-size: 12px; }
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
    <h2>可接订单</h2>
    <%
        List<GameOrder> orders = (List<GameOrder>) request.getAttribute("orders");
        if (orders == null || orders.isEmpty()) {
            out.println("<div class='empty'>暂时没有新订单</div>");
        } else {
    %>
    <table>
        <tr>
            <th>订单号</th><th>玩家</th><th>游戏</th><th>时长</th><th>金额</th><th>下单时间</th><th>操作</th>
        </tr>
        <% for (GameOrder o : orders) { %>
        <tr>
            <td><%=o.getOrderNo()%></td>
            <td><%=o.getPlayerNickname()%></td>
            <td><%=o.getGame()%></td>
            <td><%=o.getHours()%> 小时</td>
            <td>¥<%=String.format("%.2f", o.getAmount())%></td>
            <td><%=o.getCreateTime() != null ? sdf.format(o.getCreateTime()) : "-"%></td>
            <td><a class="btn" href="<%=request.getContextPath()%>/order/take?orderId=<%=o.getId()%>" onclick="return confirm('确定接单吗?')">接单</a></td>
        </tr>
        <% } %>
    </table>
    <% } %>
</div>
</body>
</html>
