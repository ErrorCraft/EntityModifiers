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

public class SetAirTimeEntityModifier implements EntityModifier {
	private final LootNumberProvider airTimeProvider;
	private final boolean add;

	public SetAirTimeEntityModifier(LootNumberProvider airTimeProvider, boolean add) {
		this.airTimeProvider = airTimeProvider;
		this.add = add;
	}

	@Override
	public EntityModifierType getType() {
		return EntityModifierTypes.SET_AIR_TIME;
	}

	@Override
	public Entity apply(Entity entity, LootContext lootContext) {
		int newAirTime = this.add ? entity.getAir() : 0;
		entity.setAir(newAirTime + this.airTimeProvider.nextInt(lootContext));
		return entity;
	}

	public static class Serialiser implements EntityModifier.Serialiser<SetAirTimeEntityModifier> {
		@Override
		public void toJson(JsonObject json, SetAirTimeEntityModifier object, JsonSerializationContext context) {
			json.add("time", context.serialize(object.airTimeProvider));
			json.addProperty("add", object.add);
		}

		@Override
		public SetAirTimeEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
			LootNumberProvider airTimeProvider = JsonHelper.deserialize(json, "time", context, LootNumberProvider.class);
			boolean add = JsonHelper.getBoolean(json, "add", false);
			return new SetAirTimeEntityModifier(airTimeProvider, add);
		}
	}
}
