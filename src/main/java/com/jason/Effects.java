package com.jason;

import com.mojang.math.Transformation;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Effects {

    private static class PendingRemoval {
        final Entity entity;
        final long removeAtTick;
        double dx, dy, dz;

        PendingRemoval(Entity entity, long removeAtTick, double dx, double dy, double dz) {
            this.entity = entity;
            this.removeAtTick = removeAtTick;
            this.dx = dx;
            this.dy = dy;
            this.dz = dz;
        }
    }    private static final List<PendingRemoval> pendingRemovals = new ArrayList<>();

    /** Call this once during mod init via ServerTickEvents.END_SERVER_TICK */
    public static void tick(MinecraftServer server) {
        long currentTick = server.getTickCount();
        Iterator<PendingRemoval> it = pendingRemovals.iterator();
        while (it.hasNext()) {
            PendingRemoval removal = it.next();
            if (currentTick >= removal.removeAtTick) {
                removal.entity.discard();
                it.remove();
            } else {
                removal.dy -= 0.06; // gravity
                removal.entity.setPos(
                        removal.entity.getX() + removal.dx,
                        removal.entity.getY() + removal.dy,
                        removal.entity.getZ() + removal.dz
                );
            }
        }
    }

    public static void textEffect(String text, ServerLevel level, ChatFormatting color, double x, double y, double z) {
        ArmorStand armorStand = new ArmorStand(EntityType.ARMOR_STAND, level);
        armorStand.setCustomName(Component.literal(text).withStyle(color));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
//        armorStand.setNoGravity(true);

        // Set marker via data value
        armorStand.getEntityData().set(ArmorStand.DATA_CLIENT_FLAGS, (byte)(armorStand.getEntityData().get(ArmorStand.DATA_CLIENT_FLAGS) | 0x10));

        armorStand.setPos(x, y+1, z);

        Random random = new Random();
        double dx = (random.nextDouble() - 0.5) * 0.3;
        double dy = 0.15 + random.nextDouble() * 7.6;
        double dz = (random.nextDouble() - 0.5) * 0.3;
        armorStand.setDeltaMovement(dx, dy, dz);
        dy = 0.15 + random.nextDouble() * 0.1;

        level.addFreshEntity(armorStand);
        pendingRemovals.add(new PendingRemoval(armorStand, level.getServer().getTickCount() + 15, dx, dy, dz));    }
}