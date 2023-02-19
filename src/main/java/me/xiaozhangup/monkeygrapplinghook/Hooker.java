package me.xiaozhangup.monkeygrapplinghook;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Hooker {
    public static void normalPush(Player p, FishHook hook, Double multiply) {
        Location entityLoc = p.getLocation();
        Vector boost = p.getVelocity();
        boost.setY(0.3D);
        p.setVelocity(boost);
        Bukkit.getScheduler().scheduleSyncDelayedTask(MonkeyGrapplingHook.plugin, () -> {
            double g = -0.08D;
            double d = hook.getLocation().distance(entityLoc);
            double t = d;
            double v_x = (1.0D + 0.07D * t) * (hook.getLocation().getX() - entityLoc.getX()) / t;
            double v_y = (1.0D + 0.03D * t) * (hook.getLocation().getY() - entityLoc.getY()) / t - 0.5D * g * t;
            double v_z = (1.0D + 0.07D * t) * (hook.getLocation().getZ() - entityLoc.getZ()) / t;
            Vector v = p.getVelocity();
            v.setX(v_x);
            v.setY(v_y);
            v.setZ(v_z);
            v.multiply(multiply.doubleValue());
            p.setVelocity(v);
        }, 1L);
    }
}
