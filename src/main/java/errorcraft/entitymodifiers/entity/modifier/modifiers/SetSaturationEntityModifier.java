package errorcraft.entitymodifiers.entity.modifier.modifiers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import errorcraft.entitymodifiers.entity.modifier.EntityModifier;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierType;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.util.JsonHelper;

public class SetSaturationEntityModifier implements EntityModifier {
	private final LootNumberProvider saturationProvider;
	private final boolean add;

	public SetSaturationEntityModifier(LootNumberProvider saturationProvider, boolean add) {
		this.saturationProvider = saturationProvider;
		this.add = add;
	}

	@Override
	public EntityModifierType getType() {
		return EntityModifierTypes.SET_SATURATION;
	}

	@Override
	public Entity apply(Entity entity, LootContext lootContext) {
		if (entity instanceof PlayerEntity player) {
			setSaturation(player, lootContext);
		}
		return entity;
	}

	private void setSaturation(PlayerEntity player, LootContext lootContext) {
		HungerManager hungerManager = player.getHungerManager();
		float newSaturation = this.add ? hungerManager.getSaturationLevel() : 0.0f;
		hungerManager.setSaturationLevel(newSaturation + this.saturationProvider.nextFloat(lootContext));
	}

	public static class Serialiser implements EntityModifier.Serialiser<SetSaturationEntityModifier> {
		@Override
		public void toJson(JsonObject json, SetSaturationEntityModifier object, JsonSerializationContext context) {
			json.add("saturation", context.serialize(object.saturationProvider));
			json.addProperty("add", object.add);
		}

		@Override
		public SetSaturationEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
			LootNumberProvider saturationProvider = JsonHelper.deserialize(json, "saturation", context, LootNumberProvider.class);
			boolean add = JsonHelper.getBoolean(json, "add", false);
			return new SetSaturationEntityModifier(saturationProvider, add);
		}
	}
}
