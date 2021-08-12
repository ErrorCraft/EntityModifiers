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
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.MathHelper;

public class SetHungerEntityModifier implements EntityModifier {
	private final int hunger;
	private final boolean add;

	public SetHungerEntityModifier(int hunger, boolean add) {
		this.hunger = hunger;
		this.add = add;
	}

	@Override
	public EntityModifierType getType() {
		return EntityModifierTypes.SET_HUNGER;
	}

	@Override
	public Entity apply(Entity entity, LootContext lootContext) {
		if (entity instanceof PlayerEntity player) {
			setHunger(player);
		}
		return entity;
	}

	private void setHunger(PlayerEntity player) {
		HungerManager hungerManager = player.getHungerManager();
		int newHunger = this.add ? hungerManager.getFoodLevel() : 0;
		hungerManager.setFoodLevel(MathHelper.clamp(newHunger + this.hunger, 0, 20));
	}

	public static class Serialiser implements EntityModifier.Serialiser<SetHungerEntityModifier> {
		@Override
		public void toJson(JsonObject json, SetHungerEntityModifier object, JsonSerializationContext context) {
			json.addProperty("hunger", object.hunger);
			json.addProperty("add", object.add);
		}

		@Override
		public SetHungerEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
			int hunger = JsonHelper.getInt(json, "hunger");
			boolean add = JsonHelper.getBoolean(json, "add", false);
			return new SetHungerEntityModifier(hunger, add);
		}
	}
}
