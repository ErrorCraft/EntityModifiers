package errorcraft.entitymodifiers.entity.modifier;

import com.google.gson.GsonBuilder;

class Deserialisers {
	private Deserialisers() {}

	public static GsonBuilder createEntityModifierSerialiser() {
		return new GsonBuilder().registerTypeHierarchyAdapter(EntityModifier.class, EntityModifierTypes.createGsonAdapter());
	}
}
