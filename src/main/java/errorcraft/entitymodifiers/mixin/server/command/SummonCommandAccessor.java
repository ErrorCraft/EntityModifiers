package errorcraft.entitymodifiers.mixin.server.command;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.command.SummonCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SummonCommand.class)
public interface SummonCommandAccessor {
	@Accessor("FAILED_EXCEPTION")
	static SimpleCommandExceptionType getFailedException() {
		throw new AssertionError();
	}

	@Accessor("FAILED_UUID_EXCEPTION")
	static SimpleCommandExceptionType getFailedUUIDException() {
		throw new AssertionError();
	}

	@Accessor("INVALID_POSITION_EXCEPTION")
	static SimpleCommandExceptionType getInvalidPositionException() {
		throw new AssertionError();
	}
}
