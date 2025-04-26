package me.colingrimes.displays.command.display.fun.shrinkgun;

import me.colingrimes.displays.Displays;
import me.colingrimes.displays.util.DisplayUtil;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.plugin.MidnightPlugin;
import me.colingrimes.midnight.scheduler.Scheduler;
import me.colingrimes.midnight.util.Common;
import me.colingrimes.midnight.util.bukkit.Inventories;
import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.midnight.util.bukkit.NBT;
import me.colingrimes.midnight.util.bukkit.Players;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class DisplayShrinkGun implements Command<Displays>, Listener {

	private final Map<Player, Target> targets = new HashMap<>();

	public DisplayShrinkGun() {
		Common.register(MidnightPlugin.get(), this);
		Scheduler.sync().runRepeating(() -> Players.forEach(this::getTarget), 10L, 1L);
	}

	@Override
	public void execute(@Nonnull Displays plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		ItemStack laserGun = Items
				.of(Material.REDSTONE_TORCH)
				.name("&c&lShrink Gun")
				.nbt("shrink_gun", true)
				.build();
		Inventories.give(sender.player(), laserGun);
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setPlayerRequired(true);
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getHand() == EquipmentSlot.HAND && event.getAction().name().startsWith("RIGHT_CLICK") && targets.containsKey(player)) {
			fireLaserGun(player);
			event.setCancelled(true);
		}
	}

	/**
	 * Fires the Laser Gun.
	 *
	 * @param player the player
	 */
	private void fireLaserGun(@Nonnull Player player) {
		Location eye = player.getEyeLocation();
		Vector direction = eye.getDirection().normalize();

		for (double d=0; d<=20; d+=1) {
			Location point = eye.clone().add(direction.clone().multiply(d));
			if (point.getBlock().isPassable()) {
				player.spawnParticle(Particle.DUST, point, 2, 0, 0, 0, 0, new Particle.DustOptions(Color.RED, 0.5f));
				continue;
			}

			Target target = targets.get(player);
			BlockDisplay blockDisplay = target.blockDisplay;
			blockDisplay.setTransformation(new Transformation(
					new Vector3f(0.25f, 0, 0.25f),
					new Quaternionf(),
					new Vector3f(0.5f, 0.5f, 0.5f),
					new Quaternionf()
			));
			blockDisplay.setGlowing(false);
			blockDisplay.setBrightness(new Display.Brightness(15, 15));
			target.block.setType(Material.AIR);
			targets.remove(player);
			return;
		}
	}

	/**
	 * Gets the new target of the Laser Gun by raytracing where the player is looking.
	 *
	 * @param player the player
	 */
	private void getTarget(@Nonnull Player player) {
		Target existing = targets.get(player);
		ItemStack item = player.getInventory().getItemInMainHand();
		if (!NBT.hasTag(item, "shrink_gun", Boolean.class)) {
			removeTarget(player, existing);
			return;
		}

		RayTraceResult result = player.rayTraceBlocks(20);
		if (result == null || result.getHitBlock() == null) {
			removeTarget(player, existing);
			return;
		}

		Block block = result.getHitBlock();

		// Same target, ignore.
		if (existing != null && existing.block.equals(block)) {
			return;
		}

		// Remove old target.
		if (existing != null) {
			removeTarget(player, existing);
		}

		Location location = block.getLocation();
		BlockDisplay blockDisplay = DisplayUtil.createBlock(location, block.getType().createBlockData());
		blockDisplay.setGlowing(true);
		blockDisplay.setGlowColorOverride(Color.RED);
		block.setType(Material.BARRIER);
		targets.put(player, new Target(block, blockDisplay));
	}

	/**
	 * Removes the target from the player.
	 *
	 * @param player the player
	 * @param target the target
	 */
	private void removeTarget(@Nonnull Player player, @Nullable Target target) {
		if (target == null) {
			return;
		}

		target.block.setType(target.blockDisplay.getBlock().getMaterial());
		target.blockDisplay.remove();
		targets.remove(player);
	}

	private static class Target {
		private final Block block;
		private final BlockDisplay blockDisplay;

		public Target(@Nonnull Block block, @Nonnull BlockDisplay blockDisplay) {
			this.block = block;
			this.blockDisplay = blockDisplay;
		}
	}
}
