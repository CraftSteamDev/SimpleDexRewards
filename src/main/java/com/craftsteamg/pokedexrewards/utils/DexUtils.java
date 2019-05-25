package com.craftsteamg.pokedexrewards.utils;

import com.craftsteamg.pokedexrewards.PokeDexRewards;
import com.craftsteamg.pokedexrewards.config.DexTier;
import com.craftsteamg.pokedexrewards.data.PlayerData;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import org.spongepowered.api.entity.living.player.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DexUtils {


    public static double getDexPercentage(Player player) {
        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueId());

        int coughtCount = storage.pokedex.countCaught();
        return (double) coughtCount / (double) Pokedex.pokedexSize * 100;
    }

    public static List<DexTier> getAvailableDexTiers(Player player) {
        List<DexTier> tiers = new ArrayList<>();

        double percentage = getDexPercentage(player);
        for(DexTier dexTier : PokeDexRewards.instance.getConfig().tiers) {
            if(percentage >= dexTier.percentage) tiers.add(dexTier);
        }

        tiers.removeIf(e -> {
            PlayerData playerData = getDataForPlayer(player);
            return (playerData != null && playerData.claimedRewards.contains(e.id));
        });
        return tiers;
    }

    public static List<DexTier> getAllDexTiers() {
        return new ArrayList<>(PokeDexRewards.instance.getConfig().tiers);
    }


    @Nullable
    public static PlayerData getDataForPlayer(Player player) {
        return PokeDexRewards.storageManager.data.playerData.stream().filter(e -> e.uuid.equals(player.getUniqueId().toString()))
                .findAny().orElse(null);
    }

    public static PlayerData createDataForPlayer(Player player) {
        if(PokeDexRewards.storageManager.data.playerData.stream().anyMatch(e -> e.uuid.equals(player.getUniqueId().toString()))){
            return getDataForPlayer(player);
        }
        PlayerData playerData = new PlayerData(player.getUniqueId().toString(), Lists.newArrayList());
        PokeDexRewards.storageManager.data.playerData.add(playerData);
        PokeDexRewards.storageManager.save();
        return playerData;
    }


}
