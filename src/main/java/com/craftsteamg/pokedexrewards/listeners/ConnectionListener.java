package com.craftsteamg.pokedexrewards.listeners;

import com.craftsteamg.pokedexrewards.utils.DexUtils;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class ConnectionListener {


    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        DexUtils.createDataForPlayer(event.getTargetEntity());
    }
}
