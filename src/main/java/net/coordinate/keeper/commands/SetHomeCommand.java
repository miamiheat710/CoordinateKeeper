package net.coordinate.keeper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.coordinate.keeper.data.Config;
import net.coordinate.keeper.data.Coordinates;
import net.coordinate.keeper.data.NameConfig;
import net.coordinate.keeper.helpers.MessageHelper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public final class SetHomeCommand implements Command<ServerCommandSource> {
    private final String type;
    private Config config;
    private NameConfig nameConfig;


    public SetHomeCommand(Config config, NameConfig nameConfig, String type){
        this.config = config;
        this.nameConfig = nameConfig;
        this.type = type;
    }

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity user = context.getSource().getPlayer();
        BlockPos block = user.getBlockPos();
        Coordinates coordinates = new Coordinates(block.getX(), block.getY(), block.getZ());
        config.addCoordinates(user.getEntityName(), coordinates);
        MessageHelper.sendSpecialInfo(context, "Your " + type + " was set to ", coordinates.toString());
        if (!nameConfig.isUsernameStored(user.getEntityName())) {
            nameConfig.addName(user.getEntityName(), "");
        }
        return 1;
    }
}