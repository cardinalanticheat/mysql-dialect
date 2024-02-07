package me.micartey.mysql.dialect;

import lombok.SneakyThrows;
import me.clientastisch.cardinal.extension.impl.dialects.ViolationDialect;
import me.micartey.mysql.Core;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Violation implements ViolationDialect {

    @Override
    @SneakyThrows
    public void addViolations(String uniqueId, List<String> violations) {
        List<String> currentViolations = this.getViolations(uniqueId);
        currentViolations.addAll(violations);

        String rep = Arrays.toString(currentViolations.toArray());
        String violations2String = rep.substring(1, rep.length() - 1);

        // Deleting makes the "updating" easy
        this.removeViolations(uniqueId);

        Core.DRIVER.getConnection().prepareStatement(
                String.format("INSERT INTO violations (uniqueId, violations) VALUES ('%s', '%s')", uniqueId, violations2String)
        ).executeUpdate();
    }

    @Override
    @SneakyThrows
    public void removeViolations(String uniqueId) {
        Core.DRIVER.getConnection().prepareStatement(
                String.format("DELETE FROM violations WHERE uniqueId='%s'", uniqueId)
        ).executeUpdate();
    }

    @Override
    @SneakyThrows
    public List<String> getViolations(String uniqueId) {
        ResultSet result = Core.DRIVER.getConnection().prepareStatement(
                String.format("SELECT * FROM violations WHERE uniqueId='%s'", uniqueId)
        ).executeQuery();

        return result.next() ? Arrays.asList(result.getString("violations").split(", ")) : new ArrayList<>();
    }

    @Override
    @SneakyThrows
    public List<String> getViolations() {
        ResultSet result = Core.DRIVER.getConnection().prepareStatement(
                String.format("SELECT uniqueId FROM violations")
        ).executeQuery();

        List<String> uniqueIds = new ArrayList<>();

        while (result.next())
            uniqueIds.add(result.getString("uniqueId"));

        return uniqueIds;
    }
}
