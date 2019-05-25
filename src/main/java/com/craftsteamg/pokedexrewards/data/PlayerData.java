package com.craftsteamg.pokedexrewards.data;

import java.util.ArrayList;

public class PlayerData {

    public PlayerData() {

    }

    public PlayerData(String uuid, ArrayList<String> claimedRewards) {
        this.uuid = uuid;
        this.claimedRewards = claimedRewards;
    }

    public String uuid = "";
    public ArrayList<String> claimedRewards = new ArrayList<>();
}
