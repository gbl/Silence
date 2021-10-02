package de.giselbaer.mcmod.silence.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import de.giselbaer.mcmod.silence.ConfigurationHandler;
import de.giselbaer.mcmod.silence.Silence;
import de.guntram.mcmod.fabrictools.GuiModOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class SilenceGuiMixin extends DrawableHelper {
    @Inject(method="render", at=@At(
            value="FIELD", 
            target="Lnet/minecraft/client/option/GameOptions;debugEnabled:Z", 
            opcode = Opcodes.GETFIELD, args = {"log=false"}))
    
    private void beforeRenderDebugScreen(MatrixStack stack, float f, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (Silence.silencedByMe() && ConfigurationHandler.getWantIcon()
        ||  client.currentScreen != null && client.currentScreen instanceof GuiModOptions) {
            int x=(client.getWindow().getScaledWidth()-32)*ConfigurationHandler.getXPercent()/100;
            int y=(client.getWindow().getScaledHeight()-32)*ConfigurationHandler.getYPercent()/100;   
            RenderSystem.setShaderTexture(0, Silence.SPEAKER_TEXTURE);
            this.drawTexture(stack, x, y, 0, 0, 32, 32);
        }
    }
}
