package com.jason.elements;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;

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
            return elements;
        }

        public void setDefaultTicking(int ticking) {
            defaultTicking = ticking;
        }

        public int getDefaultTicking() {
            return defaultTicking;
        }

        public boolean affectPyro() {
            this.target.modifyAttached(PYRO_TICKS, defaultTicking -> defaultTicking);
            return true;
        }
        public boolean affectCryo() {
            this.target.modifyAttached(CRYO_TICKS, defaultTicking -> defaultTicking);
            return true;
        }
        public boolean affectHydro() {
            this.target.modifyAttached(HYDRO_TICKS, defaultTicking -> defaultTicking);
            return true;
        }
        public boolean affectElectro() {
            this.target.modifyAttached(ELECTRO_TICKS, defaultTicking -> defaultTicking);
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

        public boolean removeAllElements() {
            this.target.modifyAttached(PYRO_TICKS, defaultTicking -> 0);
            this.target.modifyAttached(CRYO_TICKS, defaultTicking -> 0);
            this.target.modifyAttached(HYDRO_TICKS, defaultTicking -> 0);
            this.target.modifyAttached(ELECTRO_TICKS, defaultTicking -> 0);
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

        public void tick() {
            this.target.modifyAttached(PYRO_TICKS, defaultTicking -> defaultTicking - 1);
            this.target.modifyAttached(CRYO_TICKS, defaultTicking -> defaultTicking - 1);
            this.target.modifyAttached(HYDRO_TICKS, defaultTicking -> defaultTicking - 1);
            this.target.modifyAttached(ELECTRO_TICKS, defaultTicking -> defaultTicking - 1);
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
    }
}
