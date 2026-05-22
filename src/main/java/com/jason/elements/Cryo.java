package com.jason.elements;

public class Cryo extends Element{
    private double damageIncreaseCryoMelt = 2.0;

    public Cryo(Elements element) {
        super(Elements.CRYO);
    }

    public double cryoMelt(double damage) {
        return damage * damageIncreaseCryoMelt;
    }
}
