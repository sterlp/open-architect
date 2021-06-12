package org.sterl.dependency.graph.activity;

public interface DotOutputGenerator {
    static final char END = ';';
    static final char NEW_LINE = '\n';
    static final String END_NEW_LINE = ";\n";
    static final String USES = " -> ";
    static final String TAB = "    ";
    static final String TABS_2 = TAB + TAB;
    
    default String escapeName(String value) {
        return '\"' + value + '\"';
    }
}
