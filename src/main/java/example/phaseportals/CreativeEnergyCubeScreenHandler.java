package example.phaseportals;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public final class CreativeEnergyCubeScreenHandler extends ScreenHandler {
    private final BlockPos pos;

    public CreativeEnergyCubeScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, null);
    }

    public CreativeEnergyCubeScreenHandler(int syncId, PlayerInventory inventory, BlockPos pos) {
        super(PhasePortalsMod.CREATIVE_ENERGY_CUBE_SCREEN_HANDLER, syncId);
        this.pos = pos;
    }

    @Override public boolean canUse(PlayerEntity player) {
        return pos == null || (player.getWorld().getBlockState(pos)
                .isOf(PhasePortalsMod.CREATIVE_ENERGY_CUBE)
                && player.squaredDistanceTo(pos.toCenterPos()) <= 64.0);
    }

    @Override public boolean onButtonClick(PlayerEntity player, int id) {
        return EnergyConfigurationScreenHandler.open(player, id,
                pos != null && player.getWorld().getBlockEntity(pos) instanceof CreativeEnergyCubeBlockEntity cube
                        ? cube : null);
    }

    @Override public ItemStack quickMove(PlayerEntity player, int slot) { return ItemStack.EMPTY; }
}
