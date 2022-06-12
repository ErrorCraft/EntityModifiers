package errorcraft.entitymodifiers.util;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import errorcraft.entitymodifiers.access.server.MinecraftServerExtenderAccess;
import errorcraft.entitymodifiers.entity.modifier.EntityModifier;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CommandUtils {
	private static final DynamicCommandExceptionType UNKNOWN_ENTITY_MODIFIER = new DynamicCommandExceptionType(object -> Text.translatable("entity_modifier.unknown", object));
	public static final SuggestionProvider<ServerCommandSource> ENTITY_MODIFIER_SUGGESTION_PROVIDER = (context, builder) -> {
		MinecraftServerExtenderAccess serverExtenderAccess = (MinecraftServerExtenderAccess)(context.getSource().getServer());
		EntityModifierManager entityModifierManager = serverExtenderAccess.getEntityModifierManager();
		return CommandSource.suggestIdentifiers(entityModifierManager.getKeys(), builder);
	};

	public static EntityModifier getEntityModifier(CommandContext<ServerCommandSource> context, String argumentName) throws CommandSyntaxException {
		Identifier resourceLocation = IdentifierArgumentType.getIdentifier(context, argumentName);

		MinecraftServerExtenderAccess serverExtenderAccess = (MinecraftServerExtenderAccess)(context.getSource().getServer());
		EntityModifierManager entityModifierManager = serverExtenderAccess.getEntityModifierManager();
		EntityModifier entityModifier = entityModifierManager.get(resourceLocation);

		if (entityModifier == null) {
			throw UNKNOWN_ENTITY_MODIFIER.create(resourceLocation);
		}
		return entityModifier;
	}
}
