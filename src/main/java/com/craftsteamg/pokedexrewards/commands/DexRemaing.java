package com.craftsteamg.pokedexrewards.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DexRemaing implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            throw new CommandException(Text.of("Only a player can execute this command!"));
        }

        Player player = (Player) src;
        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueId());
        String filter = args.<String>getOne(Text.of("Filter")).orElse("potato");
        List<EnumSpecies> species;
        if (filter.equalsIgnoreCase("legendary")) {
            species = EnumSpecies.legendaries.stream().map(EnumSpecies::getFromNameAnyCase).collect(Collectors.toList());
        } else {
           species = new ArrayList<>(Arrays.asList(EnumSpecies.values()));
        }
        species.removeIf(e -> storage.pokedex.hasCaught(e.getNationalPokedexInteger()));

        PaginationService service = Sponge.getServiceManager().provideUnchecked(PaginationService.class);

        service.builder()
                .contents(species.stream().map(e -> Text.of(TextColors.GREEN, e.name)).collect(Collectors.toList()))
                .padding(Text.of(Text.of(TextColors.AQUA, "-")))
                .title(TextSerializers.FORMATTING_CODE.deserialize("&6Remaining Pokemon"))
                .linesPerPage(8)
                .sendTo(player);


        return CommandResult.success();
    }

}
