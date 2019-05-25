package com.craftsteamg.pokedexrewards.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigSerializable
public class DexConfig {


    @Setting(comment = "Should the plugin regularly remind people to claim rewards?")
    public boolean isAnnoucementEnabled = true;

    @Setting(comment = "How long should the annoucement run? (Time in mins)")
    public double announcementTime = 2;

    @Setting(comment = "List of Tiers")
    public List<DexTier> tiers = new ArrayList<>(Collections.singletonList(new DexTier("diamonds", 10, Collections.singletonList("give %player% minecraft:diamond 5"))));

    @Setting(comment = "Lang Configuration")
    public LangConfig langConfig = new LangConfig();

}
