package com.jason.elements;

public class Hydro extends Element{
    private double damageIncreaseHydroVaporize = 2.0;

    public Hydro(Elements element) {
        super(Elements.HYDRO);
    }
    public double hydroVaporize(double damage) {
        return damage * damageIncreaseHydroVaporize;
    }

}
