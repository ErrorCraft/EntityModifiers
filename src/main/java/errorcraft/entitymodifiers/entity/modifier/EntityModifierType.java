package errorcraft.entitymodifiers.entity.modifier;

import net.minecraft.util.JsonSerializableType;
import net.minecraft.util.JsonSerializer;

public class EntityModifierType extends JsonSerializableType<EntityModifier> {
	public EntityModifierType(JsonSerializer<? extends EntityModifier> jsonSerializer) {
		super(jsonSerializer);
	}
}
