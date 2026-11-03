<%@ page contentType="text/html;charset=UTF-8" import="com.peiwan.entity.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>下单</title>
    <style>
        body { font-family: "Microsoft YaHei", sans-serif; background: #f5f5f5; }
        .box { width: 500px; margin: 50px auto; background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        h2 { color: #333; border-bottom: 2px solid #4CAF50; padding-bottom: 10px; }
        .info { background: #f9f9f9; padding: 15px; border-radius: 4px; margin: 15px 0; }
        .info p { margin: 8px 0; color: #555; }
        .row { margin: 15px 0; }
        label { display: block; margin-bottom: 5px; color: #666; }
        input[type=text], input[type=number] { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; padding: 12px; background: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
        .error { color: red; }
        .back { display: inline-block; margin-top: 10px; color: #2196F3; text-decoration: none; }
        .total { color: #f44336; font-weight: bold; font-size: 18px; }
    </style>
    <script>
        function calc() {
            var price = parseFloat(document.getElementById('price').value);
            var hours = parseInt(document.getElementById('hours').value) || 0;
            document.getElementById('total').innerText = '¥' + (price * hours).toFixed(2);
        }
    </script>
</head>
<body>
<%
    User peiwan = (User) request.getAttribute("peiwan");
    String error = (String) request.getAttribute("error");
%>
<div class="box">
    <h2>下单陪玩</h2>
    <% if (error != null) out.println("<p class='error'>" + error + "</p>"); %>

    <div class="info">
        <p>陪玩:<b><%=peiwan.getNickname() != null ? peiwan.getNickname() : peiwan.getUsername()%></b></p>
        <p>擅长游戏:<%=peiwan.getGame()%></p>
        <p>单价:¥<%=String.format("%.2f", peiwan.getPrice())%> / 小时</p>
    </div>

    <form method="post" action="<%=request.getContextPath()%>/order/place">
        <input type="hidden" name="peiwanId" value="<%=peiwan.getId()%>">
        <input type="hidden" id="price" value="<%=peiwan.getPrice()%>">

        <div class="row">
            <label>游戏名称</label>
            <input type="text" name="game" value="<%=peiwan.getGame()%>" required>
        </div>
        <div class="row">
            <label>陪玩时长(小时)</label>
            <input type="number" id="hours" name="hours" min="1" max="24" value="1" onchange="calc()" oninput="calc()" required>
        </div>
        <div class="row">
            <label>订单金额:<span id="total" class="total">¥<%=String.format("%.2f", peiwan.getPrice())%></span></label>
        </div>
        <button type="submit">提交订单</button>
    </form>
    <a class="back" href="<%=request.getContextPath()%>/peiwan/list">← 返回列表</a>
</div>
</body>
</html>
