package example.phaseportals;

import example.phaseportals.energy.EnergyCableNetwork;
import example.phaseportals.energy.PEBlockEntity;
import example.phaseportals.energy.PEPropertyCodec;
import example.phaseportals.energy.PESideMode;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class CoalGeneratorBlockEntity extends PEBlockEntity
        implements Inventory, NamedScreenHandlerFactory {
    public static final long PE_PER_TICK = 120;
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private int burnTime;
    private int fuelTime;
    private final PropertyDelegate properties = new PropertyDelegate() {
        @Override public int get(int index) {
            return index == 0 ? burnTime : index == 1 ? fuelTime
                    : index >= 2 && index < 6 ? PEPropertyCodec.part(CoalGeneratorBlockEntity.this, index - 2) : 0;
        }
        @Override public void set(int index, int value) {
            if (index == 0) burnTime = value;
            if (index == 1) fuelTime = value;
        }
        @Override public int size() { return 6; }
    };

    public CoalGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(PhasePortalsMod.COAL_GENERATOR_BLOCK_ENTITY, pos, state,
                PESideMode.OUTPUT, PESideMode.OUTPUT, PESideMode.DISABLED);
    }

    public static void tick(World world, BlockPos pos, BlockState state, CoalGeneratorBlockEntity generator) {
        EnergyCableNetwork.distribute(world, pos, generator);
        if (generator.getCapacity() - generator.getStored() < PE_PER_TICK) return;
        if (generator.burnTime == 0) {
            ItemStack fuel = generator.items.get(0);
            int duration = fuelTime(fuel);
            if (duration == 0) return;
            generator.burnTime = duration;
            generator.fuelTime = duration;
            net.minecraft.item.Item remainder = fuel.getItem().getRecipeRemainder();
            fuel.decrement(1);
            if (fuel.isEmpty() && remainder != null)
                generator.items.set(0, new ItemStack(remainder));
        }
        generator.burnTime--;
        generator.insert(PE_PER_TICK, false);
        generator.markDirty();
    }

    private static int fuelTime(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        return AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(stack.getItem(), 0);
    }

    @Override public int size() { return 1; }
    @Override public boolean isEmpty() { return items.get(0).isEmpty(); }
    @Override public ItemStack getStack(int slot) { return items.get(slot); }
    @Override public ItemStack removeStack(int slot, int amount) {
        ItemStack removed = Inventories.splitStack(items, slot, amount);
        if (!removed.isEmpty()) markDirty();
        return removed;
    }
    @Override public ItemStack removeStack(int slot) {
        ItemStack removed = Inventories.removeStack(items, slot);
        if (!removed.isEmpty()) markDirty();
        return removed;
    }
    @Override public void setStack(int slot, ItemStack stack) { items.set(slot, stack); markDirty(); }
    @Override public void clear() { items.clear(); markDirty(); }
    @Override public boolean canPlayerUse(PlayerEntity player) { return Inventory.canPlayerUse(this, player); }
    @Override public boolean isValid(int slot, ItemStack stack) { return slot == 0 && fuelTime(stack) > 0; }

    @Override protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.readNbt(nbt, lookup);
        items.clear();
        Inventories.readNbt(nbt, items, lookup);
        burnTime = Math.max(0, nbt.getInt("BurnTime"));
        fuelTime = Math.max(0, nbt.getInt("FuelTime"));
    }
    @Override protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.writeNbt(nbt, lookup);
        Inventories.writeNbt(nbt, items, lookup);
        nbt.putInt("BurnTime", burnTime);
        nbt.putInt("FuelTime", fuelTime);
    }
    @Override public Text getDisplayName() { return Text.translatable("block.phaseportals.coal_generator"); }
    @Override public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return new CoalGeneratorScreenHandler(syncId, inventory, this, properties);
    }
}
