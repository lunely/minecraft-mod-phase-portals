package example.phaseportals.mixin;

import example.phaseportals.InterdimensionalTerrainClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DownloadingTerrainScreen.class)
public abstract class DownloadingTerrainScreenMixin extends Screen {
    private static final Identifier PHASEPORTALS_DIRT = Identifier.ofVanilla("textures/block/dirt.png");

    protected DownloadingTerrainScreenMixin() { super(net.minecraft.text.Text.empty()); }

    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    private void phaseportals$renderDirt(DrawContext context, int mouseX, int mouseY, float delta,
            CallbackInfo ci) {
        if (!InterdimensionalTerrainClient.isActive()) return;
        Screen.renderBackgroundTexture(context, PHASEPORTALS_DIRT, 0, 0, 0, 0, width, height);
        ci.cancel();
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void phaseportals$clearTransition(CallbackInfo ci) {
        InterdimensionalTerrainClient.clear();
    }
}
