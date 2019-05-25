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
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextFormat;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.List;

public class DexList implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (!(src instanceof Player)) {
            throw new CommandException(Text.of("Only a player can execute this command!"));
        }

        Player player = (Player) src;

        PaginationService pagination = Sponge.getServiceManager().provideUnchecked(PaginationService.class);

        List<Text> dexTierText = new ArrayList<>();


        double percentage = DexUtils.getDexPercentage(player);
        for (DexTier availableDexTier : DexUtils.getAllDexTiers()) {
            dexTierText.add(getTextForTier(availableDexTier, percentage, DexUtils.getDataForPlayer(player)));
        }


        pagination.builder()
                .title(TextSerializers.FORMATTING_CODE.deserialize(PokeDexRewards.instance.getConfig().langConfig.paginationTitle))
                .linesPerPage(8)
                .padding(Text.of(TextColors.AQUA, "-"))
                .contents(dexTierText)
                .sendTo(player);

        return CommandResult.success();

    }


    private Text getTextForTier(DexTier tier, double currentPercentage, PlayerData playerData) {
        Text.Builder builder = Text.builder();

        if(playerData != null && playerData.claimedRewards.contains(tier.id)) {
            builder.append(Text.of(TextColors.GREEN, TextStyles.STRIKETHROUGH, tier.id))
                    .onHover(TextActions.showText(Text.of(Text.of(TextColors.AQUA, "Required Dex Percentage: " + tier.percentage + "%", Text.NEW_LINE, TextColors.RED, "You've already claimed this reward!"))));
        } else if (currentPercentage >= tier.percentage) {
            builder.append(Text.of(TextColors.GREEN, tier.id))
                    .onHover(TextActions.showText(Text.of(Text.of(TextColors.AQUA, "Required Dex Percentage: " + tier.percentage + "%", Text.NEW_LINE, TextColors.GREEN, "Click to Claim!"))))
                    .onClick(TextActions.runCommand("/dex claim " + tier.id));
        } else {
            builder.append(Text.of(TextColors.RED, tier.id))
                    .onHover(TextActions.showText(Text.of(Text.of(TextColors.AQUA, "Required Dex Percentage: " + tier.percentage + "%", Text.NEW_LINE, TextColors.RED, "You do not meet these requirements!"))));
        }

        return builder.build();

    }
}
