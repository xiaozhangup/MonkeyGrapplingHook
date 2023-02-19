package me.xiaozhangup.monkeygrapplinghook;

import io.th0rgal.oraxen.api.OraxenItems;
import me.xiaozhangup.monkeygrapplinghook.utils.command.Command;
import me.xiaozhangup.monkeygrapplinghook.utils.manager.ListenerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.Objects;

public class MonkeyGrapplingHook extends JavaPlugin implements Listener {

    public static Plugin plugin;
    public static ListenerManager listenerManager = new ListenerManager();
    public NamespacedKey namespace;

    @Override
    public void onEnable() {
        plugin = this;
        namespace = new NamespacedKey(plugin, "grappling");

        listenerManager.addListeners(
                this
        );
        listenerManager.register();

        Command.register("gethook", (commandSender, command, s, inside) -> {
            if (commandSender.isOp() && commandSender instanceof Player p) {
                p.getInventory().addItem(getHook(Integer.parseInt(inside[0])));
            }
            return false;
        });


    }

    public ItemStack getHook(int dur) {
        ItemStack itemStack = OraxenItems.getItemById("grapplinghook").build();
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        data.set(namespace, PersistentDataType.STRING, "true");
        itemStack.setItemMeta(itemMeta);
        itemStack.setDurability((short) dur);

        return itemStack;
    }

    @EventHandler
    public void on(PlayerFishEvent e) {
        if (e.getState() == PlayerFishEvent.State.REEL_IN || e.getState() == PlayerFishEvent.State.IN_GROUND) {
            Player player = e.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            try {
                PersistentDataContainer data = meta.getPersistentDataContainer();
                String ret = data.get(namespace, PersistentDataType.STRING);
                if (Objects.equals(ret, "true")) {
                    Hooker.normalPush(player, e.getHook(), 1.2);
                    if (item.getType().getMaxDurability() < item.getDurability()) {
                        item.setType(Material.AIR);
                        sendTitle(player, "抓钩已损坏", 210, 15, 57);
                    } else {
                        item.setDurability((short) (item.getDurability() + 1));
                        if (item.getType().getMaxDurability() == item.getDurability()) {
                            sendTitle(player, "抓钩已损坏", 210, 15, 57);
                        } else if ((item.getType().getMaxDurability() - item.getDurability()) < 6) {
                            sendTitle(player, "抓钩即将损坏", 230, 69, 83);
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static void sendTitle(Player player, String 抓钩即将损坏, int r, int g, int b) {
        player.resetTitle();
        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(Duration.ofSeconds(0),Duration.ofSeconds(1),Duration.ofSeconds(0)));
        player.sendTitlePart(TitlePart.TITLE, Component.text(""));
        player.sendTitlePart(TitlePart.SUBTITLE, Component.text(抓钩即将损坏).color(TextColor.color(r, g, b)));
    }

}
