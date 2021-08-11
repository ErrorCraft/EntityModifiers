package errorcraft.entitymodifiers.entity.modifier;

import errorcraft.entitymodifiers.mixin.registry.RegistryAccessor;
import net.minecraft.util.JsonSerializing;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class EntityModifierTypes {
	public static final RegistryKey<Registry<EntityModifierType>> ENTITY_MODIFIER_TYPE_KEY = RegistryAccessor.createRegistryKey("entity_modifier_type");
	public static final Registry<EntityModifierType> ENTITY_MODIFIER_TYPE = RegistryAccessor.create(ENTITY_MODIFIER_TYPE_KEY, () -> null);

	public static Object createGsonAdapter() {
		return JsonSerializing.createSerializerBuilder(ENTITY_MODIFIER_TYPE, "function", "function", EntityModifier::getType).build();
	}
}
