package errorcraft.entitymodifiers.mixin.resource;

import errorcraft.entitymodifiers.access.resource.ServerResourceManagerExtenderAccess;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ServerResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerResourceManager.class)
public interface ServerResourceManagerAccessor extends ServerResourceManagerExtenderAccess {
	@Accessor
	ReloadableResourceManager getResourceManager();
}
