package errorcraft.entitymodifiers.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import errorcraft.entitymodifiers.entity.modifier.EntityModifier;
import errorcraft.entitymodifiers.mixin.server.command.SummonCommandAccessor;
import errorcraft.entitymodifiers.util.CommandUtils;
import net.minecraft.command.argument.EntitySummonArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SummonCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("summon")
				.then(argument("entity", EntitySummonArgumentType.entitySummon())
						.then(argument("pos", Vec3ArgumentType.vec3())
								.then(argument("modifier", IdentifierArgumentType.identifier())
										.suggests(CommandUtils.ENTITY_MODIFIER_SUGGESTION_PROVIDER)
										.executes((context) -> summonWithModifier(context.getSource(), EntitySummonArgumentType.getEntitySummon(context, "entity"), Vec3ArgumentType.getVec3(context, "pos"), CommandUtils.getEntityModifier(context, "modifier")))
								)
						)
				)
		);
	}

	private static int summonWithModifier(ServerCommandSource source, Identifier entity, Vec3d pos, EntityModifier modifier) throws CommandSyntaxException {
		BlockPos blockPos = new BlockPos(pos);
		if (!World.isValid(blockPos)) {
			throw SummonCommandAccessor.getInvalidPositionException().create();
		}

		ServerWorld world = source.getWorld();
		Entity actualEntity = Registry.ENTITY_TYPE.get(entity).create(world);
		if (actualEntity == null) {
			throw SummonCommandAccessor.getFailedException().create();
		}

		actualEntity.refreshPositionAndAngles(pos.x, pos.y, pos.z, actualEntity.getYaw(), actualEntity.getPitch());
		LootContext.Builder builder = new LootContext.Builder(world).parameter(LootContextParameters.ORIGIN, source.getPosition()).parameter(LootContextParameters.THIS_ENTITY, actualEntity);
		modifier.apply(actualEntity, builder.build(LootContextTypes.COMMAND));
		if (!world.spawnEntity(actualEntity)) {
			throw SummonCommandAccessor.getFailedUUIDException().create();
		}

		source.sendFeedback(new TranslatableText("commands.summon.success", actualEntity.getDisplayName()), true);
		return 1;
	}
}
