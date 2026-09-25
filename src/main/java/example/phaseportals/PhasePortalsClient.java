package example.phaseportals;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;

public final class PhasePortalsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(PhasePortalsMod.INFUSION_STATION_SCREEN_HANDLER, InfusionStationScreen::new);
        HandledScreens.register(PhasePortalsMod.CRUSHER_SCREEN_HANDLER, CrusherScreen::new);
        HandledScreens.register(PhasePortalsMod.ELECTRIC_FURNACE_SCREEN_HANDLER, ElectricFurnaceScreen::new);
        HandledScreens.register(PhasePortalsMod.COAL_GENERATOR_SCREEN_HANDLER, CoalGeneratorScreen::new);
        HandledScreens.register(PhasePortalsMod.CREATIVE_ENERGY_CUBE_SCREEN_HANDLER,
                CreativeEnergyCubeScreen::new);
        HandledScreens.register(PhasePortalsMod.ENERGY_CUBE_SCREEN_HANDLER, EnergyCubeScreen::new);
        HandledScreens.register(PhasePortalsMod.ENERGY_CONFIGURATION_SCREEN_HANDLER,
                EnergyConfigurationScreen::new);
        HandledScreens.register(PhasePortalsMod.TELEPORT_SCREEN_HANDLER, TeleportScreen::new);
        HandledScreens.register(PhasePortalsMod.INTERDIMENSIONAL_TELEPORT_SCREEN_HANDLER,
                InterdimensionalTeleportScreen::new);
        TeleportClientNetworking.register();
        InterdimensionalTeleportClientNetworking.register();
        InterdimensionalTerrainClient.register();
        BlockRenderLayerMap.INSTANCE.putBlock(PhasePortalsMod.PORTAL_PLANE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(PhasePortalsMod.INTERDIMENSIONAL_PORTAL_PLANE,
                RenderLayer.getTranslucent());
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) ->
                tintIndex == 0 ? PortalColors.rgb(state.get(PortalPlaneBlock.COLOR)) : 0xFFFFFF,
                PhasePortalsMod.PORTAL_PLANE);
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) ->
                tintIndex == 0 ? PortalColors.rgb(state.get(InterdimensionalPortalPlaneBlock.COLOR)) : 0xFFFFFF,
                PhasePortalsMod.INTERDIMENSIONAL_PORTAL_PLANE);
    }
}
