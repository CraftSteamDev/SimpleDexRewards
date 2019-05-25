package com.craftsteamg.pokedexrewards.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class LangConfig {

    @Setting(comment = "Title of the pagination list")
    public String paginationTitle = "&6Available DexTiers";

    @Setting(comment = "Announcement String")
    public String reminderAnnouncement = "&6You have a tier you're able to claim! Use /dex list to find out!";
}
