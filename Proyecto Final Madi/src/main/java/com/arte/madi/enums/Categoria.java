package com.arte.madi.enums;

/**
 * Enumera los roles existentes en el sistema.
 *
 * @author Mauro Montenegro <maumontenegro.s at gmail.com>
 */
public enum Categoria {
    Cuadros("CUADROS"),
    Esculturas("ESCULTURA"),
    Arte_Reciclado("ARTE RECICLADO");
    
    private final String displayName;

    Categoria(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() { return displayName; }

    // Optionally and/or additionally, toString.
  //  @Override public String toString() { return displayName; }
}
