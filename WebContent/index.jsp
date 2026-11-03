<%@ page contentType="text/html;charset=UTF-8" %>
<%-- 入口页:已登录去对应首页,未登录去登录页 --%>
<%
    com.peiwan.entity.User loginUser = (com.peiwan.entity.User) session.getAttribute("loginUser");
    if (loginUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
    } else if (loginUser.isPlayer()) {
        response.sendRedirect(request.getContextPath() + "/peiwan/list");
    } else {
        response.sendRedirect(request.getContextPath() + "/order/available");
    }
%>
