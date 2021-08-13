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
import net.minecraft.util.math.MathHelper;

public class SetHungerEntityModifier implements EntityModifier {
	private final LootNumberProvider hungerProvider;
	private final boolean add;

	public SetHungerEntityModifier(LootNumberProvider hungerProvider, boolean add) {
		this.hungerProvider = hungerProvider;
		this.add = add;
	}

	@Override
	public EntityModifierType getType() {
		return EntityModifierTypes.SET_HUNGER;
	}

	@Override
	public Entity apply(Entity entity, LootContext lootContext) {
		if (entity instanceof PlayerEntity player) {
			setHunger(player, lootContext);
		}
		return entity;
	}

	private void setHunger(PlayerEntity player, LootContext lootContext) {
		HungerManager hungerManager = player.getHungerManager();
		int newHunger = this.add ? hungerManager.getFoodLevel() : 0;
		hungerManager.setFoodLevel(MathHelper.clamp(newHunger + this.hungerProvider.nextInt(lootContext), 0, 20));
	}

	public static class Serialiser implements EntityModifier.Serialiser<SetHungerEntityModifier> {
		@Override
		public void toJson(JsonObject json, SetHungerEntityModifier object, JsonSerializationContext context) {
			json.add("hunger", context.serialize(object.hungerProvider));
			json.addProperty("add", object.add);
		}

		@Override
		public SetHungerEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
			LootNumberProvider hungerProvider = JsonHelper.deserialize(json, "hunger", context, LootNumberProvider.class);
			boolean add = JsonHelper.getBoolean(json, "add", false);
			return new SetHungerEntityModifier(hungerProvider, add);
		}
	}
}
