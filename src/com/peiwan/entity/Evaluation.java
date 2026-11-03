package com.peiwan.entity;

import java.util.Date;

/**
 * 订单评价
 */
public class Evaluation {
    private Integer id;
    private Integer orderId;
    private Integer playerId;
    private Integer peiwanId;
    private Integer score;    // 1~5 星
    private String content;
    private Date createTime;

    // 显示用
    private String orderNo;
    private String playerNickname;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getPlayerId() { return playerId; }
    public void setPlayerId(Integer playerId) { this.playerId = playerId; }

    public Integer getPeiwanId() { return peiwanId; }
    public void setPeiwanId(Integer peiwanId) { this.peiwanId = peiwanId; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public String getPlayerNickname() { return playerNickname; }
    public void setPlayerNickname(String playerNickname) { this.playerNickname = playerNickname; }
}
