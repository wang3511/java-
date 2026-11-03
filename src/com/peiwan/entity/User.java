package com.peiwan.entity;

/**
 * 用户实体类:玩家和陪玩共用,用 role 区分
 */
public class User {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String role;      // PLAYER 玩家 / PEIWAN 陪玩
    private String game;      // 陪玩擅长的游戏
    private Double price;     // 陪玩每小时单价
    private Double balance;   // 余额

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getGame() { return game; }
    public void setGame(String game) { this.game = game; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }

    public boolean isPlayer() { return "PLAYER".equals(role); }
    public boolean isPeiwan() { return "PEIWAN".equals(role); }
}
