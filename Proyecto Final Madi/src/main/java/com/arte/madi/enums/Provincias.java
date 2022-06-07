/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.arte.madi.enums;

/**
 *
 * @author lgsan
 */
public enum Provincias {
    Buenos_Aires("Buenos Aires"),
    CABA("Ciudad Autónoma de Buenos Aires"),
    Catamarca("Catamarca"),
    Chaco("Chaco"),
    Chubut("Chubut"),
    Cordoba("Córdoba"),
    Corrientes("Corrientes"),
    Entre_Rios("Entre Ríos"),
    Formosa("Formosa"), 
    Jujuy("Jujuy"), 
    La_Pampa("La Pampa"),
    La_Rioja("La Rioja"), 
    Mendoza("Mendoza"), 
    Misiones("Misiones"), 
    Neuquen("Neuquén"), 
    Rio_Negro("Río Negro"), 
    Salta("Salta"), 
    San_Juan("San Juan"), 
    San_Luis("San Luis"), 
    Santa_Cruz("Santa Cruz"), 
    Santa_Fe("Santa Fe"), 
    Santiago_del_Estero("Santiago del Estero"), 
    Tierra_del_Fuego("Tierra del Fuego"),
    Tucuman("Tucumán");
    
    private final String displayName;

    Provincias(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() { return displayName; }

    // Optionally and/or additionally, toString.
  //  @Override public String toString() { return displayName; }
    
}
