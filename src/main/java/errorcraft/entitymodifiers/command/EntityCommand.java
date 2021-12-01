package errorcraft.entitymodifiers.command;

import com.mojang.brigadier.CommandDispatcher;
import errorcraft.entitymodifiers.entity.modifier.EntityModifier;
import errorcraft.entitymodifiers.util.CommandUtils;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class EntityCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("entity")
			.requires(source -> source.hasPermissionLevel(2))
			.then(literal("modify")
				.then(argument("targets", EntityArgumentType.entities())
					.then(argument("modifier", IdentifierArgumentType.identifier())
						.suggests(CommandUtils.ENTITY_MODIFIER_SUGGESTION_PROVIDER)
						.executes((context) -> executeModify(context.getSource(), EntityArgumentType.getEntities(context, "targets"), CommandUtils.getEntityModifier(context, "modifier")))
					)
				)
			)
		);
	}

	private static int executeModify(ServerCommandSource source, Collection<? extends Entity> targets, EntityModifier modifier) {
		ServerWorld world = source.getWorld();
		for (Entity entity : targets) {
			LootContext.Builder builder = new LootContext.Builder(world).parameter(LootContextParameters.ORIGIN, source.getPosition()).optionalParameter(LootContextParameters.THIS_ENTITY, entity);
			modifier.apply(entity, builder.build(LootContextTypes.COMMAND));
		}

		if (targets.size() == 1) {
			source.sendFeedback(new TranslatableText("commands.entity.modify.success.single", targets.iterator().next().getDisplayName()), true);
		} else {
			source.sendFeedback(new TranslatableText("commands.entity.modify.success.multiple", targets.size()), true);
		}

		return targets.size();
	}
}
