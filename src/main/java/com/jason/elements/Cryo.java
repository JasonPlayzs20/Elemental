package com.jason.elements;

public class Cryo extends Element{
    private static double damageIncreaseCryoMelt = 2.0;

    public Cryo(Elements element) {
        super(Elements.CRYO);
    }

    public static double cryoMelt() {
        return  damageIncreaseCryoMelt;
    }
}
