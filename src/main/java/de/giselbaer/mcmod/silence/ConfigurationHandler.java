package de.giselbaer.mcmod.silence;

import de.guntram.mcmod.fabrictools.ConfigChangedEvent;
import de.guntram.mcmod.fabrictools.Configuration;
import de.guntram.mcmod.fabrictools.ModConfigurationHandler;
import java.io.File;

public class ConfigurationHandler implements ModConfigurationHandler
{
    private static ConfigurationHandler instance;

    private boolean trackFocus;
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
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Silence.MODNAME)) {
            loadConfig();
        }
    }
    
    private void loadConfig() {
        trackFocus=config.getBoolean("silence.config.trackfocus", Configuration.CATEGORY_CLIENT, trackFocus, "silence.config.tt.trackfocus");
        if (config.hasChanged())
            config.save();
    }
    
    @Override
    public Configuration getConfig() {
        return getInstance().config;
    }
    
    public static boolean trackFocus() {
        return getInstance().trackFocus;
    }
}
