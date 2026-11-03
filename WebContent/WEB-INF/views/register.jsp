<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>注册 - 游戏陪玩平台</title>
    <style>
        body { font-family: "Microsoft YaHei", sans-serif; background: #f5f5f5; }
        .box { width: 400px; margin: 60px auto; background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        h2 { text-align: center; color: #333; }
        .row { margin: 12px 0; }
        label { display: block; margin-bottom: 5px; color: #666; }
        input[type=text], input[type=password], input[type=number], select { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        button { width: 100%; padding: 10px; background: #2196F3; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
        .error { color: red; text-align: center; }
        .link { text-align: center; margin-top: 15px; }
        .link a { color: #2196F3; text-decoration: none; }
        #peiwanFields { background: #f9f9f9; padding: 10px; border-radius: 4px; }
    </style>
    <script>
        function togglePeiwan() {
            var role = document.querySelector('input[name="role"]:checked').value;
            document.getElementById('peiwanFields').style.display = (role === 'PEIWAN') ? 'block' : 'none';
        }
    </script>
</head>
<body>
<div class="box">
    <h2>注册账号</h2>
    <%
        String error = (String) request.getAttribute("error");
        if (error != null) out.println("<p class='error'>" + error + "</p>");
    %>
    <form method="post" action="<%=request.getContextPath()%>/register">
        <div class="row">
            <label>用户名 *</label>
            <input type="text" name="username" required>
        </div>
        <div class="row">
            <label>密码 *</label>
            <input type="password" name="password" required>
        </div>
        <div class="row">
            <label>昵称</label>
            <input type="text" name="nickname">
        </div>
        <div class="row">
            <label>我是 *</label>
            <input type="radio" name="role" value="PLAYER" checked onclick="togglePeiwan()"> 玩家
            &nbsp;&nbsp;
            <input type="radio" name="role" value="PEIWAN" onclick="togglePeiwan()"> 陪玩
        </div>
        <div id="peiwanFields" style="display:none;">
            <div class="row">
                <label>擅长游戏</label>
                <input type="text" name="game" placeholder="例如:王者荣耀">
            </div>
            <div class="row">
                <label>每小时单价(元)</label>
                <input type="number" name="price" min="0" step="0.01" value="30">
            </div>
        </div>
        <button type="submit">注 册</button>
    </form>
    <div class="link">
        已有账号?<a href="<%=request.getContextPath()%>/login">去登录</a>
    </div>
</div>
</body>
</html>
