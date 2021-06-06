package org.sterl.dependency.plugin.imports;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Currency;
import java.util.Optional;

import javax.inject.Inject;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.sterl.dependency.analyze.activity.AnalyseManager;
import org.sterl.dependency.analyze.model.JavaClass;
import org.sterl.dependency.graph.DotClassOutputManager;

@EnableWeld
public class ImportAnalyserTest {
    @WeldSetup
    public WeldInitiator weld = WeldInitiator.performDefaultDiscovery();

    @Inject AnalyseManager analyseManager;

    @Test
    public void testClassImport() throws Exception {
        analyseManager.addClasses("src/main/java/org/sterl");
        
        JavaClass javaClass = analyseManager.getJavaClass(DotClassOutputManager.class);
        Optional<JavaClass> findFirst = javaClass.getUses().stream().filter(p -> p.getName().equals(JavaClass.class.getName())).findFirst();
        assertTrue(findFirst.isPresent(), "JavaClass is used by DotOutputManager");
    }
    
    
    @Test
    public void testCurrency() {
    	System.out.println(Currency.getInstance("BRL"));
    }
}
