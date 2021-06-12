package org.sterl.dependency.graph.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Color {
    BLUE("#007bff"),
    GREEN("#28a745"),
    DARK("#343a40")
    ;
    private final String rgb;
    
    public String get() {
        return rgb;
    }
}
