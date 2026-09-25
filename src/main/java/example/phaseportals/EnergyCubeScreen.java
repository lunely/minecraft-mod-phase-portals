package example.phaseportals;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public final class EnergyCubeScreen extends HandledScreen<EnergyCubeScreenHandler> {
    public EnergyCubeScreen(EnergyCubeScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundWidth = 176;
        backgroundHeight = 80;
    }

    @Override protected void init() {
        super.init();
        addDrawableChild(EnergyConfigurationButton.create(
                (width - backgroundWidth) / 2 - 24, (height - backgroundHeight) / 2 + 6, handler));
    }

    @Override protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.fill(x, y, x + 176, y + 80, 0xFF30343A);
        context.fill(x + 4, y + 4, x + 172, y + 76, 0xFFBFC0C0);
        context.fill(x + 12, y + 28, x + 164, y + 62, 0xFF25282C);
        long capacity = handler.getMaxEnergy();
        int filled = capacity <= 0 ? 0 : (int) (148 * handler.getEnergy() / capacity);
        context.fill(x + 14, y + 30, x + 14 + filled, y + 60, 0xFF5ACD70);
    }

    @Override protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(textRenderer, title, 8, 9, 0xFF303030, false);
        context.drawCenteredTextWithShadow(textRenderer,
                Text.literal(handler.getEnergy() + " / " + handler.getMaxEnergy() + " PE"),
                88, 40, 0xFFFFFFFF);
    }

    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
