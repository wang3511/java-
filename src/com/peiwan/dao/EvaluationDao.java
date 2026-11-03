package com.peiwan.dao;

import com.peiwan.entity.Evaluation;
import com.peiwan.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EvaluationDao {

    /** 玩家提交评价 */
    public boolean insert(Evaluation ev) {
        String sql = "INSERT INTO evaluation(order_id, player_id, peiwan_id, score, content, create_time) VALUES(?,?,?,?,?,NOW())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ev.getOrderId());
            ps.setInt(2, ev.getPlayerId());
            ps.setInt(3, ev.getPeiwanId());
            ps.setInt(4, ev.getScore());
            ps.setString(5, ev.getContent());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 某订单是否已评价 */
    public Evaluation findByOrderId(Integer orderId) {
        String sql = "SELECT * FROM evaluation WHERE order_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 查询某个陪玩收到的所有评价 */
    public List<Evaluation> listByPeiwan(Integer peiwanId) {
        List<Evaluation> list = new ArrayList<>();
        String sql = "SELECT e.*, o.order_no, u.nickname AS player_nick " +
                "FROM evaluation e " +
                "LEFT JOIN game_order o ON e.order_id=o.id " +
                "LEFT JOIN user u ON e.player_id=u.id " +
                "WHERE e.peiwan_id=? ORDER BY e.id DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, peiwanId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Evaluation mapRow(ResultSet rs) throws SQLException {
        Evaluation ev = new Evaluation();
        ev.setId(rs.getInt("id"));
        ev.setOrderId(rs.getInt("order_id"));
        ev.setPlayerId(rs.getInt("player_id"));
        ev.setPeiwanId(rs.getInt("peiwan_id"));
        ev.setScore(rs.getInt("score"));
        ev.setContent(rs.getString("content"));
        Timestamp ct = rs.getTimestamp("create_time");
        if (ct != null) ev.setCreateTime(new java.util.Date(ct.getTime()));
        try {
            ev.setOrderNo(rs.getString("order_no"));
            ev.setPlayerNickname(rs.getString("player_nick"));
        } catch (SQLException ignored) {}
        return ev;
    }
}
