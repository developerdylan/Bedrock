package me.ohvalsgod.bedrock.command.param.defaults;

import com.google.common.collect.ImmutableList;
import me.ohvalsgod.bedrock.command.param.ParameterType;
import me.ohvalsgod.bedrock.util.bukkit.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class ItemStackParameterType implements ParameterType<ItemStack> {

    @Override
    public ItemStack transform(CommandSender sender, String source) {
        ItemStack item = ItemUtil.get(source);

        if (item == null) {
            sender.sendMessage(ChatColor.RED + "No item with the name '" + source + "' found.");
            return null;
        }

        return item;
    }

    @Override
    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        return ImmutableList.of();
    }

}
