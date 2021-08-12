package errorcraft.entitymodifiers.mixin.server;

import errorcraft.entitymodifiers.entity.modifier.EntityModifierManager;
import errorcraft.entitymodifiers.mixin.resource.ServerResourceManagerAccessor;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecraftServer.class)
public class MinecraftServerExtender implements MinecraftServerExtenderAccess {
	@Override
	public EntityModifierManager getEntityModifierManager() {
		MinecraftServerAccessor thisAccessor = (MinecraftServerAccessor)this;
		ServerResourceManagerAccessor serverResourceManagerAccessor = (ServerResourceManagerAccessor)(thisAccessor.getServerResourceManager());
		return serverResourceManagerAccessor.getEntityModifierManager();
	}
}
