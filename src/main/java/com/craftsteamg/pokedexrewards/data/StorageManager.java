package com.craftsteamg.pokedexrewards.data;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class StorageManager {

    private Gson gson;
    private File storageDir;
    private File dataFile;
    public DexData data;

    public StorageManager(File storageDir) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.storageDir = storageDir;

        this.dataFile = new File(storageDir, "data.json");
        if(!this.dataFile.exists()) {
            try {
                this.dataFile.createNewFile();
                this.data = new DexData();
                FileWriter fileWriter = new FileWriter(this.dataFile);
                fileWriter.write(gson.toJson(this.data));
                fileWriter.flush();
                fileWriter.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            load();
        }
    }

    public void load() {
        try {
            this.data = gson.fromJson(new FileReader(this.dataFile), DexData.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        File tempFile = new File(this.storageDir, "data-temp.json");
        try {
            if (!tempFile.exists()) tempFile.createNewFile();
            FileWriter writer = new FileWriter(tempFile);
            writer.write(gson.toJson(this.data));
            writer.flush();
            writer.close();
            Files.copy(tempFile, dataFile);
            tempFile.delete();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
