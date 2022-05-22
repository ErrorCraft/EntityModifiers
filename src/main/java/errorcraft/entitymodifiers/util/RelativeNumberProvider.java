package errorcraft.entitymodifiers.util;

import com.google.gson.*;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.util.JsonHelper;

import java.lang.reflect.Type;

public class RelativeNumberProvider {
    private final LootNumberProvider value;
    private final boolean relative;

    public RelativeNumberProvider(LootNumberProvider value, boolean relative) {
        this.value = value;
        this.relative = relative;
    }

    public double getDouble(double currentValue, LootContext context) {
        if (this.relative) {
            return currentValue + this.value.nextFloat(context);
        }
        return this.value.nextFloat(context);
    }

    public float getFloat(float currentValue, LootContext context) {
        if (this.relative) {
            return currentValue + this.value.nextFloat(context);
        }
        return this.value.nextFloat(context);
    }

    public int getInt(int currentValue, LootContext context) {
        if (this.relative) {
            return currentValue + this.value.nextInt(context);
        }
        return this.value.nextInt(context);
    }

    public static class Serialiser implements JsonDeserializer<RelativeNumberProvider>, JsonSerializer<RelativeNumberProvider> {
        @Override
        public RelativeNumberProvider deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonPrimitive()) {
                return new RelativeNumberProvider(ConstantLootNumberProvider.create(json.getAsFloat()), false);
            }

            JsonObject jsonObject = JsonHelper.asObject(json, "value");
            LootNumberProvider value = JsonHelper.deserialize(jsonObject, "value", context, LootNumberProvider.class);
            boolean relative = JsonHelper.getBoolean(jsonObject, "relative", false);
            return new RelativeNumberProvider(value, relative);
        }

        @Override
        public JsonElement serialize(RelativeNumberProvider object, Type type, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            json.add("value", context.serialize(object.value));
            json.addProperty("relative", object.relative);
            return json;
        }
    }
}
