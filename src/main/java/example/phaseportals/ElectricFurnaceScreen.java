package example.phaseportals;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public final class ElectricFurnaceScreen extends HandledScreen<ElectricFurnaceScreenHandler> {
    public ElectricFurnaceScreen(ElectricFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
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
        context.fill(x, y, x + 176, y + 166, 0xFF373737);
        context.fill(x + 3, y + 3, x + 173, y + 163, 0xFFC6C6C6);
        context.fill(x + 5, y + 5, x + 171, y + 161, 0xFFE0E0E0);
        drawSlot(context, x + 43, y + 34);
        drawSlot(context, x + 115, y + 34);
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) drawSlot(context, x + 7 + col * 18, y + 83 + row * 18);
        }
        for (int col = 0; col < 9; col++) drawSlot(context, x + 7 + col * 18, y + 141);
        // Vanilla-style right arrow, filled from left to right as processing advances.
        arrow(context, x + 70, y + 38, 0xFF8B8B8B, 24);
        int progress = handler.getProcessTime() == 0 ? 0
                : 24 * handler.getProgress() / handler.getProcessTime();
        arrow(context, x + 70, y + 38, 0xFFCA8B38, progress);
        EnergyBarRenderer.draw(context, x - 20, y + 20, handler.getEnergy(), handler.getMaxEnergy());
    }

    private static void arrow(DrawContext context, int x, int y, int color, int width) {
        if (width <= 0) return;
        context.fill(x, y + 2, x + Math.min(width, 18), y + 8, color);
        if (width > 18) {
            context.fill(x + 18, y, x + Math.min(width, 20), y + 10, color);
            if (width > 20) context.fill(x + 20, y + 2, x + Math.min(width, 22), y + 8, color);
            if (width > 22) context.fill(x + 22, y + 4, x + width, y + 6, color);
        }
    }

    private static void drawSlot(DrawContext context, int x, int y) {
        context.fill(x, y, x + 18, y + 18, 0xFF555555);
        context.fill(x + 1, y + 1, x + 17, y + 17, 0xFF8B8B8B);
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
