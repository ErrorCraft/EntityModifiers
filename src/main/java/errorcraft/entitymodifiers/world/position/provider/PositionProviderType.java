package errorcraft.entitymodifiers.world.position.provider;

import net.minecraft.util.JsonSerializableType;
import net.minecraft.util.JsonSerializer;

public class PositionProviderType extends JsonSerializableType<PositionProvider> {
    public PositionProviderType(JsonSerializer<? extends PositionProvider> jsonSerializer) {
        super(jsonSerializer);
    }
}
