package de.giselbaer.mcmod.silence.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import de.giselbaer.mcmod.silence.ConfigurationHandler;
import de.giselbaer.mcmod.silence.Silence;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class SilenceGuiMixin {
    @Inject(method="render", at=@At(
            value="FIELD", 
            target="Lnet/minecraft/client/option/GameOptions;debugEnabled:Z", 
            opcode = Opcodes.GETFIELD, args = {"log=false"}))
    
    private void beforeRenderDebugScreen(DrawContext context, float f, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (Silence.silencedByMe() && ConfigurationHandler.getWantIcon()) {
            int x=(client.getWindow().getScaledWidth()-32)*ConfigurationHandler.getXPercent()/100;
            int y=(client.getWindow().getScaledHeight()-32)*ConfigurationHandler.getYPercent()/100;   
            context.drawTexture(Silence.SPEAKER_TEXTURE, x, y, 0, 0, 32, 32);
        }
    }
}
