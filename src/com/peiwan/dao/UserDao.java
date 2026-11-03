package com.peiwan.dao;

import com.peiwan.entity.User;
import com.peiwan.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    /** 根据用户名密码查询,用于登录 */
    public User findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM user WHERE username=? AND password=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 根据用户名查询(注册时判断是否重名) */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findById(Integer id) {
        String sql = "SELECT * FROM user WHERE id=?";
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

    /** 列出所有陪玩(玩家首页用) */
    public List<User> listAllPeiwan() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user WHERE role='PEIWAN' ORDER BY price ASC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 注册新用户,成功返回 true */
    public boolean insert(User u) {
        String sql = "INSERT INTO user(username, password, nickname, role, game, price, balance) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getNickname());
            ps.setString(4, u.getRole());
            ps.setString(5, u.getGame());
            ps.setDouble(6, u.getPrice() == null ? 0 : u.getPrice());
            ps.setDouble(7, u.getBalance() == null ? 0 : u.getBalance());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 修改余额(结算用) */
    public void addBalance(Integer userId, double delta) {
        String sql = "UPDATE user SET balance = balance + ? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, delta);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** 注销账号:先删该账号相关的评价和订单,再删账号,全过程在一个事务里 */
    public boolean deleteAccount(Integer userId) {
        String delEval = "DELETE FROM evaluation WHERE player_id=? OR peiwan_id=?";
        String delOrder = "DELETE FROM game_order WHERE player_id=? OR peiwan_id=?";
        String delUser = "DELETE FROM user WHERE id=?";
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(delEval)) {
                ps.setInt(1, userId);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(delOrder)) {
                ps.setInt(1, userId);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }
            boolean ok;
            try (PreparedStatement ps = conn.prepareStatement(delUser)) {
                ps.setInt(1, userId);
                ok = ps.executeUpdate() == 1;
            }
            conn.commit();
            return ok;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setNickname(rs.getString("nickname"));
        u.setRole(rs.getString("role"));
        u.setGame(rs.getString("game"));
        u.setPrice(rs.getDouble("price"));
        u.setBalance(rs.getDouble("balance"));
        return u;
    }
}
