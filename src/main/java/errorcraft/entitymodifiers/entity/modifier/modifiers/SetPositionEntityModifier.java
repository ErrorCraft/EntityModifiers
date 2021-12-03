package errorcraft.entitymodifiers.entity.modifier.modifiers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import errorcraft.entitymodifiers.entity.modifier.EntityModifier;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierType;
import errorcraft.entitymodifiers.entity.modifier.EntityModifierTypes;
import net.minecraft.command.argument.CoordinateArgument;
import net.minecraft.command.argument.DefaultPosArgument;
import net.minecraft.command.argument.LookingPosArgument;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.entity.Entity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Set;

public class SetPositionEntityModifier implements EntityModifier {
	private final PositionType positionType;
	private final LootNumberProvider coordinateProviderLeftOrX;
	private final LootNumberProvider coordinateProviderUpOrY;
	private final LootNumberProvider coordinateProviderForwardOrZ;
	private boolean isXRelative;
	private boolean isYRelative;
	private boolean isZRelative;

	public SetPositionEntityModifier(PositionType positionType, LootNumberProvider coordinateProviderX, LootNumberProvider coordinateProviderY, LootNumberProvider coordinateProviderZ, boolean isXRelative, boolean isYRelative, boolean isZRelative) {
		this.positionType = positionType;
		this.coordinateProviderLeftOrX = coordinateProviderX;
		this.coordinateProviderUpOrY = coordinateProviderY;
		this.coordinateProviderForwardOrZ = coordinateProviderZ;
		this.isXRelative = isXRelative;
		this.isYRelative = isYRelative;
		this.isZRelative = isZRelative;
	}

	public SetPositionEntityModifier(PositionType positionType, LootNumberProvider coordinateProviderLeft, LootNumberProvider coordinateProviderUp, LootNumberProvider coordinateProviderForward) {
		this.positionType = positionType;
		this.coordinateProviderLeftOrX = coordinateProviderLeft;
		this.coordinateProviderUpOrY = coordinateProviderUp;
		this.coordinateProviderForwardOrZ = coordinateProviderForward;
	}

	@Override
	public EntityModifierType getType() {
		return EntityModifierTypes.SET_POSITION;
	}

	@Override
	public Entity apply(Entity entity, LootContext lootContext) {
		PosArgument location;
		if (this.positionType == PositionType.WORLD) {
			location = new DefaultPosArgument(new CoordinateArgument(this.isXRelative, this.coordinateProviderLeftOrX.nextFloat(lootContext)), new CoordinateArgument(this.isYRelative, this.coordinateProviderUpOrY.nextFloat(lootContext)), new CoordinateArgument(this.isZRelative, this.coordinateProviderForwardOrZ.nextFloat(lootContext)));
		} else {
			location = new LookingPosArgument(this.coordinateProviderLeftOrX.nextFloat(lootContext), this.coordinateProviderUpOrY.nextFloat(lootContext), this.coordinateProviderForwardOrZ.nextFloat(lootContext));
		}
		Vec3d vec3d = location.toAbsolutePos(entity.getCommandSource());

		BlockPos blockPos = new BlockPos(vec3d.x, vec3d.y, vec3d.z);
		if (!World.isValid(blockPos)) {
			try {
				throw new Exception("Invalid position for teleport");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (entity instanceof ServerPlayerEntity player) {
			ChunkPos chunkPos = new ChunkPos(blockPos);
			player.getWorld().getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, entity.getId());
			entity.stopRiding();
			if (player.isSleeping()) {
				player.wakeUp(true, true);
			}

			Set<PlayerPositionLookS2CPacket.Flag> movementFlags = EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class);
			if (this.isXRelative) movementFlags.add(PlayerPositionLookS2CPacket.Flag.X);
			if (this.isYRelative) movementFlags.add(PlayerPositionLookS2CPacket.Flag.Y);
			if (this.isZRelative) movementFlags.add(PlayerPositionLookS2CPacket.Flag.Z);
			player.networkHandler.requestTeleport(vec3d.x, vec3d.y, vec3d.z, entity.getYaw(), entity.getPitch(), movementFlags);
		} else {
			entity.refreshPositionAndAngles(vec3d.x, vec3d.y, vec3d.z, entity.getYaw(), entity.getPitch());
		}
		return entity;
	}

