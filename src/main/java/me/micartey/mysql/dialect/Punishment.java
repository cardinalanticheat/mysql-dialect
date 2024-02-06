package me.micartey.mysql.dialect;

import lombok.SneakyThrows;
import me.clientastisch.cardinal.extension.impl.dialects.PunishDialect;
import me.micartey.mysql.Core;

import java.sql.ResultSet;

public class Punishment implements PunishDialect {

    @Override
    @SneakyThrows
    public void punish(String uniqueId, String reason, long minutes) {
        Core.DRIVER.getConnection().prepareStatement(
                String.format("INSERT INTO punishments (uniqueId, reason, duration) VALUES ('%s', '%s', '%s')", uniqueId, reason, minutes > 0 ? System.currentTimeMillis() + minutes * 60 * 1000 : 0)
        ).executeUpdate();
    }

    @Override
    @SneakyThrows
    public void pardon(String uniqueId) {
        Core.DRIVER.getConnection().prepareStatement(
                String.format("DELETE FROM punishments WHERE uniqueId='%s'", uniqueId)
        ).executeUpdate();
    }

    @Override
    @SneakyThrows
    public boolean isBanned(String uniqueId) {
        return Core.DRIVER.getConnection().prepareStatement(
                String.format("SELECT * FROM punishments WHERE uniqueId='%s'", uniqueId)
        ).executeQuery().next();
    }

    @Override
    @SneakyThrows
    public long getExpire(String uniqueId) {
        ResultSet result = Core.DRIVER.getConnection().prepareStatement(
                String.format("SELECT * FROM punishments WHERE uniqueId='%s'", uniqueId)
        ).executeQuery();

        return result.next() ? result.getLong("duration") : 0;
    }

    @Override
    @SneakyThrows
    public String getReason(String uniqueId) {
        ResultSet result = Core.DRIVER.getConnection().prepareStatement(
                String.format("SELECT * FROM punishments WHERE uniqueId='%s'", uniqueId)
        ).executeQuery();

        return result.next() ? result.getString("reason") : null;
    }
}
