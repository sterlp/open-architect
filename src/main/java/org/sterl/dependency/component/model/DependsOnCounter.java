package org.sterl.dependency.component.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(of = "component")
public class DependsOnCounter {

    private final Component component;
    private int count = 0;
    
    public DependsOnCounter addOne() {
        ++count;
        return this;
    }
}