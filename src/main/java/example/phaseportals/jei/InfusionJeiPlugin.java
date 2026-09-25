package example.phaseportals.jei;

import example.phaseportals.InfusionStationBlockEntity;
import example.phaseportals.CrusherBlockEntity;
import example.phaseportals.PhasePortalsMod;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.Identifier;

@JeiPlugin
public final class InfusionJeiPlugin implements IModPlugin {
    public static final RecipeType<InfusionStationBlockEntity.InfusionRecipe> TYPE =
            RecipeType.create(PhasePortalsMod.MOD_ID, "infusion", InfusionStationBlockEntity.InfusionRecipe.class);
    public static final RecipeType<CrusherBlockEntity.CrusherRecipe> CRUSHER_TYPE =
            RecipeType.create(PhasePortalsMod.MOD_ID, "crusher", CrusherBlockEntity.CrusherRecipe.class);

    @Override public Identifier getPluginUid() {
        return Identifier.of(PhasePortalsMod.MOD_ID, "infusion_jei");
    }

    @Override public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new InfusionJeiCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CrusherJeiCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(TYPE, InfusionStationBlockEntity.RECIPES);
        registration.addRecipes(CRUSHER_TYPE, CrusherBlockEntity.RECIPES);
    }

    @Override public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(PhasePortalsMod.INFUSION_STATION, TYPE);
        registration.addRecipeCatalyst(PhasePortalsMod.CRUSHER, CRUSHER_TYPE);
        registration.addRecipeCatalyst(PhasePortalsMod.ELECTRIC_FURNACE, RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(PhasePortalsMod.ELECTRIC_FURNACE, RecipeTypes.BLASTING);
    }
}
