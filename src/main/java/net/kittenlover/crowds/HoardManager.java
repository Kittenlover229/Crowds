package net.kittenlover.crowds;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.PersistentState;

public class HoardManager extends PersistentState {
	private final ServerWorld world;
	public boolean active;

	public HoardManager(ServerWorld world, boolean active) {
		this.world = world;
		;
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
		for (var player : world.getPlayers()) {
			player.sendMessage(new TranslatableText("text.crowds.crowds_interrupt"), active);
		}
	}

	public void tick() {

	}

	public static HoardManager fromNBT(NbtCompound nbt, ServerWorld world) {
		boolean active = nbt.getBoolean("active");
		var ret = new HoardManager(world, active);
		return ret;
	}

	public static HoardManager fromWorld(ServerWorld world) {
		return world.getPersistentStateManager().getOrCreate(nbt -> HoardManager.fromNBT(nbt, world),
				() -> new HoardManager(world, false), "hoard_manager" + world.getDimension().toString());
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putBoolean("active", this.active);
		return nbt;
	}

}
