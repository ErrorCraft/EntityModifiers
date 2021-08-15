package errorcraft.entitymodifiers.entity.modifier.modifiers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import errorcraft.entitymodifiers.entity.modifier.EntityModifier;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierType;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierTypes;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetNameLootFunction;
import net.minecraft.text.Text;

public class SetCustomNameEntityModifier implements EntityModifier {
	private final Text customName;

	public SetCustomNameEntityModifier(Text customName) {
		this.customName = customName;
	}

	@Override
	public EntityModifierType getType() {
		return EntityModifierTypes.SET_CUSTOM_NAME;
	}

	@Override
	public Entity apply(Entity entity, LootContext lootContext) {
		if (this.customName != null) {
			entity.setCustomName(SetNameLootFunction.applySourceEntity(lootContext, LootContext.EntityTarget.THIS).apply(this.customName));
		}
		return entity;
	}

	public static class Serialiser implements EntityModifier.Serialiser<SetCustomNameEntityModifier> {
		@Override
		public void toJson(JsonObject json, SetCustomNameEntityModifier object, JsonSerializationContext context) {
			if (object.customName != null) {
				json.add("name", Text.Serializer.toJsonTree(object.customName));
			}
		}

		@Override
		public SetCustomNameEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
			Text customName = Text.Serializer.fromJson(json.get("name"));
			return new SetCustomNameEntityModifier(customName);
		}
	}
}
