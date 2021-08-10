package errorcraft.entitymodifiers.entity.modifier;

import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;

import java.util.function.BiFunction;

public interface EntityModifier extends BiFunction<Entity, LootContext, Entity> {
	EntityModifierType getType();
}
