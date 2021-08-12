package errorcraft.entitymodifiers.entity.modifier.modifiers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import errorcraft.entitymodifiers.entity.modifier.EntityModifier;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierType;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierTypes;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.JsonHelper;

public class SetInvulnerableEntityModifier implements EntityModifier {
	private final boolean invulnerable;

	public SetInvulnerableEntityModifier(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}

	@Override
	public EntityModifierType getType() {
		return EntityModifierTypes.SET_INVULNERABLE;
	}

	@Override
	public Entity apply(Entity entity, LootContext lootContext) {
		entity.setInvulnerable(this.invulnerable);
		return entity;
	}

	public static class Serialiser implements EntityModifier.Serialiser<SetInvulnerableEntityModifier> {
		@Override
		public void toJson(JsonObject json, SetInvulnerableEntityModifier object, JsonSerializationContext context) {
			json.addProperty("invulnerable", object.invulnerable);
		}

		@Override
		public SetInvulnerableEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
			boolean invulnerable = JsonHelper.getBoolean(json, "invulnerable");
			return new SetInvulnerableEntityModifier(invulnerable);
		}
	}
}
