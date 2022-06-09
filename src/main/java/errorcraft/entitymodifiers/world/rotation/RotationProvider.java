package errorcraft.entitymodifiers.world.rotation;

import com.google.gson.*;
import errorcraft.entitymodifiers.util.RelativeNumberProvider;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Vec2f;

import java.lang.reflect.Type;

public class RotationProvider {
    private final RelativeNumberProvider x;
    private final RelativeNumberProvider y;

    public RotationProvider(RelativeNumberProvider x, RelativeNumberProvider y) {
        this.x = x;
        this.y = y;
    }

    public Vec2f getRotation(Vec2f currentRotation, LootContext lootContext) {
        float newX = this.x.getFloat(currentRotation.x, lootContext);
        float newY = this.y.getFloat(currentRotation.y, lootContext);
        return new Vec2f(newX, newY);
    }

    public static class Serialiser implements JsonDeserializer<RotationProvider>, JsonSerializer<RotationProvider> {
        @Override
        public RotationProvider deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = JsonHelper.asObject(json, "value");
            RelativeNumberProvider x = JsonHelper.deserialize(jsonObject, "x", context, RelativeNumberProvider.class);
            RelativeNumberProvider y = JsonHelper.deserialize(jsonObject, "y", context, RelativeNumberProvider.class);
            return new RotationProvider(x, y);
        }

        @Override
        public JsonElement serialize(RotationProvider object, Type type, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            json.add("x", context.serialize(object.x));
            json.add("y", context.serialize(object.y));
            return json;
        }
    }
}
