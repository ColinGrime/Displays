package me.colingrimes.displays.util;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Utils {

	/**
	 * Simple method to ray trace a block that is up to 50 blocks away.
	 *
	 * @param player the player
	 * @return the block or null
	 */
	@Nullable
	public static Block rayTraceBlock(@Nonnull Player player) {
		RayTraceResult result = player.rayTraceBlocks(50);
		return result != null ? result.getHitBlock() : null;
	}
}
