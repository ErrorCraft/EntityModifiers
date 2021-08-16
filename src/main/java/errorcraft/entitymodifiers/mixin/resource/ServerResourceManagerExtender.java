package errorcraft.entitymodifiers.mixin.resource;

import errorcraft.entitymodifiers.access.resource.ServerResourceManagerExtenderAccess;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerResourceManager.class)
public class ServerResourceManagerExtender implements ServerResourceManagerExtenderAccess {
	private EntityModifierManager entityModifierManager;

	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/util/registry/DynamicRegistryManager; Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment; I)V")
	private void injectEntityModifierManagerIntoConstructor(DynamicRegistryManager registryManager, RegistrationEnvironment commandEnvironment, int functionPermissionLevel, CallbackInfo info) {
		this.entityModifierManager = new EntityModifierManager();
		((ServerResourceManagerAccessor)this).getResourceManager().registerReloader(this.entityModifierManager);
	}

	@Override
	public EntityModifierManager getEntityModifierManager() {
		return this.entityModifierManager;
	}
}
