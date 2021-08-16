package errorcraft.entitymodifiers.entity.modifier.modifiers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import errorcraft.entitymodifiers.entity.modifier.EntityModifier;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierType;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.util.JsonHelper;

public class SetAbsorptionEntityModifier implements EntityModifier {
	private final LootNumberProvider absorptionProvider;
	private final boolean add;

	public SetAbsorptionEntityModifier(LootNumberProvider absorptionProvider, boolean add) {
		this.absorptionProvider = absorptionProvider;
		this.add = add;
	}

	@Override
	public EntityModifierType getType() {
		return EntityModifierTypes.SET_ABSORPTION;
	}

	@Override
	public Entity apply(Entity entity, LootContext lootContext) {
		if (entity instanceof LivingEntity livingEntity) {
			setAbsorption(livingEntity, lootContext);
		}
		return entity;
	}

	private void setAbsorption(LivingEntity livingEntity, LootContext lootContext) {
		float newAbsorption = this.add ? livingEntity.getAbsorptionAmount() : 0.0f;
		livingEntity.setAbsorptionAmount(newAbsorption + this.absorptionProvider.nextFloat(lootContext));
	}

	public static class Serialiser implements EntityModifier.Serialiser<SetAbsorptionEntityModifier> {
		@Override
		public void toJson(JsonObject json, SetAbsorptionEntityModifier object, JsonSerializationContext context) {
			json.add("absorption", context.serialize(object.absorptionProvider));
			json.addProperty("add", object.add);
		}

		@Override
		public SetAbsorptionEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
			LootNumberProvider absorptionProvider = JsonHelper.deserialize(json, "absorption", context, LootNumberProvider.class);
			boolean add = JsonHelper.getBoolean(json, "add", false);
			return new SetAbsorptionEntityModifier(absorptionProvider, add);
		}
	}
}
