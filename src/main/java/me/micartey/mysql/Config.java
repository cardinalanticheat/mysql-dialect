package me.micartey.mysql;

import io.vavr.control.Try;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public enum Config {

    MARIA_HOST("host", "localhost"),
    MARIA_PORT("port", 3306),
    MARIA_USERNAME("username", "root"),
    MARIA_PASSWORD("password", "password"),
    MARIA_DATABASE("database", "database")

    ;

    private static final File CONFIG_FILE = new File("plugins//CAC//MySQL.yml");

    private YamlConfiguration yaml;

    private Object object;

    private final Object defaultObject;
    private final String data;

    Config(String data, Object object) {
        this.defaultObject = object;
        this.data = data;
        createFile();
        load();
    }

    @SneakyThrows
    private void createFile() {
        if (CONFIG_FILE.getParentFile().exists() && CONFIG_FILE.exists())
            return;

        CONFIG_FILE.getParentFile().mkdir();
        CONFIG_FILE.createNewFile();
    }

    private void load() {
        yaml = YamlConfiguration.loadConfiguration(CONFIG_FILE);

        Try.ofCallable(() -> {
            if (yaml.get(data) == null) {
                yaml.set(data, this.defaultObject);
                yaml.save(CONFIG_FILE);
            }
            return null;
        }).onFailure(var -> this.object = this.defaultObject).onSuccess(var -> this.object = yaml.get(data));
    }

    public <T> T get() {
        return (T) (object == null ? defaultObject : object);
    }
}