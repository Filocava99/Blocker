package it.forgottenworld.fwblocker;

import it.forgottenworld.fwblocker.command.BlockCommand;
import it.forgottenworld.fwblocker.command.UnblockCommand;
import it.forgottenworld.fwblocker.command.tabcompleters.BlockCommandTabCompleter;
import it.forgottenworld.fwblocker.command.tabcompleters.UnblockCommandTabCompleter;
import it.forgottenworld.fwblocker.config.Config;
import it.forgottenworld.fwblocker.listener.PlayerListener;
import it.forgottenworld.fwblocker.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public final class FWBlocker extends JavaPlugin {

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
