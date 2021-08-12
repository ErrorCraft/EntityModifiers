package errorcraft.entitymodifiers.mixin.server;

import errorcraft.entitymodifiers.access.server.MinecraftServerExtenderAccess;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftServer.class)
public interface MinecraftServerAccessor extends MinecraftServerExtenderAccess {
	@Accessor
	ServerResourceManager getServerResourceManager();
}
