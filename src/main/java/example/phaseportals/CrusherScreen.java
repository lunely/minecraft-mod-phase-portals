package example.phaseportals;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public final class CrusherScreen extends HandledScreen<CrusherScreenHandler> {
    public CrusherScreen(CrusherScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundWidth = 176;
        backgroundHeight = 166;
        playerInventoryTitleY = 72;
    }

    @Override protected void init() {
        super.init();
        addDrawableChild(EnergyConfigurationButton.create(
                (width - backgroundWidth) / 2 - 24, (height - backgroundHeight) / 2 + 6, handler));
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.fill(x, y, x + 176, y + 166, 0xFF30343A);
        context.fill(x + 4, y + 4, x + 172, y + 162, 0xFFBFC0C0);
        context.fill(x + 6, y + 6, x + 170, y + 80, 0xFFADB0B2);
        drawSlot(context, x + 43, y + 34);
        drawSlot(context, x + 115, y + 34);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) drawSlot(context, x + 7 + col * 18, y + 83 + row * 18);
        }
        for (int col = 0; col < 9; col++) drawSlot(context, x + 7 + col * 18, y + 141);
        context.fill(x + 68, y + 39, x + 106, y + 47, 0xFF393D42);
        int progress = handler.getProcessTime() == 0 ? 0
                : 38 * handler.getProgress() / handler.getProcessTime();
        context.fill(x + 68, y + 39, x + 68 + progress, y + 47, 0xFF9564B2);
        EnergyBarRenderer.draw(context, x - 20, y + 20, handler.getEnergy(), handler.getMaxEnergy());
    }

    private static void drawSlot(DrawContext context, int x, int y) {
        context.fill(x, y, x + 18, y + 18, 0xFF55575A);
        context.fill(x + 1, y + 1, x + 17, y + 17, 0xFF25282C);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
        EnergyBarRenderer.drawTooltip(context, textRenderer, (width - backgroundWidth) / 2 - 20,
                (height - backgroundHeight) / 2 + 20, mouseX, mouseY,
                handler.getEnergy(), handler.getMaxEnergy());
    }
}
