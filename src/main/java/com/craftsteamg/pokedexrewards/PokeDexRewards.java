package com.craftsteamg.pokedexrewards;

import com.craftsteamg.pokedexrewards.commands.DexClaim;
import com.craftsteamg.pokedexrewards.commands.DexList;
import com.craftsteamg.pokedexrewards.commands.DexRemaing;
import com.craftsteamg.pokedexrewards.config.DexConfig;
import com.craftsteamg.pokedexrewards.data.StorageManager;
import com.craftsteamg.pokedexrewards.listeners.ConnectionListener;
import com.craftsteamg.pokedexrewards.tasks.RemindTask;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Plugin(
        id = "simpledexrewards",
        name = "SimpleDexRewards",
        description = "Rewards for filling up the pokedex.",
        authors = {
                "CraftSteamG"
        },
        dependencies = {
                @Dependency(id = "pixelmon")
        }
)
public class PokeDexRewards {

    @Inject
    private Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private Path defaultConfig;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configManager;
    private CommentedConfigurationNode configNode;
    public static StorageManager storageManager;

    public static PokeDexRewards instance;

    @Inject
    public PokeDexRewards(PluginContainer container) {
        instance = this;
    }


    private static TypeToken<DexConfig> configTypeToken = TypeToken.of(DexConfig.class);

    public DexConfig getConfig() {
        return config;
    }

    private DexConfig config;


    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        this.logger.info("PokéDex Rewards by CraftSteamG.");
        Sponge.getEventManager().registerListeners(this, new ConnectionListener());

        this.logger.info("Loading Data");
        File file = new File(configDir.toString());
        file.mkdir();
        storageManager = new StorageManager(file);
        storageManager.load();


        this.logger.info("Loading Config");
        if(load()) {
            this.logger.info("Config Loaded Successfully!");
            registerCommands();

            Task.builder().interval((long) this.config.announcementTime, TimeUnit.MINUTES)
                    .execute(new RemindTask())
                    .submit(this);

        }else{
            this.logger.error("Error Loading Config! The plugin will not continue loading!");
        }
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        this.logger.info("Reloading Config");
        if(load()) {
            this.logger.info("Config Loaded Successfully!");
        }else{
            this.logger.error("Error Loading Config!");
        }
    }



    private void save() {
        try {
            this.configManager.save(this.configNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean load() {
        try {
            this.configNode = configManager.load();
            this.config = this.configNode.getValue(configTypeToken, new DexConfig());
            this.configNode.setValue(configTypeToken, this.config);
            save();
            return true;
        } catch (IOException | ObjectMappingException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void registerCommands() {

        CommandSpec dexListSpec = CommandSpec.builder()
                .permission("pokedexrewards.list.base")
                .executor(new DexList())
                .arguments(GenericArguments.none())
                .build();

        CommandSpec dexRemainingSpec = CommandSpec.builder()
                .permission("pokedexrewards.remaining.base")
                .executor(new DexRemaing())
                .arguments(GenericArguments.optional(GenericArguments.withSuggestions(GenericArguments.remainingJoinedStrings(Text.of("Filter")), Arrays.asList("legendary"))))
                .build();

        CommandSpec dexClaimSpec = CommandSpec.builder()
                .permission("pokedexrewards.claim.base")
                .executor(new DexClaim())
                .arguments(GenericArguments.withSuggestions(GenericArguments.remainingJoinedStrings(Text.of("TierName")), e -> this.config.tiers.stream().map(e1 -> e1.id).collect(Collectors.toList())))
                .build();

        CommandSpec baseSpec = CommandSpec.builder()
                .child(dexListSpec, "list", "l")
                .child(dexClaimSpec, "claim", "c")
                .child(dexRemainingSpec, "remaining", "r")
                .build();

        Sponge.getCommandManager().register(this, baseSpec, "pokedex", "dex");
    }


}
