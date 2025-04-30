package me.colingrimes.displays.command.display.block;

import me.colingrimes.displays.Displays;
import me.colingrimes.displays.util.DisplayUtil;
import me.colingrimes.displays.util.Utils;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.util.ArgumentList;
import me.colingrimes.midnight.command.handler.util.CommandProperties;
import me.colingrimes.midnight.command.handler.util.Sender;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class DisplayBlock implements Command<Displays> {

	@Override
	public void execute(@Nonnull Displays plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		Block block = Utils.rayTraceBlock(sender.player());
		if (block == null) {
			return;
		}

		Material material = Material.getMaterial(args.getOrDefault(0, "STONE"));
		if (material == null) {
			return;
		}

		Location location = block.getLocation().clone().add(0, 1, 0);
		DisplayUtil.createBlock(location, material.createBlockData());
	}

	@Nullable
	@Override
	public List<String> tabComplete(@Nonnull Displays plugin, @Nonnull Sender sender, @Nonnull ArgumentList args) {
		if (args.size() == 1) {
			return Stream.of(Material.values()).filter(m -> m.isBlock() && m.name().contains(args.getFirst())).map(Material::name).toList();
		}
		return null;
	}

	@Override
	public void configureProperties(@Nonnull CommandProperties properties) {
		properties.setPlayerRequired(true);
	}
}
