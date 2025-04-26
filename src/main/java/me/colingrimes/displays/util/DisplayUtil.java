package me.colingrimes.displays.util;

import com.google.common.base.Preconditions;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class DisplayUtil {

	public static void createItem(@Nonnull Location location, @Nonnull Material material, @Nonnull ItemDisplay.ItemDisplayTransform transform) {
		Preconditions.checkArgument(location.getWorld() != null, "World is null.");
		location.getWorld().spawn(location, ItemDisplay.class, (entity) -> {
			entity.setItemStack(new ItemStack(material));
			entity.setItemDisplayTransform(transform);
			entity.setPersistent(false);
			entity.setGlowing(true);
			entity.setGlowColorOverride(Color.RED);
		});
	}

	public static void createBlock(@Nonnull Location location, @Nonnull BlockData block) {
		Preconditions.checkArgument(location.getWorld() != null, "World is null.");
		location.getWorld().spawn(location, BlockDisplay.class, (entity) -> {
			entity.setBlock(block);
			entity.setPersistent(false);
			entity.setGlowing(true);
			entity.setGlowColorOverride(Color.RED);
		});
	}
}
