package taboolib.common;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SimpleThreadPool {

    private static final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(16);

    public static ScheduledThreadPoolExecutor getScheduler() {
        return scheduler;
    }

    public static void async(Runnable runnable) {
        try {
            scheduler.execute(runnable);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static void asyncLater(Runnable runnable, long delay, TimeUnit timeUnit) {
        try {
            scheduler.schedule(runnable, delay, timeUnit);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public static ScheduledFuture<?> asyncTimer(Runnable runnable, long delay, long period, TimeUnit timeUnit) {
        try {
            return scheduler.scheduleAtFixedRate(runnable, delay, period, timeUnit);
        } catch (Exception e) {
            e.fillInStackTrace();
            return null;
        }
    }
}
