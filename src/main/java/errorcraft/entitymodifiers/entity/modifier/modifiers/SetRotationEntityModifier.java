package errorcraft.entitymodifiers.entity.modifier.modifiers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import errorcraft.entitymodifiers.entity.modifier.EntityModifier;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierType;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierTypes;
import errorcraft.entitymodifiers.world.rotation.RotationProvider;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Vec2f;

public class SetRotationEntityModifier implements EntityModifier {
    private final RotationProvider rotation;

    public SetRotationEntityModifier(RotationProvider rotation) {
        this.rotation = rotation;
    }

    @Override
    public EntityModifierType getType() {
        return EntityModifierTypes.SET_ROTATION;
    }

    @Override
    public Entity apply(Entity entity, LootContext lootContext) {
        Vec2f newRotation = this.rotation.getRotation(entity.getRotationClient(), lootContext);
        entity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), newRotation.y, newRotation.x);
        entity.setHeadYaw(newRotation.y);
        if (entity instanceof ServerPlayerEntity player) {
            setPlayerRotation(player, newRotation);
        }
        return entity;
    }

    private static void setPlayerRotation(ServerPlayerEntity player, Vec2f rotation) {
        player.networkHandler.requestTeleport(player.getX(), player.getY(), player.getZ(), rotation.y, rotation.x);
    }

    public static class Serialiser implements EntityModifier.Serialiser<SetRotationEntityModifier> {
        @Override
        public void toJson(JsonObject json, SetRotationEntityModifier object, JsonSerializationContext context) {
            json.add("rotation", context.serialize(object.rotation));
        }

        @Override
        public SetRotationEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
            RotationProvider rotation = JsonHelper.deserialize(json, "rotation", context, RotationProvider.class);
            return new SetRotationEntityModifier(rotation);
        }
    }
}
