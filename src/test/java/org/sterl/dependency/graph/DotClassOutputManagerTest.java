package org.sterl.dependency.graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.inject.Inject;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.sterl.dependency.analyze.activity.AnalyseManager;
import org.sterl.dependency.analyze.model.JavaClass;
import org.sterl.dependency.component.activity.ComponentManager;
import org.sterl.dependency.graph.DotClassOutputManager;
import org.sterl.dependency.graph.DotComponentOutputManager;

@EnableWeld
public class DotClassOutputManagerTest {
    @WeldSetup
    public WeldInitiator weld = WeldInitiator.performDefaultDiscovery();

    
    @Inject DotClassOutputManager output;
    @Inject DotComponentOutputManager outputComp;
    
    @Inject ComponentManager compManager;
    @Inject AnalyseManager analyseManager;
    
    @Test
    public void testOutput() throws Exception {
        analyseManager.addClasses("src/main/java/org/sterl");
        String printDependency = output.printDependency(analyseManager.getAll());
        System.out.println(printDependency);
        
        toDotSvg(printDependency, "out");
    }

    @Test
    public void testDotComponentOutputManager() throws Exception {
        
        analyseManager.addClasses("./src/test/java/org/sterl");
        compManager.reBuildComponent(Arrays.asList("org.sterl.dependecy", "org.sterl.testproject"));
        
        String result = outputComp.printDependency(compManager.getComponents().values());
        System.out.println(result);
        
        toDotSvg(result, "test.project");
        
        compManager.getComponents().entrySet().forEach(e -> {
            final Set<JavaClass> componentClasses = e.getValue().getContains();
            String graphViz = output.printDependency(componentClasses, c -> {
                Color color = Color.GREEN;
                if (!c.getClassPackage().contains(e.getValue().getQualifiedName())) color = Color.BLUE;
                return color;
            });
            System.out.println(graphViz);
            try {
                toDotSvg(graphViz, e.getKey());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    
    private void toDotSvg(String printDependency, String fileName) throws IOException, InterruptedException {
        Path out = Paths.get("./source.dot");
        Files.write(out, printDependency.getBytes());
        
        File f = new File("./out.svg");
        if (f.exists()) f.delete();
        
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("/usr/local/bin/dot -Tsvg source.dot -o " + fileName + ".svg");
        System.out.println( pr.waitFor() );
        out.toFile().delete();
    }
}