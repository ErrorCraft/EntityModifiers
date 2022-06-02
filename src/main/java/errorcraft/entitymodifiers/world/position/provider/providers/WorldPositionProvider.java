package errorcraft.entitymodifiers.world.position.provider.providers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import errorcraft.entitymodifiers.util.RelativeNumberProvider;
import errorcraft.entitymodifiers.world.position.provider.PositionProvider;
import errorcraft.entitymodifiers.world.position.provider.PositionProviderType;
import errorcraft.entitymodifiers.world.position.provider.PositionProviderTypes;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class WorldPositionProvider implements PositionProvider {
    private final RelativeNumberProvider x;
    private final RelativeNumberProvider y;
    private final RelativeNumberProvider z;

    public WorldPositionProvider(RelativeNumberProvider x, RelativeNumberProvider y, RelativeNumberProvider z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PositionProviderType getType() {
        return PositionProviderTypes.WORLD;
    }

    @Override
    public Vec3d getPosition(Vec3d currentPosition, Vec2f rotation, LootContext lootContext) {
        double newX = this.x.getDouble(currentPosition.getX(), lootContext);
        double newY = this.y.getDouble(currentPosition.getY(), lootContext);
        double newZ = this.z.getDouble(currentPosition.getZ(), lootContext);
        return new Vec3d(newX, newY, newZ);
    }

    public static class Serialiser implements PositionProvider.Serialiser<WorldPositionProvider> {
        @Override
        public void toJson(JsonObject json, WorldPositionProvider object, JsonSerializationContext context) {
            json.add("x", context.serialize(object.x));
            json.add("y", context.serialize(object.y));
            json.add("z", context.serialize(object.z));
        }

        @Override
        public WorldPositionProvider fromJson(JsonObject json, JsonDeserializationContext context) {
            RelativeNumberProvider x = JsonHelper.deserialize(json, "x", context, RelativeNumberProvider.class);
            RelativeNumberProvider y = JsonHelper.deserialize(json, "y", context, RelativeNumberProvider.class);
            RelativeNumberProvider z = JsonHelper.deserialize(json, "z", context, RelativeNumberProvider.class);
            return new WorldPositionProvider(x, y, z);
        }
    }
}
