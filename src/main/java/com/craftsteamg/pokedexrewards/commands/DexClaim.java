package com.craftsteamg.pokedexrewards.commands;

import com.craftsteamg.pokedexrewards.PokeDexRewards;
import com.craftsteamg.pokedexrewards.config.DexTier;
import com.craftsteamg.pokedexrewards.data.PlayerData;
import com.craftsteamg.pokedexrewards.utils.DexUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class DexClaim implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!(src instanceof Player)) {
            throw new CommandException(Text.of("Only a player can execute this command!"));
        }

        Player player = (Player) src;
        String tierName = args.<String>getOne(Text.of("TierName")).get();

        if (PokeDexRewards.instance.getConfig().tiers.stream().noneMatch(e -> e.id.equals(tierName))) {
            throw new CommandException(Text.of("No Such Tier Name!"));
        }

        PlayerData data = DexUtils.getDataForPlayer(player);

        if (data == null) {
            throw new CommandException(Text.of("Error Fetching Data!"));
        }

        DexTier tier = PokeDexRewards.instance.getConfig().tiers.stream().filter(e -> e.id.equals(tierName)).findFirst().get();

        if (data.claimedRewards.contains(tier.id)) {
            throw new CommandException(Text.of("You've already claimed that DexTier!"));
        }

        if (DexUtils.getDexPercentage(player) < tier.percentage) {
            throw new CommandException(Text.of("You do not meet the requirements to claim that DexTier!"));
        }


        player.sendMessage(Text.of(TextSerializers.FORMATTING_CODE.deserialize("&aYou've redeemed the '" + tier.id + "' DexTier!")));
        data.claimedRewards.add(tier.id);
        PokeDexRewards.storageManager.save();

        for (String command : tier.commands) {
            Sponge.getCommandManager().process((CommandSource) Sponge.getServer(), command.replace("%player%", player.getName()));
        }

        return CommandResult.success();


    }

}
