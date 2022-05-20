package errorcraft.entitymodifiers.entity.modifier.modifiers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import errorcraft.entitymodifiers.entity.modifier.EntityModifier;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierType;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierTypes;
import errorcraft.entitymodifiers.world.position.provider.PositionProvider;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Vec3d;

public class SetPositionEntityModifier implements EntityModifier {
    private final PositionProvider position;

    public SetPositionEntityModifier(PositionProvider position) {
        this.position = position;
    }

    @Override
    public EntityModifierType getType() {
        return EntityModifierTypes.SET_POSITION;
    }

    @Override
    public Entity apply(Entity entity, LootContext lootContext) {
        Vec3d newPos = this.position.getPosition(entity.getPos(), entity.getRotationClient(), lootContext);
        entity.setPosition(newPos);
        if (entity instanceof ServerPlayerEntity player) {
            setPlayerPosition(player, newPos);
        }
        return entity;
    }

    private static void setPlayerPosition(ServerPlayerEntity player, Vec3d pos) {
        if (player.isSleeping()) {
            player.wakeUp(true, true);
        }
        player.requestTeleportAndDismount(pos.getX(), pos.getY(), pos.getZ());
    }

    public static class Serialiser implements EntityModifier.Serialiser<SetPositionEntityModifier> {
        @Override
        public void toJson(JsonObject json, SetPositionEntityModifier object, JsonSerializationContext context) {
            json.add("position", context.serialize(object.position));
        }

        @Override
        public SetPositionEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
            PositionProvider position = JsonHelper.deserialize(json, "position", context, PositionProvider.class);
            return new SetPositionEntityModifier(position);
        }
    }
}
