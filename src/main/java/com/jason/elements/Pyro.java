package com.jason.elements;

public class Pyro extends Element{
    private double damageIncreasePyroVape = 1.5;
    private double damageIncreasePyroMelt = 2.0;

    public Pyro() {
        super(Elements.PYRO);
    }

    public double pyroVaporize(double damage) {
        return damage * damageIncreasePyroVape;
    }

    public double pyroMelt(double damage) {
        return damage * damageIncreasePyroMelt;
    }
}
