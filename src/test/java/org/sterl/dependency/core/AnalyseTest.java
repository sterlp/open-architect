package org.sterl.dependency.core;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.sterl.dependency.analyze.activity.AnalyseManager;
import org.sterl.dependency.analyze.model.JavaClass;
import org.sterl.dependency.analyze.repository.ClassRepository;
import org.sterl.testproject.simple.FooManager;
import org.sterl.testproject.simple.SimpleEntityBE;

@EnableWeld
public class AnalyseTest {
    
    @WeldSetup
    public WeldInitiator weld = WeldInitiator.performDefaultDiscovery();

    @Inject AnalyseManager analyseManager;
    @Inject ClassRepository repository;
    
    @Test
    public void testSimpleParse() throws Exception {
        assertEquals(3, analyseManager.addClasses("src/test/java/org/sterl/testproject/simple"));
        
        assertEquals(3, repository.getAllClasses().size());
        assertNotNull(repository.getClassByName(FooManager.class.getName()));
        assertNotNull(repository.getClassByName(SimpleEntityBE.class.getName()));
    }
    
    @Test
    public void testMethodCallAnalyser() throws Exception {
        assertEquals(3, analyseManager.addClasses("src/test/java/org/sterl/testproject/simple"));
        
        JavaClass classByName = repository.getClassByName(FooManager.class.getName());
        assertEquals("BarActivity", classByName.getUses().iterator().next().getSimpleName());
    }
}