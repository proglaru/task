package org.example.dto;

import java.util.List;
import java.util.Map;

public class Player {
    private long playerId;
    private String nickname;
    private List<Progress> progresses;
    private Map<Long, Currency> currencies;
    private Map<Long, Item> items;

    public Player(long playerId, String nickname, List<Progress> progresses, Map<Long, Currency> currencies, Map<Long, Item> items) {
        this.playerId = playerId;
        this.nickname = nickname;
        this.progresses = progresses;
        this.currencies = currencies;
        this.items = items;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<Progress> getProgresses() {
        return progresses;
    }

    public void setProgresses(List<Progress> progresses) {
        this.progresses = progresses;
    }

    public Map<Long, Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<Long, Currency> currencies) {
        this.currencies = currencies;
    }

    public Map<Long, Item> getItems() {
        return items;
    }

    public void setItems(Map<Long, Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", nickname='" + nickname + '\'' +
                ", progresses=" + progresses +
                ", currencies=" + currencies +
                ", items=" + items +
                '}';
    }
}
