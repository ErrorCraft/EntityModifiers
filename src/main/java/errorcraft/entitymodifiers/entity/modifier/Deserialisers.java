package errorcraft.entitymodifiers.entity.modifier;

import com.google.gson.GsonBuilder;
import errorcraft.entitymodifiers.util.RelativeNumberProvider;
import errorcraft.entitymodifiers.world.position.provider.PositionProvider;
import errorcraft.entitymodifiers.world.position.provider.PositionProviderTypes;
import errorcraft.entitymodifiers.world.rotation.RotationProvider;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProviderTypes;
import net.minecraft.loot.provider.score.LootScoreProvider;
import net.minecraft.loot.provider.score.LootScoreProviderTypes;

class Deserialisers {
	private Deserialisers() {}

	public static GsonBuilder createEntityModifierSerialiser() {
		return new GsonBuilder()
			.registerTypeHierarchyAdapter(EntityModifier.class, EntityModifierTypes.createGsonAdapter())
			.registerTypeHierarchyAdapter(LootNumberProvider.class, LootNumberProviderTypes.createGsonSerializer())
			.registerTypeHierarchyAdapter(LootScoreProvider.class, LootScoreProviderTypes.createGsonSerializer())
			.registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer())
			.registerTypeHierarchyAdapter(PositionProvider.class, PositionProviderTypes.createGsonAdapter())
			.registerTypeHierarchyAdapter(RotationProvider.class, new RotationProvider.Serialiser())
			.registerTypeHierarchyAdapter(RelativeNumberProvider.class, new RelativeNumberProvider.Serialiser());
	}
}
