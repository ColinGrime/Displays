package me.colingrimes.displays.command.display.axis;

import me.colingrimes.displays.Displays;
import me.colingrimes.displays.util.DisplayUtil;
import me.colingrimes.displays.util.Transformations;
import me.colingrimes.displays.util.Utils;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;

import javax.annotation.Nonnull;

public class DisplayAxis implements Command<Displays> {

	@Override
	public void execute(@Nonnull Displays plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Block block = Utils.rayTraceBlock(sender.player());
		if (block == null) {
			return;
		}

		double x = block.getLocation().getX() + args.getDoubleOrDefault(0, 0);
		double y = block.getLocation().getY() + args.getDoubleOrDefault(1, 0);
		double z = block.getLocation().getZ() + args.getDoubleOrDefault(2, 0);
		renderAxes(new Location(block.getLocation().getWorld(), x, y, z));
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setPlayerRequired(true);
	}

	private void renderAxes(@Nonnull Location origin) {
		DisplayUtil.createBlock(origin, Material.YELLOW_WOOL.createBlockData(), Transformations.of(-0.1, -0.1, -0.1).scale(0.2, 0.2, 0.2).build()).setBrightness(new Display.Brightness(15, 15));
		DisplayUtil.createBlock(origin, Material.BLUE_WOOL.createBlockData(), Transformations.of(0, -0.06, -0.06).scale(1.5, 0.1, 0.1).build()).setBrightness(new Display.Brightness(15, 15));
		DisplayUtil.createBlock(origin, Material.GREEN_WOOL.createBlockData(), Transformations.of(-0.05, -0.05, 0).scale(0.1, 0.1, 1.5).build()).setBrightness(new Display.Brightness(15, 15));
		DisplayUtil.createBlock(origin, Material.RED_WOOL.createBlockData(), Transformations.of(-0.05, 0, -0.05).scale(0.1, 1.5, 0.1).build()).setBrightness(new Display.Brightness(15, 15));
	}
}
