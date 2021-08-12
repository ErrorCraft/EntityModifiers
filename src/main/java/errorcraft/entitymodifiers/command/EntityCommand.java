package errorcraft.entitymodifiers.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import errorcraft.entitymodifiers.entity.modifier.EntityModifier;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierManager;
import errorcraft.entitymodifiers.mixin.server.MinecraftServerAccessor;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class EntityCommand {
	private static final DynamicCommandExceptionType UNKNOWN_ENTITY_MODIFIER = new DynamicCommandExceptionType(object -> new TranslatableText("entity_modifier.unknown", object));
	private static final SuggestionProvider<ServerCommandSource> ENTITY_MODIFIER_SUGGESTION_PROVIDER = (context, builder) -> {
		MinecraftServerAccessor serverAccessor = (MinecraftServerAccessor)(context.getSource().getServer());
		EntityModifierManager entityModifierManager = serverAccessor.getEntityModifierManager();
		return CommandSource.suggestIdentifiers(entityModifierManager.getKeys(), builder);
	};

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("entity")
			.requires(source -> source.hasPermissionLevel(2))
			.then(literal("modify")
				.then(argument("targets", EntityArgumentType.entities())
					.then(argument("modifier", IdentifierArgumentType.identifier())
						.suggests(ENTITY_MODIFIER_SUGGESTION_PROVIDER)
						.executes((context) -> executeModify(context.getSource(), EntityArgumentType.getEntities(context, "targets"), getEntityModifier(context, "modifier")))
					)
				)
			)
		);
	}

	private static EntityModifier getEntityModifier(CommandContext<ServerCommandSource> context, String argumentName) throws CommandSyntaxException {
		Identifier resourceLocation = IdentifierArgumentType.getIdentifier(context, argumentName);

		MinecraftServerAccessor serverAccessor = (MinecraftServerAccessor)(context.getSource().getServer());
		EntityModifierManager entityModifierManager = serverAccessor.getEntityModifierManager();
		EntityModifier entityModifier = entityModifierManager.get(resourceLocation);

		if (entityModifier == null) {
			throw UNKNOWN_ENTITY_MODIFIER.create(resourceLocation);
		}
		return entityModifier;
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
