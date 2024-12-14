public class SaveRunnable implements Runnable {

    @Override
    public void run() {
        if (PlayerModule.getInstance().getQueue().isEmpty())
            return;
        KingdomFactionsPlayer p = PlayerModule.getInstance().getQueue().peek();
        
        int retries = 3;
        while (retries > 0) {
            try {
                p.save();
                break;  // Successfully saved, exit loop
            } catch (Exception e) {
                retries--;
                if (retries == 0) {
                    Logger.ERROR.log("Failed to save player " + p.getName() + " after multiple attempts.");
                }
                try {
                    Thread.sleep(1000);  // Wait before retrying
                } catch (InterruptedException ex) {
                    // Handle interruption
                }
            }
        }
        
        PlayerModule.getInstance().getQueue().poll();
        
        if (!Bukkit.getOnlinePlayers().contains(p.getPlayer())) {
            Logger.MEMLEAK.log("Discovered empty KingdomFactionsPlayer object which should NOT exist.");
        }

        if (PlayerModule.getInstance().saving) {
            if (PlayerModule.getInstance().getQueue().isEmpty()) {
                PlayerModule.getInstance().saving = false;
                Logger.INFO.log("Auto save finished!");
            }
        }
    }
}
