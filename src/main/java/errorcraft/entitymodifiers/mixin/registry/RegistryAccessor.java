package errorcraft.entitymodifiers.mixin.registry;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Registry.class)
public interface RegistryAccessor {
	@Invoker("createRegistryKey")
	static <T> RegistryKey<Registry<T>> createRegistryKey(String registryId) {
		throw new AssertionError();
	}

	@Invoker("create")
	static <T> Registry<T> create(RegistryKey<? extends Registry<T>> key, Registry.DefaultEntryGetter<T> defaultEntryGetter) {
		throw new AssertionError();
	}
}
