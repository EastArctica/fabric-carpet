package carpet.commands;

import carpet.CarpetSettings;
import carpet.utils.CommandHelper;
import carpet.utils.DistanceCalculator;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class DistanceCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext)
    {
        LiteralArgumentBuilder<CommandSourceStack> command = literal("distance").
                requires((player) -> CommandHelper.canUseCommand(player, CarpetSettings.commandDistance) && Permissions.check(player, "carpet.command.distance", true)).
                then(literal("from").
                        executes( (c) -> DistanceCalculator.setStart(c.getSource(), c.getSource().getPosition())).
                        then(argument("from", Vec3Argument.vec3()).
                                executes( (c) -> DistanceCalculator.setStart(
                                        c.getSource(),
                                        Vec3Argument.getVec3(c, "from"))).
                                then(literal("to").
                                        executes((c) -> DistanceCalculator.distance(
                                                c.getSource(),
                                                Vec3Argument.getVec3(c, "from"),
                                                c.getSource().getPosition())).
                                        then(argument("to", Vec3Argument.vec3()).
                                                executes( (c) -> DistanceCalculator.distance(
                                                        c.getSource(),
                                                        Vec3Argument.getVec3(c, "from"),
                                                        Vec3Argument.getVec3(c, "to")
                                                )))))).
                then(literal("to").
                        executes( (c) -> DistanceCalculator.setEnd(c.getSource(), c.getSource().getPosition()) ).
                        then(argument("to", Vec3Argument.vec3()).
                                executes( (c) -> DistanceCalculator.setEnd(c.getSource(), Vec3Argument.getVec3(c, "to")))));
        dispatcher.register(command);
    }
}
