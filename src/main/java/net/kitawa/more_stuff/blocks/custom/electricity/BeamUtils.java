package net.kitawa.more_stuff.blocks.custom.electricity;

import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class BeamUtils {

    // === Helper Methods ===
    static Vec3 findBeamEnd(Level level, Vec3 start, Vec3 end, int maxAllowed) {
        int solidCount = 0;
        Vec3 dir = end.subtract(start);
        double length = dir.length();
        if (length < 0.0001) return start;
        dir = dir.normalize();
        double step = 0.5;
        Vec3 lastFree = start;
        int steps = (int) (length / step);
        for (int i = 0; i <= steps; i++) {
            Vec3 pos = start.add(dir.scale(i * step));
            BlockState state = level.getBlockState(BlockPos.containing(pos));
            if (!state.isAir() && state.isSolid() && !state.is(ModBlockTags.ELECTRICITY_CAN_TRAVEL_THROUGH)) {
                solidCount++;
                if (solidCount > maxAllowed) return lastFree;
            } else lastFree = pos;
        }
        return end;
    }

    static Vec3 adjustRodEnd(BlockState rodState, Vec3 rodCenter) {
        Direction facing = rodState.getValue(LightningRodBlock.FACING);
        return switch (facing) {
            case UP -> rodCenter.add(0, 0.5, 0);
            case DOWN -> rodCenter.subtract(0, 0.5, 0);
            case NORTH -> rodCenter.subtract(0, 0, 0.5);
            case SOUTH -> rodCenter.add(0, 0, 0.5);
            case WEST -> rodCenter.subtract(0.5, 0, 0);
            case EAST -> rodCenter.add(0.5, 0, 0);
        };
    }

    static List<Vec3> buildPath(RandomSource random, Vec3 start, Vec3 end, int charge) {
        List<Vec3> points = new ArrayList<>();
        points.add(start);
        int count = Math.max((int)(charge * 0.75), 4);
        for (int i = 0; i < count; i++) {
            double t = (i + 1) / (double)(count + 1);
            points.add(start.lerp(end, t).add(random.nextDouble() - 0.5,
                    random.nextDouble() - 0.5,
                    random.nextDouble() - 0.5));
        }
        points.add(end);
        return points;
    }

    static void spawnParticlesAlongPath(ServerLevel server, List<Vec3> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            Vec3 a = path.get(i);
            Vec3 b = path.get(i + 1);
            for (int s = 0; s <= 8; s++) {
                double t = (double)s / 8;
                server.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                        Mth.lerp(t, a.x, b.x),
                        Mth.lerp(t, a.y, b.y),
                        Mth.lerp(t, a.z, b.z),
                        1, 0, 0, 0, 0);
            }
        }
    }
}
