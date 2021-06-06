package org.sterl.dependency.graph;

public interface CommonStrings {
    static final char NEW_LINE = '\n';
    static final String USES = " -> ";
    static final String TAB = "    ";
    
    default String escapeName(String value) {
        return '\"' + value + '\"';
    }
}
