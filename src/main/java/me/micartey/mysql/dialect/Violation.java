package me.micartey.mysql.dialect;

import me.clientastisch.cardinal.extension.impl.dialects.ViolationDialect;

import java.util.List;

public class Violation implements ViolationDialect {

    @Override
    public void addViolations(String uniqueId, List<String> violations) {

    }

    @Override
    public void removeViolations(String uniqueId) {

    }

    @Override
    public List<String> getViolations(String uniqueId) {
        return null;
    }

    @Override
    public List<String> getViolations() {
        return null;
    }
}
