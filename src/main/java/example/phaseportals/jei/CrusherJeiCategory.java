package example.phaseportals.jei;

import example.phaseportals.CrusherBlockEntity;
import example.phaseportals.PhasePortalsMod;
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

public final class CrusherJeiCategory implements IRecipeCategory<CrusherBlockEntity.CrusherRecipe> {
    private final IDrawable icon;

    public CrusherJeiCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableItemStack(new ItemStack(PhasePortalsMod.CRUSHER));
    }

    @Override public RecipeType<CrusherBlockEntity.CrusherRecipe> getRecipeType() {
        return InfusionJeiPlugin.CRUSHER_TYPE;
    }

    @Override public Text getTitle() { return Text.translatable("block.phaseportals.crusher"); }
    @Override public IDrawable getIcon() { return icon; }
    @Override public int getWidth() { return 142; }
    @Override public int getHeight() { return 64; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrusherBlockEntity.CrusherRecipe recipe,
            IFocusGroup focuses) {
        builder.addInputSlot(43, 22).setStandardSlotBackground().addItemStack(new ItemStack(recipe.input()));
        builder.addOutputSlot(115, 22).setStandardSlotBackground().addItemStack(new ItemStack(recipe.output()));
    }

    @Override
    public void draw(CrusherBlockEntity.CrusherRecipe recipe, IRecipeSlotsView slots,
            DrawContext context, double mouseX, double mouseY) {
        context.fill(0, 0, 142, 64, 0xFF30343A);
        context.fill(2, 2, 140, 62, 0xFFADB0B2);
        context.fill(68, 27, 104, 35, 0xFF393D42);
        context.fill(68, 29, 101, 33, 0xFF9564B2);
        context.fill(101, 25, 109, 37, 0xFF9564B2);
        var renderer = MinecraftClient.getInstance().textRenderer;
        drawCenteredLabel(context, renderer, "gui.phaseportals.base", 52);
        drawCenteredLabel(context, renderer, "gui.phaseportals.output", 124);
    }

    private static void drawCenteredLabel(DrawContext context, net.minecraft.client.font.TextRenderer renderer,
            String key, int centerX) {
        Text label = Text.translatable(key);
        context.drawText(renderer, label, centerX - renderer.getWidth(label) / 2, 44, 0xFF303030, false);
    }
}
