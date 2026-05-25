package com.jason.reactions;

import com.jason.elements.AffectedElement;
import com.jason.elements.Dendro;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;

import static com.jason.Elemental.MOD_ID;

public class AffectedReaction {

    private static final AttachmentType<Integer> FROZEN_TICKS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MOD_ID, "frozen"),
            builder -> builder.syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
    );
    private static final AttachmentType<Integer> BURNING_TICKS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MOD_ID, "burning"),
            builder -> builder.syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
    );
//    private static final AttachmentType<Integer> FROZEN_TICKS = AttachmentRegistry.create(
//            Identifier.fromNamespaceAndPath(MOD_ID, "frozen"),
//            builder -> builder.syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
//    );


    public static AffectedReactions get(AttachmentTarget target) {
        return new AffectedReactions(target);
    }

    public record AffectedReactions(AttachmentTarget target) {
        private static int defaultTicks = 5 * 20;
        private static double burningDamage = 0;

        public double getBurningDamage() {
            return burningDamage;
        }

        public void setBurningDamage(double burningDamage) {
            AffectedReactions.burningDamage = burningDamage;
        }

        public ArrayList<Reactions> getAffectedElements() {
            ArrayList<Reactions> reactions = new ArrayList<>();
            if (getFrozenTick() > 0) {
                reactions.add(Reactions.FROZEN);
            }
            if (getBurningTick() > 0) {
                reactions.add(Reactions.BURNING);
            }
            return reactions;
        }

        public boolean affectFrozen(int ticks) {
            this.target.setAttached(FROZEN_TICKS, ticks);
            return true;
        }

        public boolean affectBurning(int ticks, double damage) {
            this.target.setAttached(BURNING_TICKS, defaultTicks);
            burningDamage = damage* Dendro.getElementalMastery();
            return true;
        }


        public int getFrozenTick() {
            return this.target.getAttachedOrElse(FROZEN_TICKS, 0);
        }

        public int getBurningTick() {
            return this.target.getAttachedOrElse(BURNING_TICKS, 0);
        }

        public void tick() {
            this.target.modifyAttached(FROZEN_TICKS, val -> val == null ? 0 : val - 1);
            this.target.modifyAttached(BURNING_TICKS, val -> val == null ? 0 : val - 1);
        }

        public boolean isFrozen() {
            return this.target.getAttachedOrElse(FROZEN_TICKS, 0) > 0;
        }

        public boolean isBurning() {
            return this.target.getAttachedOrElse(BURNING_TICKS, 0) > 0;
        }

        public boolean removeFrozen() {
            this.target.modifyAttached(FROZEN_TICKS, defaultTicking -> 0);
            return true;
        }

        public boolean removeBurning() {
            this.target.modifyAttached(BURNING_TICKS, defaultTicking -> 0);
            return true;
        }

        public boolean removeAllReactions() {
            this.target.modifyAttached(FROZEN_TICKS, defaultTicking -> 0);
            this.target.modifyAttached(BURNING_TICKS, defaultTicking -> 0);
            return true;
        }

    }
}
