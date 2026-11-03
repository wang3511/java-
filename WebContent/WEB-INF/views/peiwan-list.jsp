<%@ page contentType="text/html;charset=UTF-8" import="java.util.List,com.peiwan.entity.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>陪玩列表</title>
    <style>
        body { font-family: "Microsoft YaHei", sans-serif; background: #f5f5f5; margin: 0; }
        .header { background: #4CAF50; color: white; padding: 15px 30px; display: flex; justify-content: space-between; align-items: center; }
        .header a { color: white; text-decoration: none; margin-left: 15px; }
        .container { max-width: 1000px; margin: 20px auto; padding: 0 20px; }
        .card { background: white; padding: 20px; margin-bottom: 15px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); display: flex; justify-content: space-between; align-items: center; }
        .info h3 { margin: 0 0 8px 0; color: #333; }
        .info p { margin: 4px 0; color: #666; }
        .price { color: #f44336; font-size: 20px; font-weight: bold; }
        .btn { background: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px; }
        .btn:hover { background: #45a049; }
        .empty { text-align: center; color: #999; padding: 50px; }
    </style>
</head>
<body>
<%
    User loginUser = (User) session.getAttribute("loginUser");
%>
<div class="header">
    <div>欢迎,<b><%=loginUser.getNickname() != null ? loginUser.getNickname() : loginUser.getUsername()%></b>(玩家) | 余额:¥<%=String.format("%.2f", loginUser.getBalance())%></div>
    <div>
        <a href="<%=request.getContextPath()%>/peiwan/list">陪玩列表</a>
        <a href="<%=request.getContextPath()%>/order/my">我的订单</a>
        <a href="<%=request.getContextPath()%>/logout">退出</a>
        <a href="<%=request.getContextPath()%>/account/delete" onclick="return confirm('注销后账号及全部订单、评价将被永久删除,不可恢复!\n\n确认注销当前账号?')">注销账号</a>
    </div>
</div>

<div class="container">
    <h2>选择你喜欢的陪玩</h2>
    <%
        List<User> peiwanList = (List<User>) request.getAttribute("peiwanList");
        if (peiwanList == null || peiwanList.isEmpty()) {
            out.println("<div class='empty'>暂无陪玩,请稍后再来</div>");
        } else {
            for (User pw : peiwanList) {
    %>
    <div class="card">
        <div class="info">
            <h3><%=pw.getNickname() != null ? pw.getNickname() : pw.getUsername()%></h3>
            <p>擅长游戏:<b><%=pw.getGame() == null ? "-" : pw.getGame()%></b></p>
            <p class="price">¥<%=String.format("%.2f", pw.getPrice())%> / 小时</p>
        </div>
        <a class="btn" href="<%=request.getContextPath()%>/order/place?peiwanId=<%=pw.getId()%>">立即下单</a>
    </div>
    <%
            }
        }
    %>
</div>
</body>
</html>
