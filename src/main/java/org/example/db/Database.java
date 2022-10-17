package org.example.db;

import org.example.dto.Currency;
import org.example.dto.Item;
import org.example.dto.Player;
import org.example.dto.Progress;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static Connection conn = null;

    public static void connect() {
        try {
            String url = "jdbc:sqlite:db/players.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTables() {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS player (
                	id integer PRIMARY KEY,
                	nickname text
                );""");
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS progress (
                    id integer PRIMARY KEY,
                    playerId integer,
                	resourceId integer,
                	score integer,
                	maxScore integer
                );""");
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS currency (
                    id integer PRIMARY KEY,
                    playerId integer,
                	resourceId integer,
                	name text,
                	count integer
                );""");
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS item (
                    id integer PRIMARY KEY,
                    playerId integer,
                	resourceId integer,
                	count integer,
                	level integer
                );""");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearTables() {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("DELETE FROM player;");
            stmt.execute("DELETE FROM progress;");
            stmt.execute("DELETE FROM currency;");
            stmt.execute("DELETE FROM item;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insert(List<Player> players) {
        players.forEach(player -> {
            insertPlayer(player);
            player.getProgresses().forEach(Database::insertProgress);
            player.getCurrencies().forEach((key, value) -> Database.insertCurrency(player, value));
            player.getItems().forEach((key, value) -> Database.insertItem(player, value));
        });
    }

    public static List<Player> select() {
        List<Player> players = new ArrayList<>();
        try {
            String sql = "SELECT id, nickname from player;";
            Statement stmt = conn.createStatement();
            ResultSet playerRs = stmt.executeQuery(sql);
            while (playerRs.next()) {
                long playerId = playerRs.getInt("id");
                sql = "SELECT id, resourceId, score, maxScore from progress WHERE playerId = ?;";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setLong(1, playerId);
                ResultSet rs = pstmt.executeQuery();
                List<Progress> progresses = new ArrayList<>();
                while (rs.next()) {
                    progresses.add(new Progress(
                            rs.getLong("id"),
                            playerId,
                            rs.getLong("resourceId"),
                            rs.getInt("score"),
                            rs.getInt("maxScore")
                    ));
                }
                sql = "SELECT id, resourceId, name, count from currency WHERE playerId = ?;";
                pstmt = conn.prepareStatement(sql);
                pstmt.setLong(1, playerId);
                rs = pstmt.executeQuery();
                Map<Long, Currency> currencies = new HashMap<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    currencies.put(id, new Currency(
                            id,
                            rs.getLong("resourceId"),
                            rs.getString("name"),
                            rs.getInt("count")
                    ));
                }
                sql = "SELECT id, resourceId, count, level from item WHERE playerId = ?;";
                pstmt = conn.prepareStatement(sql);
                pstmt.setLong(1, playerId);
                rs = pstmt.executeQuery();
                Map<Long, Item> items = new HashMap<>();
                while (rs.next()) {
                    long id = rs.getLong("id");
                    items.put(id, new Item(
                            id,
                            rs.getLong("resourceId"),
                            rs.getInt("count"),
                            rs.getInt("level")
                    ));
                }
                players.add(
                        new Player(playerId, playerRs.getString("nickname"), progresses, currencies, items)
                );
            }
            return players;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void insertProgress(Progress progress) {
        try {
            String sql = "INSERT INTO progress(id,playerId,resourceId,score,maxScore) VALUES(?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, progress.getId());
            pstmt.setLong(2, progress.getPlayerId());
            pstmt.setLong(3, progress.getResourceId());
            pstmt.setInt(4, progress.getScore());
            pstmt.setInt(5, progress.getMaxScore());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertCurrency(Player player, Currency currency) {
        try {
            String sql = "INSERT INTO currency(id,playerId,resourceId,name,count) VALUES(?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, currency.getId());
            pstmt.setLong(2, player.getPlayerId());
            pstmt.setLong(3, currency.getResourceId());
            pstmt.setString(4, currency.getName());
            pstmt.setInt(5, currency.getCount());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertItem(Player player, Item item) {
        try {
            String sql = "INSERT INTO item(id,playerId,resourceId,count,level) VALUES(?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, item.getId());
            pstmt.setLong(2, player.getPlayerId());
            pstmt.setLong(3, item.getResourceId());
            pstmt.setInt(4, item.getCount());
            pstmt.setInt(5, item.getLevel());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertPlayer(Player player) {
        try {
            String sql = "INSERT INTO player(id,nickname) VALUES(?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, player.getPlayerId());
            pstmt.setString(2, player.getNickname());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
