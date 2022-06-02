package errorcraft.entitymodifiers.world.position.provider.providers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import errorcraft.entitymodifiers.world.position.provider.PositionProvider;
import errorcraft.entitymodifiers.world.position.provider.PositionProviderType;
import errorcraft.entitymodifiers.world.position.provider.PositionProviderTypes;
import net.minecraft.command.argument.LookingPosArgument;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class LocalPositionProvider implements PositionProvider {
    private final LootNumberProvider left;
    private final LootNumberProvider up;
    private final LootNumberProvider forwards;

    public LocalPositionProvider(LootNumberProvider left, LootNumberProvider up, LootNumberProvider forwards) {
        this.left = left;
        this.up = up;
        this.forwards = forwards;
    }

    @Override
    public PositionProviderType getType() {
        return PositionProviderTypes.LOCAL;
    }

    @Override
    public Vec3d getPosition(Vec3d currentPosition, Vec2f rotation, LootContext lootContext) {
        double leftOffset = this.left.nextFloat(lootContext);
        double upOffset = this.up.nextFloat(lootContext);
        double forwardsOffset = this.forwards.nextFloat(lootContext);
        LookingPosArgument argument = new LookingPosArgument(leftOffset, upOffset, forwardsOffset);
        ServerCommandSource commandSource = new ServerCommandSource(null, currentPosition, rotation, null, 0, null, null, null, null);
        return argument.toAbsolutePos(commandSource);
    }

    public static class Serialiser implements PositionProvider.Serialiser<LocalPositionProvider> {
        @Override
        public void toJson(JsonObject json, LocalPositionProvider object, JsonSerializationContext context) {
            json.add("left", context.serialize(object.left));
            json.add("up", context.serialize(object.up));
            json.add("forwards", context.serialize(object.forwards));
        }

        @Override
        public LocalPositionProvider fromJson(JsonObject json, JsonDeserializationContext context) {
            LootNumberProvider left = JsonHelper.deserialize(json, "left", context, LootNumberProvider.class);
            LootNumberProvider up = JsonHelper.deserialize(json, "up", context, LootNumberProvider.class);
            LootNumberProvider forwards = JsonHelper.deserialize(json, "forwards", context, LootNumberProvider.class);
            return new LocalPositionProvider(left, up, forwards);
        }
    }
}
