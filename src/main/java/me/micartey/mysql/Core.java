package me.micartey.mysql;

import lombok.SneakyThrows;
import me.clientastisch.cardinal.extension.Extension;
import me.clientastisch.cardinal.extension.impl.Addon;
import me.micartey.mysql.dialect.Punishment;
import me.micartey.mysql.dialect.Violation;
import me.micartey.mysql.driver.DatabaseDriver;

import java.sql.Connection;
import java.util.function.Consumer;

public class Core implements Addon {

    public static final DatabaseDriver DRIVER = new DatabaseDriver(
            Config.MARIA_HOST.get(),
            Config.MARIA_PORT.getInt(),
            Config.MARIA_DATABASE.get(),
            Config.MARIA_USERNAME.get(),
            Config.MARIA_PASSWORD.get()
    );

    @Override
    public void onEnable() {
        DRIVER.connect().onSuccess(new Consumer<Connection>() {
            @Override
            @SneakyThrows
            public void accept(Connection connection) {
                connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS punishments (uniqueId TINYTEXT, reason TINYTEXT, duration TINYTEXT)"
                ).executeUpdate();

                connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS violations (uniqueId TINYTEXT, violations LONGTEXT)"
                ).executeUpdate();

                Extension.registerDialect(Core.this, new Punishment());
                Extension.registerDialect(Core.this, new Violation());
            }
        }).onFailure(Throwable::printStackTrace);
    }

    @Override
    public void onDisable() {
        DRIVER.disconnect();
    }
}