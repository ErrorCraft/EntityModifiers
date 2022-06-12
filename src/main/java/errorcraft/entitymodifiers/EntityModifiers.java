package errorcraft.entitymodifiers;

import errorcraft.entitymodifiers.command.EntityCommand;
import errorcraft.entitymodifiers.command.SummonCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class EntityModifiers implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistry, environment) -> {
			EntityCommand.register(dispatcher);
			SummonCommand.register(dispatcher);
		});
	}
}
