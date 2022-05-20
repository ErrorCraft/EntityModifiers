package errorcraft.entitymodifiers.world.position.provider;

import net.minecraft.loot.context.LootContext;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public interface PositionProvider {
    PositionProviderType getType();
    Vec3d getPosition(Vec3d currentPosition, Vec2f rotation, LootContext lootContext);

    interface Serialiser<T extends PositionProvider> extends JsonSerializer<T> {}
}
