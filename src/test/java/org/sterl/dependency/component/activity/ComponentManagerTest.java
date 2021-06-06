package org.sterl.dependency.component.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.sterl.dependency.analyze.activity.AnalyseManager;
import org.sterl.dependency.component.activity.ComponentManager;
import org.sterl.dependency.component.model.Component;

@EnableWeld
public class ComponentManagerTest {
    @WeldSetup
    public WeldInitiator weld = WeldInitiator.performDefaultDiscovery();
    
    @Inject ComponentManager compManager;
    @Inject AnalyseManager analyseManager;
    
    @Test
    public void testComponentDetection() throws Exception {
        
        analyseManager.addClasses("./src/test/java/org/sterl");
        compManager.reBuildComponent(Arrays.asList("org.sterl.testproject"));
        
        for (Entry<String, Component> e : compManager.getComponents().entrySet()) {
            System.out.println("key= " + e.getKey() + " value: " + e.getValue());
        }
        ArrayList<Component> comps = new ArrayList<>(compManager.getComponents().values());
        assertEquals(3, comps.size());
        assertEquals("adresses", comps.get(0).getName());
        assertEquals("persons", comps.get(1).getName());
        assertEquals("simple", comps.get(2).getName());
    }
    
    @Test
    public void testComponentDependsOns() throws Exception {
        
        analyseManager.addClasses("./src/test/java/org/sterl");
        compManager.reBuildComponent(Arrays.asList("org.sterl.testproject"));
        
        Component adresses = compManager.getComponents().get("org.sterl.testproject.adresses");
        assertEquals(0, adresses.getDependsOn().size());
        
        Component persons = compManager.getComponents().get("org.sterl.testproject.persons");
        assertEquals(1, persons.getDependsOn().size());
        assertEquals("org.sterl.testproject.adresses", persons.getDependsOn().values().iterator().next().getComponent().getQualifiedName());
    }
}
