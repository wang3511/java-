package com.peiwan.dao;

import com.peiwan.entity.GameOrder;
import com.peiwan.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    /** 玩家下单 */
    public int insert(GameOrder o) {
        String sql = "INSERT INTO game_order(order_no, player_id, peiwan_id, game, hours, amount, status, create_time) VALUES(?,?,?,?,?,?,?,NOW())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, o.getOrderNo());
            ps.setInt(2, o.getPlayerId());
            ps.setInt(3, o.getPeiwanId());
            ps.setString(4, o.getGame());
            ps.setInt(5, o.getHours());
            ps.setDouble(6, o.getAmount());
            ps.setString(7, o.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /** 陪玩接单:PENDING -> TAKEN */
    public boolean takeOrder(Integer orderId) {
        String sql = "UPDATE game_order SET status='TAKEN', take_time=NOW() WHERE id=? AND status='PENDING'";
        return updateById(sql, orderId);
    }

    /** 陪玩完成订单:TAKEN -> FINISHED */
    public boolean finishOrder(Integer orderId) {
        String sql = "UPDATE game_order SET status='FINISHED', finish_time=NOW() WHERE id=? AND status='TAKEN'";
        return updateById(sql, orderId);
    }

    /** 玩家评价后:FINISHED -> EVALUATED */
    public boolean markEvaluated(Integer orderId) {
        String sql = "UPDATE game_order SET status='EVALUATED' WHERE id=? AND status='FINISHED'";
        return updateById(sql, orderId);
    }

    /** 玩家撤销订单:仅允许删除自己名下还没被接单的订单 */
    public boolean cancel(Integer orderId, Integer playerId) {
        String sql = "DELETE FROM game_order WHERE id=? AND player_id=? AND status='PENDING'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, playerId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 结算:EVALUATED -> SETTLED */
    public boolean settle(Integer orderId) {
        String sql = "UPDATE game_order SET status='SETTLED' WHERE id=? AND status='EVALUATED'";
        return updateById(sql, orderId);
    }

    private boolean updateById(String sql, Integer id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public GameOrder findById(Integer id) {
        String sql = "SELECT o.*, p.nickname AS player_nick, w.nickname AS peiwan_nick " +
                "FROM game_order o " +
                "LEFT JOIN user p ON o.player_id=p.id " +
                "LEFT JOIN user w ON o.peiwan_id=w.id WHERE o.id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 玩家查看自己的订单 */
    public List<GameOrder> listByPlayer(Integer playerId) {
        String sql = "SELECT o.*, p.nickname AS player_nick, w.nickname AS peiwan_nick " +
                "FROM game_order o " +
                "LEFT JOIN user p ON o.player_id=p.id " +
                "LEFT JOIN user w ON o.peiwan_id=w.id " +
                "WHERE o.player_id=? ORDER BY o.id DESC";
        return queryList(sql, playerId);
    }

    /** 陪玩查看自己接的订单 */
    public List<GameOrder> listByPeiwan(Integer peiwanId) {
        String sql = "SELECT o.*, p.nickname AS player_nick, w.nickname AS peiwan_nick " +
                "FROM game_order o " +
                "LEFT JOIN user p ON o.player_id=p.id " +
                "LEFT JOIN user w ON o.peiwan_id=w.id " +
                "WHERE o.peiwan_id=? AND o.status<>'PENDING' ORDER BY o.id DESC";
        return queryList(sql, peiwanId);
    }

    /** 陪玩查看可接的订单(所有人下的、待接单的、指定给当前陪玩的) */
    public List<GameOrder> listAvailableFor(Integer peiwanId) {
        String sql = "SELECT o.*, p.nickname AS player_nick, w.nickname AS peiwan_nick " +
                "FROM game_order o " +
                "LEFT JOIN user p ON o.player_id=p.id " +
                "LEFT JOIN user w ON o.peiwan_id=w.id " +
                "WHERE o.status='PENDING' AND o.peiwan_id=? ORDER BY o.id DESC";
        return queryList(sql, peiwanId);
    }

    private List<GameOrder> queryList(String sql, Integer arg) {
        List<GameOrder> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, arg);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private GameOrder mapRow(ResultSet rs) throws SQLException {
        GameOrder o = new GameOrder();
        o.setId(rs.getInt("id"));
        o.setOrderNo(rs.getString("order_no"));
        o.setPlayerId(rs.getInt("player_id"));
        o.setPeiwanId(rs.getInt("peiwan_id"));
        o.setGame(rs.getString("game"));
        o.setHours(rs.getInt("hours"));
        o.setAmount(rs.getDouble("amount"));
        o.setStatus(rs.getString("status"));
        Timestamp ct = rs.getTimestamp("create_time");
        if (ct != null) o.setCreateTime(new java.util.Date(ct.getTime()));
        Timestamp tt = rs.getTimestamp("take_time");
        if (tt != null) o.setTakeTime(new java.util.Date(tt.getTime()));
        Timestamp ft = rs.getTimestamp("finish_time");
        if (ft != null) o.setFinishTime(new java.util.Date(ft.getTime()));
        try {
            o.setPlayerNickname(rs.getString("player_nick"));
            o.setPeiwanNickname(rs.getString("peiwan_nick"));
        } catch (SQLException ignored) {}
        return o;
    }
}
