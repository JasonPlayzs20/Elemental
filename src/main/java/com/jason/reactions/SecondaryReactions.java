package com.jason.reactions;

import com.jason.elements.AffectedElement;
import com.jason.elements.Cryo;
import com.jason.elements.Dendro;
import com.jason.elements.Elements;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.jason.elements.Elements.*;
import static com.jason.reactions.Reaction.*;
import static com.jason.reactions.Reactions.*;

public class SecondaryReactions {

    private static Map<ReactionPair, SecondaryReactionFunction> secondaryReactionMap = new HashMap<>();

    public static void initSecondaryReactions() {
        secondaryReactionMap.put(new ReactionPair(FROZEN,PYRO), ((entity, damage, lastElement) -> melt(entity, damage, lastElement)));
        secondaryReactionMap.put(new ReactionPair(FROZEN,HYDRO), ((entity, damage, lastElement) -> melt(entity, damage, lastElement)));

        secondaryReactionMap.put(new ReactionPair(BURNING,HYDRO), ((entity, damage, lastElement) -> vaporize(entity, damage, lastElement)));
        secondaryReactionMap.put(new ReactionPair(BURNING, CRYO), ((entity, damage, lastElement) -> melt(entity, damage, lastElement)));
        secondaryReactionMap.put(new ReactionPair(BURNING, ELECTRO), ((entity, damage, lastElement) -> overload(entity, damage)));
    }



    public static double calculateSecondaryReactions(LivingEntity entity, double damage, Elements lastElement) {
        if (entity.isAlive() && !entity.level().isClientSide() && !(entity instanceof ArmorStand)) {
            ArrayList<Elements> elements = AffectedElement.getAffectedElements(entity).getAffectedElements();
            ArrayList<Reactions> reactions = AffectedReaction.get(entity).getAffectedElements();
            for (int i = 0; i < elements.size(); i++) {
                for (int j = 0; j < reactions.size(); j++) {
                    ReactionPair pair = new ReactionPair(reactions.get(j), elements.get(i));
                    SecondaryReactionFunction function = secondaryReactionMap.get(pair);
                    if (function != null) {
                        return function.apply(entity, damage, lastElement);
                    }
                }
            }
        }
        return 0;
    }



    private record ReactionPair(Reactions a, Elements b) {

    }

    @FunctionalInterface
    interface SecondaryReactionFunction {
        double apply(LivingEntity entity, double damage, Elements lastElement);
    }
}
