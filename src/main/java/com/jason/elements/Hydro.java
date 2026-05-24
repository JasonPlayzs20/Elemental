package com.jason.elements;

public class Hydro extends Element{
    private static double damageIncreaseHydroVaporize = 2.0;

    public Hydro(Elements element) {
        super(Elements.HYDRO);
    }
    public static double hydroVaporize() {
        return  damageIncreaseHydroVaporize;
    }

}
