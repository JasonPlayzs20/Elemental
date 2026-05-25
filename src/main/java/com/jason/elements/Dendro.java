package com.jason.elements;

public class Dendro extends Element{
    private static double elementalMastery = 0.3;
    public Dendro(Elements element) {
        super(Elements.DENDRO);
    }

    public static double getElementalMastery() {
        return elementalMastery;
    }

    public static void setElementalMastery(double elementalMastery) {
        Dendro.elementalMastery = elementalMastery;
    }
}
