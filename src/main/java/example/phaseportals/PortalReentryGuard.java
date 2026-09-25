package example.phaseportals;

import java.util.Map;
import java.util.WeakHashMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

/** Blocks a return trip until the player has completely left the destination plane. */
final class PortalReentryGuard {
    private static final double PLANE_MIN = 7.0 / 16.0;
    private static final double PLANE_MAX = 9.0 / 16.0;
    private static final Map<ServerPlayerEntity, ExitPortal> EXIT_PORTALS = new WeakHashMap<>();

    private PortalReentryGuard() {}

    static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> EXIT_PORTALS.entrySet().removeIf(entry -> {
            ServerPlayerEntity player = entry.getKey();
            ExitPortal exit = entry.getValue();
            return player == null || player.isRemoved() || player.getWorld() != exit.world()
                    || !intersectsExitPlane(player, exit);
        }));
    }

    static boolean isBlocked(ServerPlayerEntity player) {
        return EXIT_PORTALS.containsKey(player);
    }

    static void mark(ServerPlayerEntity player, ServerWorld world, BlockPos controller) {
        EXIT_PORTALS.put(player, new ExitPortal(world, controller.toImmutable()));
    }

    static void clear(ServerPlayerEntity player) {
        EXIT_PORTALS.remove(player);
    }

    private static boolean intersectsExitPlane(ServerPlayerEntity player, ExitPortal exit) {
        Box playerBox = player.getBoundingBox();
        ServerWorld world = exit.world();
        for (int x = (int) Math.floor(playerBox.minX); x <= (int) Math.floor(playerBox.maxX); x++) {
            for (int y = (int) Math.floor(playerBox.minY); y <= (int) Math.floor(playerBox.maxY); y++) {
                for (int z = (int) Math.floor(playerBox.minZ); z <= (int) Math.floor(playerBox.maxZ); z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    if (state.isOf(PhasePortalsMod.PORTAL_PLANE)) {
                        if (!(world.getBlockEntity(pos) instanceof PortalPlaneBlockEntity plane)
                                || !plane.belongsTo(exit.controller())) continue;
                    } else if (state.isOf(PhasePortalsMod.INTERDIMENSIONAL_PORTAL_PLANE)) {
                        if (!(world.getBlockEntity(pos) instanceof InterdimensionalPortalPlaneBlockEntity plane)
                                || !plane.belongsTo(exit.controller())) continue;
                    } else continue;
                    if (intersectsPlane(playerBox, state, pos)) return true;
                }
            }
        }
        return false;
    }

    static boolean intersectsPlane(Box playerBox, BlockState state, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        Direction.Axis axis = state.get(Properties.HORIZONTAL_AXIS);
        Box planeBox = axis == Direction.Axis.X
                ? new Box(x, y, z + PLANE_MIN, x + 1, y + 1, z + PLANE_MAX)
                : new Box(x + PLANE_MIN, y, z, x + PLANE_MAX, y + 1, z + 1);
        return playerBox.intersects(planeBox);
    }

    private record ExitPortal(ServerWorld world, BlockPos controller) {}
}
