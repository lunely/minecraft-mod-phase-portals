package example.phaseportals.jei;

import example.phaseportals.InfusionStationBlockEntity;
import example.phaseportals.PhasePortalsMod;
import example.phaseportals.InfusionResource;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public final class InfusionJeiCategory implements IRecipeCategory<InfusionStationBlockEntity.InfusionRecipe> {
    private final IDrawable icon;

    public InfusionJeiCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(PhasePortalsMod.INFUSION_STATION));
    }

    @Override public RecipeType<InfusionStationBlockEntity.InfusionRecipe> getRecipeType() {
        return InfusionJeiPlugin.TYPE;
    }

    @Override public Text getTitle() {
        return Text.translatable("block.phaseportals.infusion_station");
    }

    @Override public IDrawable getIcon() { return icon; }
    @Override public int getWidth() { return 142; }
    @Override public int getHeight() { return 76; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, InfusionStationBlockEntity.InfusionRecipe recipe,
            IFocusGroup focuses) {
        builder.addInputSlot(43, 22).setStandardSlotBackground()
                .addItemStack(new ItemStack(recipe.resource().item()));
        builder.addInputSlot(79, 22).setStandardSlotBackground().addItemStack(new ItemStack(recipe.input()));
        builder.addOutputSlot(115, 22).setStandardSlotBackground().addItemStack(new ItemStack(recipe.output()));
    }

    @Override
    public void draw(InfusionStationBlockEntity.InfusionRecipe recipe, IRecipeSlotsView slots,
            DrawContext context, double mouseX, double mouseY) {
        context.fill(0, 0, 142, 76, 0xFF30343A);
        context.fill(2, 2, 140, 74, 0xFFADB0B2);
        context.fill(14, 8, 30, 52, 0xFF25282C);
        context.fill(16, 10, 28, 50, InfusionResource.NONE.barColor());
        int fillHeight = Math.max(1, 40 * recipe.units() / InfusionStationBlockEntity.MAX_INFUSION);
        context.fill(16, 50 - fillHeight, 28, 50, recipe.resource().barColor());
        context.fill(103, 28, 111, 35, 0xFF55575A);
        context.fill(108, 25, 114, 38, 0xFF55575A);
        var textRenderer = MinecraftClient.getInstance().textRenderer;
        drawCenteredLabel(context, textRenderer, "gui.phaseportals.infusion", 52);
        drawCenteredLabel(context, textRenderer, "gui.phaseportals.base", 88);
        drawCenteredLabel(context, textRenderer, "gui.phaseportals.output", 124);
        context.drawText(textRenderer, recipe.units() + " / " + InfusionStationBlockEntity.MAX_INFUSION,
                8, 60, 0xFF303030, false);
    }

    private static void drawCenteredLabel(DrawContext context, net.minecraft.client.font.TextRenderer renderer,
            String key, int centerX) {
        Text label = Text.translatable(key);
        context.drawText(renderer, label, centerX - renderer.getWidth(label) / 2, 44, 0xFF303030, false);
    }
}
