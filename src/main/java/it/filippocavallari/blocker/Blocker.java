package it.filippocavallari.blocker;

import it.filippocavallari.blocker.command.BlockCommand;
import it.filippocavallari.blocker.command.UnblockCommand;
import it.filippocavallari.blocker.command.tabcompleters.BlockCommandTabCompleter;
import it.filippocavallari.blocker.command.tabcompleters.UnblockCommandTabCompleter;
import it.filippocavallari.blocker.config.Config;
import it.filippocavallari.blocker.listener.PlayerListener;
import it.filippocavallari.blocker.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public final class Blocker extends JavaPlugin {

    private Config config;

    @Override
    public void onEnable() {
        loadConfigs();
        registerListeners();
        registerCommands();
    }

    @NotNull
    public Config getPluginConfig() {
        return config;
    }

    private void registerCommands(){
        Objects.requireNonNull(getCommand("block")).setExecutor(new BlockCommand(this));
        Objects.requireNonNull(getCommand("unblock")).setExecutor(new UnblockCommand(this));
        Objects.requireNonNull(getCommand("block")).setTabCompleter(new BlockCommandTabCompleter());
        Objects.requireNonNull(getCommand("unblock")).setTabCompleter(new UnblockCommandTabCompleter(new Utils(this)));
    }

    private void registerListeners(){
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void loadConfigs(){
        try {
            config = new Config("config.yml", this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
