package me.micartey.mysql.driver;

import io.vavr.control.Try;
import lombok.Data;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseDriver {

    private final String host, port, database, username, password;
    @Getter private Connection connection;

    public DatabaseDriver(String host, int port, String database, String username, String password) {
        this.database = database;
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = String.valueOf(port);
    }

    public Try<Connection> connect() {
        return Try.ofCallable(() -> {
            DatabaseDriver.class.getClassLoader().loadClass("com.mysql.jdbc.Driver").newInstance(); // TODO: Do I really need this?
            return this.connection = DriverManager.getConnection(
                    String.format("jdbc:mysql://%s/%s?autoReconnect=true", host + ":" + port, database),
                    username,
                    password
            );
        });
    }

    public boolean disconnect() {
        return Try.ofCallable(() -> {
            connection.close();
            return null;
        }).isSuccess();
    }
}