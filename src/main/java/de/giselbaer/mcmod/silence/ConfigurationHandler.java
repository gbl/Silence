package de.giselbaer.mcmod.silence;

import de.guntram.mcmod.fabrictools.ConfigChangedEvent;
import de.guntram.mcmod.fabrictools.Configuration;
import de.guntram.mcmod.fabrictools.ModConfigurationHandler;
import java.io.File;

public class ConfigurationHandler implements ModConfigurationHandler
{
    private static ConfigurationHandler instance;

    private boolean trackFocus;
    private boolean wantIcon = true;
    private int xPercent = 5;
    private int yPercent = 90;
    private Configuration config;

    public static ConfigurationHandler getInstance() {
        if (instance==null)
            instance=new ConfigurationHandler();
        return instance;
    }
    
    public void load(final File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }
    }
    
    @Override
    public void onConfigChanging(ConfigChangedEvent.OnConfigChangingEvent event) {
        if (event.getModID().equals(Silence.MODNAME)) {
            switch(event.getItem()) {
                case "silence.config.xPercent": xPercent = (int)(Integer)event.getNewValue(); break;
                case "silence.config.yPercent": yPercent = (int)(Integer)event.getNewValue(); break;
                case "silence.config.wantIcon": wantIcon = (boolean)(Boolean)event.getNewValue(); break;
            }
        }
    }
    
    @Override
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Silence.MODNAME)) {
            loadConfig();
        }
    }
    
    private void loadConfig() {
        trackFocus=config.getBoolean("silence.config.trackfocus", Configuration.CATEGORY_CLIENT, trackFocus, "silence.config.tt.trackfocus");
        wantIcon  =config.getBoolean("silence.config.wantIcon", Configuration.CATEGORY_CLIENT, wantIcon, "silence.config.tt.wantIcon");
        xPercent  =config.getInt("silence.config.xPercent", Configuration.CATEGORY_CLIENT, xPercent, 0, 100, "silence.config.tt.xPercent");
        yPercent  =config.getInt("silence.config.yPercent", Configuration.CATEGORY_CLIENT, yPercent, 0, 100, "silence.config.tt.yPercent");
        if (config.hasChanged())
            config.save();
    }
    
    @Override
    public Configuration getConfig() {
        return getInstance().config;
    }
    
    public static boolean trackFocus() { return getInstance().trackFocus; }
    public static boolean getWantIcon() { return getInstance().wantIcon; }
    public static int getXPercent() { return getInstance().xPercent; }
    public static int getYPercent() { return getInstance().yPercent; }
}
