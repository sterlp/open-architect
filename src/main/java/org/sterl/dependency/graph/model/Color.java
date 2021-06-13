package org.sterl.dependency.graph.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Color {
    BLUE("#0d6efd"),
    GREEN("#198754"),
    DARK("#6c757d")
    ;
    private final String rgb;
    
    public String get() {
        return rgb;
    }
}
