package com.craftsteamg.pokedexrewards.tasks;

import com.craftsteamg.pokedexrewards.PokeDexRewards;
import com.craftsteamg.pokedexrewards.utils.DexUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;

public class RemindTask implements Runnable {

    @Override
    public void run() {
        if (PokeDexRewards.instance.getConfig().isAnnoucementEnabled) {
            for (Player player : Sponge.getServer().getOnlinePlayers()) {
                if (DexUtils.getAvailableDexTiers(player).size() >= 1) {
                    player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(PokeDexRewards.instance.getConfig().langConfig.reminderAnnouncement));
                }
            }
        }
    }

}
