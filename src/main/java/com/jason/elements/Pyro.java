package com.jason.elements;

public class Pyro extends Element{
    private static double damageIncreasePyroVape = 1.5;
    private static double damageIncreasePyroMelt = 2.0;

    public Pyro() {
        super(Elements.PYRO);
    }

    public static double pyroVaporize() {
        return  damageIncreasePyroVape;
    }

    public static double pyroMelt() {
        return  damageIncreasePyroMelt;
    }
}
