package example.phaseportals;

import example.phaseportals.energy.PEPropertyCodec;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public final class EnergyCubeScreenHandler extends ScreenHandler {
    private final BlockPos pos;
    private final PropertyDelegate properties;

    public EnergyCubeScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, null, new ArrayPropertyDelegate(PEPropertyCodec.PROPERTY_COUNT));
    }

    public EnergyCubeScreenHandler(int syncId, PlayerInventory inventory,
            BlockPos pos, PropertyDelegate properties) {
        super(PhasePortalsMod.ENERGY_CUBE_SCREEN_HANDLER, syncId);
        checkDataCount(properties, PEPropertyCodec.PROPERTY_COUNT);
        this.pos = pos;
        this.properties = properties;
        addProperties(properties);
    }

    public long getEnergy() { return PEPropertyCodec.stored(properties, 0); }
    public long getMaxEnergy() { return PEPropertyCodec.capacity(properties, 0); }

    @Override public boolean canUse(PlayerEntity player) {
        return pos == null || (player.getWorld().getBlockState(pos).isOf(PhasePortalsMod.ENERGY_CUBE)
                && player.squaredDistanceTo(pos.toCenterPos()) <= 64.0);
    }
    @Override public boolean onButtonClick(PlayerEntity player, int id) {
        return EnergyConfigurationScreenHandler.open(player, id,
                pos != null && player.getWorld().getBlockEntity(pos) instanceof EnergyCubeBlockEntity cube
                        ? cube : null);
    }
    @Override public ItemStack quickMove(PlayerEntity player, int slot) { return ItemStack.EMPTY; }
}
