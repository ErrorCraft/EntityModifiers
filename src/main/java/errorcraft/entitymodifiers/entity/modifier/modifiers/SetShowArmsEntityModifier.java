package errorcraft.entitymodifiers.entity.modifier.modifiers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import errorcraft.entitymodifiers.entity.modifier.EntityModifier;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierType;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierTypes;
import errorcraft.entitymodifiers.mixin.entities.ArmorStandEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.JsonHelper;

public class SetShowArmsEntityModifier implements EntityModifier {
	private final boolean show_arms;

	public SetShowArmsEntityModifier(boolean show_arms) {
		this.show_arms = show_arms;
	}

	@Override
	public EntityModifierType getType() {
		return EntityModifierTypes.SET_SHOW_ARMS;
	}

	@Override
	public Entity apply(Entity entity, LootContext lootContext) {
		if (entity instanceof ArmorStandEntity armorStand) {
			((ArmorStandEntityAccessor)armorStand).invokeSetShowArms(show_arms);
		}

		System.out.println(entity.toString());

		return entity;
	}

	public static class Serialiser implements EntityModifier.Serialiser<SetShowArmsEntityModifier> {
		@Override
		public void toJson(JsonObject json, SetShowArmsEntityModifier object, JsonSerializationContext context) {
			json.addProperty("show_arms", object.show_arms);
		}

		@Override
		public SetShowArmsEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
			boolean show_arms = JsonHelper.getBoolean(json, "show_arms");
			return new SetShowArmsEntityModifier(show_arms);
		}
	}
}
