<%@ page contentType="text/html;charset=UTF-8" import="com.peiwan.entity.GameOrder" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>评价订单</title>
    <style>
        body { font-family: "Microsoft YaHei", sans-serif; background: #f5f5f5; }
        .box { width: 500px; margin: 50px auto; background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        h2 { color: #333; border-bottom: 2px solid #FF5722; padding-bottom: 10px; }
        .info { background: #f9f9f9; padding: 15px; border-radius: 4px; margin: 15px 0; }
        .row { margin: 15px 0; }
        label { display: block; margin-bottom: 8px; color: #666; }
        .stars input { display: none; }
        .stars label { display: inline-block; font-size: 30px; color: #ddd; cursor: pointer; margin: 0 5px; }
        .stars input:checked ~ label, .stars label:hover, .stars label:hover ~ label { color: #FFC107; }
        .stars { direction: rtl; text-align: left; }
        textarea { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; min-height: 100px; font-family: inherit; }
        button { width: 100%; padding: 12px; background: #FF5722; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
        .back { display: inline-block; margin-top: 10px; color: #2196F3; text-decoration: none; }
    </style>
</head>
<body>
<%
    GameOrder order = (GameOrder) request.getAttribute("order");
%>
<div class="box">
    <h2>评价订单</h2>
    <div class="info">
        <p>订单号:<%=order.getOrderNo()%></p>
        <p>陪玩:<b><%=order.getPeiwanNickname()%></b></p>
        <p>游戏:<%=order.getGame()%> | 时长:<%=order.getHours()%> 小时 | 金额:¥<%=String.format("%.2f", order.getAmount())%></p>
    </div>

    <form method="post" action="<%=request.getContextPath()%>/order/evaluate">
        <input type="hidden" name="orderId" value="<%=order.getId()%>">
        <div class="row">
            <label>评分</label>
            <div class="stars">
                <input type="radio" id="s5" name="score" value="5" checked><label for="s5">★</label>
                <input type="radio" id="s4" name="score" value="4"><label for="s4">★</label>
                <input type="radio" id="s3" name="score" value="3"><label for="s3">★</label>
                <input type="radio" id="s2" name="score" value="2"><label for="s2">★</label>
                <input type="radio" id="s1" name="score" value="1"><label for="s1">★</label>
            </div>
        </div>
        <div class="row">
            <label>评价内容</label>
            <textarea name="content" placeholder="说说这次陪玩的体验..."></textarea>
        </div>
        <button type="submit">提交评价</button>
    </form>
    <a class="back" href="<%=request.getContextPath()%>/order/my">← 返回订单</a>
</div>
</body>
</html>
