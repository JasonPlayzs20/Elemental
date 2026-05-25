package com.jason.elements;

import com.jason.Effects;
import com.jason.reactions.AffectedReaction;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;

import static com.jason.Elemental.MOD_ID;

public class AffectedElement {
    private static final AttachmentType<Integer> PYRO_TICKS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath("elemental", "pyro_ticks"),
            builder -> builder.syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
    );
    private static final AttachmentType<Integer> CRYO_TICKS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath("elemental", "cryo_ticks"),
            builder -> builder.syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
    );
    private static final AttachmentType<Integer> HYDRO_TICKS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath("elemental", "hydro_ticks"),
            builder -> builder.syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
    );
    private static final AttachmentType<Integer> ELECTRO_TICKS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath("elemental", "electro_ticks"),
            builder -> builder.syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
    );
    private static final AttachmentType<Integer> ANEMO_TICKS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath("elemental", "anemo_ticks"),
            builder -> builder.syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
    );
    private static final AttachmentType<Integer> DENDRO_TICKS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath("elemental", "dendro_ticks"),
            builder -> builder.syncWith(ByteBufCodecs.INT, AttachmentSyncPredicate.all())
    );

    public static AffectedElements getAffectedElements(AttachmentTarget target) {
        return new AffectedElements(target);
    }



    public record AffectedElements(AttachmentTarget target) {
        private static int defaultTicking = 9*20;

        public ArrayList<Elements> getAffectedElements() {
            ArrayList<Elements> elements = new ArrayList<>();
            if (getPyroTick() > 0) {
                elements.add(Elements.PYRO);
            }
            if (getCryoTick() > 0) {
                elements.add(Elements.CRYO);
            }
            if (getHydroTick() > 0) {
                elements.add(Elements.HYDRO);
            }
            if (getElectroTick() > 0) {
                elements.add(Elements.ELECTRO);
            }
            if (getAnemoTick() > 0) {
                elements.add(Elements.ANEMO);
            }
            if (getDendroTick() > 0) {
                elements.add(Elements.DENDRO);
            }
            return elements;
        }

        public void setDefaultTicking(int ticking) {
            defaultTicking = ticking;
        }

        public int getDefaultTicking() {
            return defaultTicking;
        }

        public boolean affectPyro(LivingEntity entity) {
            if (entity.level() instanceof ServerLevel level && !AffectedElement.getAffectedElements(entity).isAffectedWithPyro() && !(entity instanceof ArmorStand)) {
                Effects.textEffect("Hot", level, ChatFormatting.RED,
                        entity.getX(), entity.getY(), entity.getZ());
            }
            this.target.setAttached(PYRO_TICKS, defaultTicking);

            return true;
        }
        public boolean affectCryo(LivingEntity entity) {
            if (entity.level() instanceof ServerLevel level&& !AffectedElement.getAffectedElements(entity).isAffectedWithCryo()&& !(entity instanceof ArmorStand)) {
                Effects.textEffect("Freeze", level, ChatFormatting.WHITE,
                        entity.getX(), entity.getY(), entity.getZ());
            }
            this.target.setAttached(CRYO_TICKS, defaultTicking);

            return true;
        }
        public boolean affectHydro(LivingEntity entity) {
//            System.out.println(getHydroTick());
            if (entity.level() instanceof ServerLevel level&& !AffectedElement.getAffectedElements(entity).isAffectedWithHydro()&& !(entity instanceof ArmorStand) && !AffectedReaction.get(entity).isFrozen()) {
                Effects.textEffect("Wet", level, ChatFormatting.BLUE,
                        entity.getX(), entity.getY() , entity.getZ());
            }
            if (!AffectedReaction.get(entity).isFrozen()) {
                this.target.setAttached(HYDRO_TICKS, defaultTicking);
            }
            return true;
        }
        public boolean affectElectro(LivingEntity entity) {
            if (entity.level() instanceof ServerLevel level && !AffectedElement.getAffectedElements(entity).isAffectedWithElectro()&& !(entity instanceof ArmorStand)) {
                Effects.textEffect("Shock", level, ChatFormatting.YELLOW,
                        entity.getX(), entity.getY(), entity.getZ());
            }
            this.target.setAttached(ELECTRO_TICKS, defaultTicking);

            return true;
        }
        public boolean affectAnemo(LivingEntity entity) {

            this.target.setAttached(ANEMO_TICKS, 2);

            return true;
        }
        public boolean affectDendro(LivingEntity entity) {
            if (entity.level() instanceof ServerLevel level && !AffectedElement.getAffectedElements(entity).isAffectedWithElectro()&& !(entity instanceof ArmorStand)) {
                Effects.textEffect("Growth", level, ChatFormatting.GREEN,
                        entity.getX(), entity.getY(), entity.getZ());
            }
            this.target.setAttached(DENDRO_TICKS, defaultTicking);

            return true;
        }


        public boolean removePyro() {
            this.target.modifyAttached(PYRO_TICKS, defaultTicking -> 0);
            return true;
        }
        public boolean removeCryo() {
            this.target.modifyAttached(CRYO_TICKS, defaultTicking -> 0);
            return true;
        }
        public boolean removeHydro() {
            this.target.modifyAttached(HYDRO_TICKS, defaultTicking -> 0);
            return true;
        }
        public boolean removeElectro() {
            this.target.modifyAttached(ELECTRO_TICKS, defaultTicking -> 0);
            return true;
        }
        public boolean removeAnemo() {
            this.target.modifyAttached(ANEMO_TICKS, defaultTicking -> 0);
            return true;
        }
        public boolean removeDendro() {
            this.target.modifyAttached(DENDRO_TICKS, defaultTicking -> 0);
            return true;
        }


        public boolean removeAllElements() {
            this.target.modifyAttached(PYRO_TICKS, defaultTicking -> 0);
            this.target.modifyAttached(CRYO_TICKS, defaultTicking -> 0);
            this.target.modifyAttached(HYDRO_TICKS, defaultTicking -> 0);
            this.target.modifyAttached(ELECTRO_TICKS, defaultTicking -> 0);
            this.target.modifyAttached(ANEMO_TICKS, defaultTicking -> 0);
            this.target.modifyAttached(DENDRO_TICKS, defaultTicking -> 0);
            return true;
        }

        public boolean isAffectedWithPyro() {
            return this.target.getAttachedOrElse(PYRO_TICKS,0) > 0;
        }
        public boolean isAffectedWithCryo() {
            return this.target.getAttachedOrElse(CRYO_TICKS,0) > 0;
        }
        public boolean isAffectedWithHydro() {
            return this.target.getAttachedOrElse(HYDRO_TICKS,0) > 0;
        }
        public boolean isAffectedWithElectro() {
            return this.target.getAttachedOrElse(ELECTRO_TICKS,0) > 0;
        }
        public boolean isAffectedWithAnemo() {return this.target.getAttachedOrElse(ANEMO_TICKS,0) > 0;}
        public boolean isAffectedWithDendro() {return this.target.getAttachedOrElse(DENDRO_TICKS,0) > 0;}

        public void tick() {
            this.target.modifyAttached(PYRO_TICKS, val -> val == null ? 0 : val - 1);
            this.target.modifyAttached(CRYO_TICKS, val -> val == null ? 0 : val - 1);
            this.target.modifyAttached(HYDRO_TICKS, val -> val == null ? 0 : val - 1);
            this.target.modifyAttached(ELECTRO_TICKS, val -> val == null ? 0 : val - 1);
            this.target.modifyAttached(ANEMO_TICKS, val -> val == null ? 0 : val - 1);
            this.target.modifyAttached(DENDRO_TICKS, val -> val == null ? 0 : val - 1);
//            System.out.println(getHydroTick());
        }

        public int getPyroTick() {
            return this.target.getAttachedOrElse(PYRO_TICKS,0);
        }
        public int getCryoTick() {
            return this.target.getAttachedOrElse(CRYO_TICKS,0);
        }
        public int getHydroTick() {
            return this.target.getAttachedOrElse(HYDRO_TICKS,0);
        }
        public int getElectroTick() {
            return this.target.getAttachedOrElse(ELECTRO_TICKS,0);
        }
        public int getAnemoTick() {return this.target.getAttachedOrElse(ANEMO_TICKS,0);}
        public int getDendroTick() {return this.target.getAttachedOrElse(DENDRO_TICKS,0);}
    }
}
