package errorcraft.entitymodifiers.entity.modifier.modifiers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import errorcraft.entitymodifiers.entity.modifier.EntityModifier;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierType;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierTypes;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.util.JsonHelper;

public class SetFireTimeEntityModifier implements EntityModifier {
	private final LootNumberProvider fireTimeProvider;
	private final boolean add;

	public SetFireTimeEntityModifier(LootNumberProvider fireTimeProvider, boolean add) {
		this.fireTimeProvider = fireTimeProvider;
		this.add = add;
	}

	@Override
	public EntityModifierType getType() {
		return EntityModifierTypes.SET_FIRE_TIME;
	}

	@Override
	public Entity apply(Entity entity, LootContext lootContext) {
		int newFireTime = this.add ? entity.getFireTicks() : 0;
		entity.setFireTicks(newFireTime + this.fireTimeProvider.nextInt(lootContext));
		return entity;
	}

	public static class Serialiser implements EntityModifier.Serialiser<SetFireTimeEntityModifier> {
		@Override
		public void toJson(JsonObject json, SetFireTimeEntityModifier object, JsonSerializationContext context) {
			json.add("time", context.serialize(object.fireTimeProvider));
			json.addProperty("add", object.add);
		}

		@Override
		public SetFireTimeEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
			LootNumberProvider fireTimeProvider = JsonHelper.deserialize(json, "time", context, LootNumberProvider.class);
			boolean add = JsonHelper.getBoolean(json, "add", false);
			return new SetFireTimeEntityModifier(fireTimeProvider, add);
		}
	}
}
