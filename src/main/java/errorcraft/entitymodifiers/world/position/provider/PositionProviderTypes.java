package errorcraft.entitymodifiers.world.position.provider;

import errorcraft.entitymodifiers.mixin.registry.RegistryAccessor;
import errorcraft.entitymodifiers.world.position.provider.providers.WorldPositionProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializing;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class PositionProviderTypes {
    public static final RegistryKey<Registry<PositionProviderType>> POSITION_PROVIDER_TYPE_KEY = RegistryAccessor.createRegistryKey("position_provider_type");
    public static final Registry<PositionProviderType> POSITION_PROVIDER_TYPE = RegistryAccessor.create(POSITION_PROVIDER_TYPE_KEY, registry -> PositionProviderTypes.WORLD);

    public static final PositionProviderType WORLD = register("world", new WorldPositionProvider.Serialiser());

    public static Object createGsonAdapter() {
        return JsonSerializing.createSerializerBuilder(POSITION_PROVIDER_TYPE, "type", "type", PositionProvider::getType).build();
    }

    private static PositionProviderType register(String id, PositionProvider.Serialiser<? extends PositionProvider> serialiser) {
        return Registry.register(POSITION_PROVIDER_TYPE, new Identifier(id), new PositionProviderType(serialiser));
    }
}