	private enum PositionType {
		WORLD,
		LOCAL
	}

	public static class Serialiser implements EntityModifier.Serialiser<SetPositionEntityModifier> {
		@Override
		public void toJson(JsonObject json, SetPositionEntityModifier object, JsonSerializationContext context) {
			JsonObject position = new JsonObject();
			if (object.positionType == PositionType.WORLD) {
				position.addProperty("type", "world");
				JsonObject x = new JsonObject();
				JsonObject y = new JsonObject();
				JsonObject z = new JsonObject();
				x.add("value", context.serialize(object.coordinateProviderLeftOrX));
				x.add("relative", context.serialize(object.isXRelative));
				y.add("value", context.serialize(object.coordinateProviderUpOrY));
				y.add("relative", context.serialize(object.isYRelative));
				z.add("value", context.serialize(object.coordinateProviderForwardOrZ));
				z.add("relative", context.serialize(object.isZRelative));
				position.add("x", x);
				position.add("y", y);
				position.add("z", z);
			} else {
				position.addProperty("type", "local");
				position.add("left", context.serialize(object.coordinateProviderLeftOrX));
				position.add("up", context.serialize(object.coordinateProviderUpOrY));
				position.add("forward", context.serialize(object.coordinateProviderForwardOrZ));
			}
			json.add("position", position);
		}

		@Override
		public SetPositionEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
			JsonObject position = json.getAsJsonObject("position");
			String type = JsonHelper.getString(position, "type");

			switch (type) {
				case "world" -> {
					LootNumberProvider xProvider;
					LootNumberProvider yProvider;
					LootNumberProvider zProvider;
					boolean isRelativeX = false;
					boolean isRelativeY = false;
					boolean isRelativeZ = false;

					if (position.get("x").isJsonPrimitive() && position.get("x").getAsJsonPrimitive().isNumber()) {
						xProvider = JsonHelper.deserialize(position, "x", context, LootNumberProvider.class);
					} else {
						JsonObject x = JsonHelper.getObject(position, "x");
						xProvider = JsonHelper.deserialize(x, "value", context, LootNumberProvider.class);
						if (x.has("relative")) isRelativeX = JsonHelper.getBoolean(x, "relative");
					}

					if (position.get("y").isJsonPrimitive() && position.get("y").getAsJsonPrimitive().isNumber()) {
						yProvider = JsonHelper.deserialize(position, "y", context, LootNumberProvider.class);
					} else {
						JsonObject y = JsonHelper.getObject(position, "y");
						yProvider = JsonHelper.deserialize(y, "value", context, LootNumberProvider.class);
						if (y.has("relative")) isRelativeY = JsonHelper.getBoolean(y, "relative");
					}

					if (position.get("z").isJsonPrimitive() && position.get("z").getAsJsonPrimitive().isNumber()) {
						zProvider = JsonHelper.deserialize(position, "z", context, LootNumberProvider.class);
					} else {
						JsonObject z = JsonHelper.getObject(position, "z");
						zProvider = JsonHelper.deserialize(z, "value", context, LootNumberProvider.class);
						if (z.has("relative")) isRelativeZ = JsonHelper.getBoolean(z, "relative");
					}

					return new SetPositionEntityModifier(PositionType.WORLD, xProvider, yProvider, zProvider, isRelativeX, isRelativeY, isRelativeZ);
				}
				case "local" -> {
					LootNumberProvider leftProvider = JsonHelper.deserialize(position, "left", context, LootNumberProvider.class);
					LootNumberProvider upProvider = JsonHelper.deserialize(position, "up", context, LootNumberProvider.class);
					LootNumberProvider forwardProvider = JsonHelper.deserialize(position, "forward", context, LootNumberProvider.class);

					return new SetPositionEntityModifier(PositionType.LOCAL, leftProvider, upProvider, forwardProvider);
				}
				default -> throw new JsonSyntaxException("Expected " + type + " to have a different value");
			}
		}
	}
}
