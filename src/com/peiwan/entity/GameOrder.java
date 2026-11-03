package com.peiwan.entity;

import java.util.Date;

/**
 * 游戏陪玩订单
 * 状态流转: PENDING(待接单) -> TAKEN(已接单) -> FINISHED(已完成) -> EVALUATED(已评价) -> SETTLED(已结算)
 */
public class GameOrder {
    private Integer id;
    private String orderNo;
    private Integer playerId;
    private Integer peiwanId;
    private String game;
    private Integer hours;
    private Double amount;
    private String status;
    private Date createTime;
    private Date takeTime;
    private Date finishTime;

    // 关联显示用(非数据库字段)
    private String playerNickname;
    private String peiwanNickname;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public Integer getPlayerId() { return playerId; }
    public void setPlayerId(Integer playerId) { this.playerId = playerId; }

    public Integer getPeiwanId() { return peiwanId; }
    public void setPeiwanId(Integer peiwanId) { this.peiwanId = peiwanId; }

    public String getGame() { return game; }
    public void setGame(String game) { this.game = game; }

    public Integer getHours() { return hours; }
    public void setHours(Integer hours) { this.hours = hours; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getTakeTime() { return takeTime; }
    public void setTakeTime(Date takeTime) { this.takeTime = takeTime; }

    public Date getFinishTime() { return finishTime; }
    public void setFinishTime(Date finishTime) { this.finishTime = finishTime; }

    public String getPlayerNickname() { return playerNickname; }
    public void setPlayerNickname(String playerNickname) { this.playerNickname = playerNickname; }

    public String getPeiwanNickname() { return peiwanNickname; }
    public void setPeiwanNickname(String peiwanNickname) { this.peiwanNickname = peiwanNickname; }

    /** 中文状态名,方便 JSP 显示 */
    public String getStatusText() {
        if (status == null) return "";
        switch (status) {
            case "PENDING":   return "待接单";
            case "TAKEN":     return "已接单";
            case "FINISHED":  return "已完成";
            case "EVALUATED": return "已评价";
            case "SETTLED":   return "已结算";
            default:          return status;
        }
    }
}
