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
import net.minecraft.util.JsonHelper;

public class SetHealthEntityModifier implements EntityModifier {
	private final float health;
	private final boolean add;

	public SetHealthEntityModifier(float health, boolean add) {
		this.health = health;
		this.add = add;
	}

	@Override
	public EntityModifierType getType() {
		return EntityModifierTypes.SET_HEALTH;
	}

	@Override
	public Entity apply(Entity entity, LootContext lootContext) {
		if (entity instanceof LivingEntity livingEntity) {
			setHealth(livingEntity);
		}
		return entity;
	}

	private void setHealth(LivingEntity livingEntity) {
		float newHealth = this.add ? livingEntity.getHealth() : 0.0f;
		livingEntity.setHealth(newHealth + this.health);
	}

	public static class Serialiser implements EntityModifier.Serialiser<SetHealthEntityModifier> {
		@Override
		public void toJson(JsonObject json, SetHealthEntityModifier object, JsonSerializationContext context) {
			json.addProperty("health", object.health);
			json.addProperty("add", object.add);
		}

		@Override
		public SetHealthEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
			float health = JsonHelper.getFloat(json, "health");
			boolean add = JsonHelper.getBoolean(json, "add", false);
			return new SetHealthEntityModifier(health, add);
		}
	}
}
