<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>登录 - 游戏陪玩平台</title>
    <style>
        body { font-family: "Microsoft YaHei", sans-serif; background: #f5f5f5; }
        .box { width: 360px; margin: 100px auto; background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #333; }
        .row { margin: 15px 0; }
        label { display: block; margin-bottom: 5px; color: #666; }
        input[type=text], input[type=password] { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; padding: 10px; background: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
        button:hover { background: #45a049; }
        .error { color: red; text-align: center; }
        .msg { color: green; text-align: center; }
        .link { text-align: center; margin-top: 15px; }
        .link a { color: #2196F3; text-decoration: none; }
        .warn { color: #d32f2f; font-size: 12px; line-height: 1.6; margin: 0 0 10px; }
        .danger { background: #f44336; }
        .danger:hover { background: #e53935; }
        .panel { display: none; margin-top: 15px; padding-top: 15px; border-top: 1px dashed #ddd; }
    </style>
</head>
<body>
<div class="box">
    <h2>游戏陪玩平台</h2>
    <%
        String error = (String) request.getAttribute("error");
        String msg = (String) request.getAttribute("msg");
        if (error != null) { out.println("<p class='error'>" + error + "</p>"); }
        if (msg != null)   { out.println("<p class='msg'>"   + msg   + "</p>"); }
        if (request.getParameter("deleted") != null)   { out.println("<p class='msg'>账号已注销,该账号及其订单、评价已全部删除</p>"); }
        if (request.getParameter("deleteErr") != null) { out.println("<p class='error'>注销失败:用户名或密码不正确</p>"); }
    %>
    <form method="post" action="<%=request.getContextPath()%>/login">
        <div class="row">
            <label>用户名</label>
            <input type="text" name="username" required>
        </div>
        <div class="row">
            <label>密码</label>
            <input type="password" name="password" required>
        </div>
        <button type="submit">登 录</button>
    </form>
    <div class="link">
        还没有账号?<a href="<%=request.getContextPath()%>/register">立即注册</a>
        | <a href="#" id="toggleDelete">注销账号</a>
    </div>
    <div class="panel" id="deletePanel">
        <p class="warn">警告:注销将永久删除该账号及其全部订单、评价数据,删除后无法恢复,且用户名可被重新注册。</p>
        <form method="post" action="<%=request.getContextPath()%>/account/delete"
              onsubmit="return confirm('确定要注销该账号吗?账号及全部订单、评价将被永久删除,不可恢复!')">
            <div class="row">
                <label>要注销的用户名</label>
                <input type="text" name="username" required>
            </div>
            <div class="row">
                <label>密码(用于验证身份)</label>
                <input type="password" name="password" required>
            </div>
            <button type="submit" class="danger">确认注销</button>
        </form>
    </div>
    <div class="link" style="color:#999;font-size:12px;margin-top:10px;">
        测试账号:玩家 player/123 | 陪玩 pw01/123
    </div>
</div>
<script>
    document.getElementById('toggleDelete').onclick = function (e) {
        e.preventDefault();
        var panel = document.getElementById('deletePanel');
        panel.style.display = (panel.style.display === 'block') ? 'none' : 'block';
    };
</script>
</body>
</html>
