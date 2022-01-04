package net.kittenlover.crowds;

import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.PersistentState;

public class HoardManager extends PersistentState {

	private ServerBossBar bar;
	private final ServerWorld world;
	public boolean active;

	public HoardManager(ServerWorld world, boolean active) {
		this.world = world;
		this.bar = null;
		this.active = active;
		this.markDirty();
	}

	public void activate() {
		this.active = true;
		for (var player : world.getPlayers()) {
			player.sendMessage(new TranslatableText("text.crowds.crowds_start"), active);
		}
	}

	public void deactivate() {
		this.active = false;
	}

	public void tick() {

	}

	public static HoardManager fromNBT(NbtCompound nbt) {
		return null;
	}

	public static HoardManager fromWorld(ServerWorld world) {
		return world.getPersistentStateManager().getOrCreate(nbt -> HoardManager.fromNBT(nbt),
				() -> new HoardManager(world, false), "plague_moon" + world.getDimension().toString());
	}

	@Override
	public NbtCompound writeNbt(NbtCompound var) {
		return var;
	}

}
