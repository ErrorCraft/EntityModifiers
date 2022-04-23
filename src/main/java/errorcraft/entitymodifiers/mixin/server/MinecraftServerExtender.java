package errorcraft.entitymodifiers.mixin.server;

import errorcraft.entitymodifiers.access.server.DataPackContentsExtenderAccess;
import errorcraft.entitymodifiers.access.server.MinecraftServerExtenderAccess;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierManager;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftServer.class)
public class MinecraftServerExtender implements MinecraftServerExtenderAccess {
	@Shadow
	private MinecraftServer.ResourceManagerHolder resourceManagerHolder;

	@Override
	public EntityModifierManager getEntityModifierManager() {
		DataPackContentsExtenderAccess dataPackContentsExtenderAccess = (DataPackContentsExtenderAccess)(this.resourceManagerHolder.dataPackContents());
		return dataPackContentsExtenderAccess.getEntityModifierManager();
	}
}
