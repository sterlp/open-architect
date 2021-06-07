package org.sterl.dependency.component.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ComponentTest {

    @Test
    void testSumTotalDependsOn() {
        Component a = new Component("a", "a");
        Component b = new Component("b", "b");
        Component c = new Component("c", "c");
        
        assertEquals(0, a.sumTotalDependsOn());

        a.dependsOn(b)
         .dependsOn(b)
         .dependsOn(c);
        
        assertEquals(3, a.sumTotalDependsOn());
        
        b.dependsOn(c);
        assertEquals(1, b.sumTotalDependsOn());
        
        assertEquals(0, c.sumTotalDependsOn());
    }

}
