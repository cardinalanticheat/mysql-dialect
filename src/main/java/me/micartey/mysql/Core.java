package me.micartey.mysql;

import io.vavr.control.Try;
import me.clientastisch.cardinal.extension.Extension;
import me.clientastisch.cardinal.extension.impl.Addon;
import me.micartey.mysql.dialect.Punishment;
import me.micartey.mysql.dialect.Violation;
import me.micartey.mysql.driver.DatabaseDriver;

public class Core implements Addon {

    public static final DatabaseDriver DRIVER = new DatabaseDriver(
            Config.MARIA_HOST.get(),
            Config.MARIA_PORT.get(),
            Config.MARIA_DATABASE.get(),
            Config.MARIA_USERNAME.get(),
            Config.MARIA_PASSWORD.get()
    );

    @Override
    public void onEnable() throws Exception {
        DRIVER.connect();

        DRIVER.getConnection().prepareStatement(
                "CREATE TABLE IF NOT EXISTS punishments (uniqueId TINYTEXT, reason TINYTEXT, duration TINYTEXT)"
        ).executeUpdate();

        DRIVER.getConnection().prepareStatement(
                "CREATE TABLE IF NOT EXISTS violations (uniqueId TINYTEXT, violations LONGTEXT)"
        ).executeUpdate();

        Extension.registerDialect(this, new Punishment());
        Extension.registerDialect(this, new Violation());
    }

    @Override
    public void onDisable() {
        DRIVER.disconnect();
    }
}