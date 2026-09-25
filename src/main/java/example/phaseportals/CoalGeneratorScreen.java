package example.phaseportals;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public final class CoalGeneratorScreen extends HandledScreen<CoalGeneratorScreenHandler> {
    public CoalGeneratorScreen(CoalGeneratorScreenHandler handler, PlayerInventory inventory, Text title) {
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

    @Override protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.fill(x, y, x + 176, y + 166, 0xFF373737);
        context.fill(x + 3, y + 3, x + 173, y + 163, 0xFFC6C6C6);
        context.fill(x + 5, y + 5, x + 171, y + 161, 0xFFE0E0E0);
        drawSlot(context, x + 79, y + 52);
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                drawSlot(context, x + 7 + col * 18, y + 83 + row * 18);
        for (int col = 0; col < 9; col++) drawSlot(context, x + 7 + col * 18, y + 141);

        // Furnace-style flame. Its height represents the remaining burn time.
        context.fill(x + 81, y + 31, x + 95, y + 47, 0xFF555555);
        context.fill(x + 83, y + 33, x + 93, y + 45, 0xFF332B25);
        int fuelTime = handler.getFuelTime();
        int filled = fuelTime <= 0 ? 0 : Math.clamp(
                (int) Math.ceil(12.0 * handler.getBurnTime() / fuelTime), 0, 12);
        if (filled > 0)
            context.fill(x + 83, y + 45 - filled, x + 93, y + 45, 0xFFFF9B32);
        EnergyBarRenderer.draw(context, x - 20, y + 20, handler.getEnergy(), handler.getMaxEnergy());
    }

    private static void drawSlot(DrawContext context, int x, int y) {
        context.fill(x, y, x + 18, y + 18, 0xFF555555);
        context.fill(x + 1, y + 1, x + 17, y + 17, 0xFF8B8B8B);
    }

    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawMouseoverTooltip(context, mouseX, mouseY);
        EnergyBarRenderer.drawTooltip(context, textRenderer, x - 20, y + 20,
                mouseX, mouseY, handler.getEnergy(), handler.getMaxEnergy());
        if (mouseX >= x + 81 && mouseX < x + 95 && mouseY >= y + 31 && mouseY < y + 47)
            context.drawTooltip(textRenderer, Text.translatable("gui.phaseportals.coal_generator.burn",
                    handler.getBurnTime(), handler.getFuelTime()), mouseX, mouseY);
    }
}
