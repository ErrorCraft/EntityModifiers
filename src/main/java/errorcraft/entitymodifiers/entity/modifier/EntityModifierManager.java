package errorcraft.entitymodifiers.entity.modifier;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class EntityModifierManager extends JsonDataLoader {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = Deserialisers.createEntityModifierSerialiser().create();
	private Map<Identifier, EntityModifier> modifiers = ImmutableMap.of();

	public EntityModifierManager() {
		super(GSON, "modifiers/entities");
	}

	@Override
	protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
		ImmutableMap.Builder<Identifier, EntityModifier> builder = ImmutableMap.builder();
		for (Identifier resourceLocation : prepared.keySet()) {
			JsonElement json = prepared.get(resourceLocation);
			try {
				EntityModifier modifier = GSON.fromJson(json, EntityModifier.class);
				builder.put(resourceLocation, modifier);
			} catch (Exception e) {
				LOGGER.error("Couldn't parse entity modifier {}", resourceLocation, e);
			}
		}
		this.modifiers = builder.build();
	}

	public EntityModifier get(Identifier resourceLocation) {
		return this.modifiers.get(resourceLocation);
	}

	public Set<Identifier> getKeys() {
		return Collections.unmodifiableSet(this.modifiers.keySet());
	}
}
