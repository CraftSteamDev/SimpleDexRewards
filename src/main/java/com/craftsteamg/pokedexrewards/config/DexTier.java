package com.craftsteamg.pokedexrewards.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class DexTier {

    public DexTier() {

    }

    public DexTier(String id, int percentage, List<String> commands) {
        this.id = id;
        this.percentage = percentage;
        this.commands = commands;
    }

    @Setting(comment = "ID of this tier, must be unique")
    public String id = "";

    @Setting(comment = "The percentage of completion at which this reward triggers.")
    public int percentage;

    @Setting(comment = "The commands that are run, %player% as a placeholder.")
    public List<String> commands = new ArrayList<>();


}
