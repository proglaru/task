package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.example.db.Database;
import org.example.dto.Player;

import java.io.*;
import java.lang.reflect.Type;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            Database.connect();
            Database.createTables();
            System.out.println("Выберите действие:");
            System.out.println("1) загрузить кэш в базу данных");
            System.out.println("2) выгрузить кэш из базы данных");
            System.out.println("3) выйти");
            Scanner scanner = new Scanner(System.in);
            int inputNumber = 0;
            while (inputNumber != 1 && inputNumber != 2 && inputNumber != 3) {
                try {
                    inputNumber = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException ignored) {}
            }
            switch (inputNumber) {
                case 1 -> {
                    System.out.println("Введите название json-файла (например, cash.json):");
                    String path = scanner.nextLine();
                    List<Player> players = read(path);
                    if (players == null) {
                        continue;
                    }
                    System.out.println("Сколько игроков загрузить (1-" + players.size() + ")?");
                    int input = 0;
                    while (input < 1 || input > players.size()) {
                        try {
                            input = Integer.parseInt(scanner.nextLine());
                        } catch (InputMismatchException ignored) {}
                    }
                    Database.clearTables();
                    Database.insert(players.subList(0, input));
                    System.out.println("Кэш загружен в базу данных из cash/" + path);
                }
                case 2 -> {
                    System.out.println("Введите название json-файла (например, cash.json):");
                    String path = scanner.nextLine();
                    write(path, Database.select());
                    System.out.println("Кэш выгружен из базы данных в cash/" + path);
                }
                case 3 -> {
                    Database.disconnect();
                    System.exit(0);
                }
            }
        }
    }

    public static List<Player> read(String path) {
        Type REVIEW_TYPE = new TypeToken<List<Player>>() {}.getType();
        Gson gson = new Gson();
        try {
            JsonReader reader = new JsonReader(new FileReader("cash/" + path));
            return gson.fromJson(reader, REVIEW_TYPE);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
            return null;
        }
    }

    public static void write(String path, List<Player> players) {
        try (Writer writer = new FileWriter("cash/" + path)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(players, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}