/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.giselbaer.mcmod.silence;

import de.guntram.mcmod.fabrictools.ConfigurationProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F12;

/**
 *
 * @author gbl
 */
public class Silence implements ClientModInitializer {
    static public final String MODID = "silence";
    static public final String MODNAME = "Silence!";
    private static Silence instance;    
    private KeyBinding muteKey;
    private boolean focused;
    float originalVolume = 1.0f;
    float volumeOnFocusLost = 1.0f;
    private static ConfigurationHandler confHandler;
    private static boolean silencedByMe = false;

    public static Identifier SPEAKER_TEXTURE;

    @Override
    public void onInitializeClient() {
        SPEAKER_TEXTURE = new Identifier(Silence.MODID, "textures/texture.png");
        instance = this;
        confHandler=ConfigurationHandler.getInstance();
        ConfigurationProvider.register(MODNAME, confHandler);
        confHandler.load(ConfigurationProvider.getSuggestedFile(MODID));        
        setKeyBinding();
        focused = true;
    }

    private void setKeyBinding() {
        final String category = "key.categories.silence";
        muteKey = new KeyBinding("key.silence.toggle", InputUtil.Type.KEYSYM, GLFW_KEY_F12, category);
        KeyBindingHelper.registerKeyBinding(muteKey);
        ClientTickEvents.END_CLIENT_TICK.register(e -> processKeyBind());
        ClientTickEvents.END_CLIENT_TICK.register(e -> checkFocusChange());
    }

    private void processKeyBind() {
        if (muteKey.wasPressed()) {
            GameOptions options = MinecraftClient.getInstance().options;
            float current = options.getSoundVolume(SoundCategory.MASTER);
            if (current > 0) {
                originalVolume = options.getSoundVolume(SoundCategory.MASTER);
                setZero();
            } else {
                restore(originalVolume);
            }
            options.write();
        }
    }

    private void checkFocusChange() {
        if (ConfigurationHandler.trackFocus()) {
            boolean nowFocused = MinecraftClient.getInstance().isWindowFocused();
            if (nowFocused != focused) {
                GameOptions options = MinecraftClient.getInstance().options;
                if (nowFocused) {
                    restore(volumeOnFocusLost);
                } else {
                    volumeOnFocusLost = options.getSoundVolume(SoundCategory.MASTER);
                    setZero();
                }
                focused = nowFocused;
            }
        }
    }
    
    private void setZero() {
        GameOptions options = MinecraftClient.getInstance().options;
        options.setSoundVolume(SoundCategory.MASTER, 0.0f);
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        silencedByMe = true;
        if (player != null) {
            player.sendMessage(Text.translatable("silence.soundoff"), true);
        }
    }
    
    private void restore(float previous) {
        GameOptions options = MinecraftClient.getInstance().options;
        options.setSoundVolume(SoundCategory.MASTER, previous);
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        silencedByMe = false;
        if (player != null) {
            player.sendMessage(Text.translatable("silence.soundon", (int)(previous * 100)), true);            
        }
    }
    
    public static boolean silencedByMe() { return silencedByMe; }
}
