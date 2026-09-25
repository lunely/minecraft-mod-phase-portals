package example.phaseportals;

import example.phaseportals.energy.EnergyCableNetwork;
import example.phaseportals.energy.PEBlockEntity;
import example.phaseportals.energy.PEPropertyCodec;
import example.phaseportals.energy.PESideMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class EnergyCubeBlockEntity extends PEBlockEntity implements NamedScreenHandlerFactory {
    private final PropertyDelegate properties = new PropertyDelegate() {
        @Override public int get(int index) { return PEPropertyCodec.part(EnergyCubeBlockEntity.this, index); }
        @Override public void set(int index, int value) {}
        @Override public int size() { return PEPropertyCodec.PROPERTY_COUNT; }
    };

    public EnergyCubeBlockEntity(BlockPos pos, BlockState state) {
        super(PhasePortalsMod.ENERGY_CUBE_BLOCK_ENTITY, pos, state, PESideMode.INPUT_OUTPUT,
                PESideMode.INPUT, PESideMode.OUTPUT, PESideMode.INPUT_OUTPUT, PESideMode.DISABLED);
    }

    public static void tick(World world, BlockPos pos, BlockState state, EnergyCubeBlockEntity cube) {
        if (cube.getStored() > 0) EnergyCableNetwork.distribute(world, pos, cube);
    }

    @Override public Text getDisplayName() { return Text.translatable("block.phaseportals.energy_cube"); }
    @Override public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return new EnergyCubeScreenHandler(syncId, inventory, pos, properties);
    }
}
