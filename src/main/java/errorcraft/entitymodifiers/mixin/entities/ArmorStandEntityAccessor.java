package errorcraft.entitymodifiers.mixin.entities;

import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ArmorStandEntity.class)
public interface ArmorStandEntityAccessor {
	@Invoker("setShowArms")
	void invokeSetShowArms(boolean showArms);
}
