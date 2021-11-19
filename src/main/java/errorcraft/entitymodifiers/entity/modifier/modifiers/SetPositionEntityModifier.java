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
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;
import java.util.Set;

public class SetPositionEntityModifier implements EntityModifier {
	private final String type;
	private final LootNumberProvider xProvider;
	private final LootNumberProvider yProvider;
	private final LootNumberProvider zProvider;

	public SetPositionEntityModifier(String type, LootNumberProvider xProvider, LootNumberProvider yProvider, LootNumberProvider zProvider) {
		this.type = type;
		this.xProvider = xProvider;
		this.yProvider = yProvider;
		this.zProvider = zProvider;
	}

	@Override
	public EntityModifierType getType() {
		return EntityModifierTypes.SET_POSITION;
	}

	@Override
	public Entity apply(Entity entity, LootContext lootContext) {
		boolean isRelative = !type.equals("absolute");
		double x = this.xProvider != null ? this.xProvider.nextFloat(lootContext) : 0;
		double y = this.yProvider != null ? this.yProvider.nextFloat(lootContext) : 0;
		double z = this.zProvider != null ? this.zProvider.nextFloat(lootContext) : 0;

		PosArgument location = type.equals("local") ? new LookingPosArgument(x, y, z) : new DefaultPosArgument(new CoordinateArgument(isRelative, x), new CoordinateArgument(isRelative, y), new CoordinateArgument(isRelative, z));
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
			player.getServerWorld().getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, entity.getId());
			entity.stopRiding();
			if (player.isSleeping()) {
				player.wakeUp(true, true);
			}

			Set<PlayerPositionLookS2CPacket.Flag> movementFlags = EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class);
			if (isRelative) {
				movementFlags.add(PlayerPositionLookS2CPacket.Flag.X);
				movementFlags.add(PlayerPositionLookS2CPacket.Flag.Y);
				movementFlags.add(PlayerPositionLookS2CPacket.Flag.Z);
			}
			player.networkHandler.requestTeleport(vec3d.x, vec3d.y, vec3d.z, entity.getYaw(), entity.getPitch(), movementFlags);
		} else {
			entity.refreshPositionAndAngles(vec3d.x, vec3d.y, vec3d.z, entity.getYaw(), entity.getPitch());
		}
		return entity;
	}

	public static class Serialiser implements EntityModifier.Serialiser<SetPositionEntityModifier> {
		@Override
		public void toJson(JsonObject json, SetPositionEntityModifier object, JsonSerializationContext context) {
			json.addProperty("type", object.type);
			json.add("x", context.serialize(object.xProvider));
			json.add("y", context.serialize(object.yProvider));
			json.add("z", context.serialize(object.zProvider));
		}

		@Override
		public SetPositionEntityModifier fromJson(JsonObject json, JsonDeserializationContext context) {
			String type = JsonHelper.getString(json, "type");
			LootNumberProvider xProvider = JsonHelper.hasElement(json, "x") ? JsonHelper.deserialize(json, "x", context, LootNumberProvider.class) : null;
			LootNumberProvider yProvider = JsonHelper.hasElement(json, "y") ? JsonHelper.deserialize(json, "y", context, LootNumberProvider.class) : null;
			LootNumberProvider zProvider = JsonHelper.hasElement(json, "z") ? JsonHelper.deserialize(json, "z", context, LootNumberProvider.class) : null;

			if (!StringUtils.equalsAny(type, "absolute", "relative", "local")) {
				throw new JsonSyntaxException("Expected " + type + " to have a different value");
			}

			return new SetPositionEntityModifier(type, xProvider, yProvider, zProvider);
		}
	}
}
