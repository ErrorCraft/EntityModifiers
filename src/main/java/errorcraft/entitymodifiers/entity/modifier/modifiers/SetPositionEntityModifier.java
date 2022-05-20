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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.JsonHelper;

public class SetPositionEntityModifier implements EntityModifier {
    private final LootNumberProvider x;
    private final LootNumberProvider y;
    private final LootNumberProvider z;

    public SetPositionEntityModifier(LootNumberProvider x, LootNumberProvider y, LootNumberProvider z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public EntityModifierType getType() {
        return EntityModifierTypes.SET_POSITION;
    }

    @Override
    public Entity apply(Entity entity, LootContext lootContext) {
        double newX = this.x.nextFloat(lootContext);
        double newY = this.y.nextFloat(lootContext);
        double newZ = this.z.nextFloat(lootContext);
        entity.setPosition(newX, newY, newZ);
        if (entity instanceof ServerPlayerEntity player) {
            setPlayerPosition(player, newX, newY, newZ);
        }
        return entity;
    }

    private static void setPlayerPosition(ServerPlayerEntity player, double x, double y, double z) {
        if (player.isSleeping()) {
            player.wakeUp(true, true);
        }
        player.requestTeleportAndDismount(x, y, z);
    }

    public static class Serialiser implements EntityModifier.Serialiser<SetPositionEntityModifier> {
        @Override
        public void toJson(JsonObject json, SetPositionEntityModifier object, JsonSerializationContext context) {
            json.add("x", context.serialize(object.x));
            json.add("y", context.serialize(object.y));
            json.add("z", context.serialize(object.z));
        }

        @Override
        public SetPositionEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
            LootNumberProvider x = JsonHelper.deserialize(json, "x", context, LootNumberProvider.class);
            LootNumberProvider y = JsonHelper.deserialize(json, "y", context, LootNumberProvider.class);
            LootNumberProvider z = JsonHelper.deserialize(json, "z", context, LootNumberProvider.class);
            return new SetPositionEntityModifier(x, y, z);
        }
    }
}
