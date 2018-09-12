package me.ohvalsgod.bedrock.util;

import me.ohvalsgod.bedrock.Bedrock;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {

    public static void run(Runnable runnable) {
        Bedrock.getInstance().getServer().getScheduler().runTask(Bedrock.getInstance(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        Bedrock.getInstance().getServer().getScheduler().runTaskTimer(Bedrock.getInstance(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(Bedrock.getInstance(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        Bedrock.getInstance().getServer().getScheduler().runTaskLater(Bedrock.getInstance(), runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        Bedrock.getInstance().getServer().getScheduler().runTaskAsynchronously(Bedrock.getInstance(), runnable);
    }

}
