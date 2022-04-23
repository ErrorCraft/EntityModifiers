package errorcraft.entitymodifiers.mixin.server;

import errorcraft.entitymodifiers.access.server.DataPackContentsExtenderAccess;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.server.DataPackContents;
import net.minecraft.server.command.CommandManager.RegistrationEnvironment;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(DataPackContents.class)
public class DataPackContentsExtender implements DataPackContentsExtenderAccess {
	private final EntityModifierManager entityModifierManager = new EntityModifierManager();

	@Inject(at = @At("RETURN"), method = "getContents", cancellable = true)
	private void addEntityModifierManagerToContents(CallbackInfoReturnable<List<ResourceReloader>> info) {
		List<ResourceReloader> reloaders = new ArrayList<>(info.getReturnValue());
		reloaders.add(this.entityModifierManager);
		info.setReturnValue(Collections.unmodifiableList(reloaders));
	}

	@Override
	public EntityModifierManager getEntityModifierManager() {
		return this.entityModifierManager;
	}
}
