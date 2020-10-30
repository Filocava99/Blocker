package it.forgottenworld.fwblocker;

import it.forgottenworld.fwblocker.command.BlockCommand;
import it.forgottenworld.fwblocker.command.UnblockCommand;
import it.forgottenworld.fwblocker.config.Config;
import it.forgottenworld.fwblocker.listener.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public final class FWBlocker extends JavaPlugin {

    private Config config;

    @Override
    public void onEnable() {
        try {
            config = new Config("config.yml", this);
            Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
            Objects.requireNonNull(getCommand("block")).setExecutor(new BlockCommand(this));
            Objects.requireNonNull(getCommand("unblock")).setExecutor(new UnblockCommand(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    public Config getPluginConfig() {
        return config;
    }
}
