package org.sterl.dependency.graph.facade;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.sterl.dependency.analyze.activity.AnalyseManager;
import org.sterl.dependency.analyze.model.JavaClass;
import org.sterl.dependency.component.activity.ComponentManager;
import org.sterl.dependency.graph.activity.DotClassOutputManager;
import org.sterl.dependency.graph.activity.DotComponentOutputManager;
import org.sterl.dependency.graph.model.Color;

@ApplicationScoped
public class GraphDotPrinterFacade {
    
    @Inject DotClassOutputManager output;
    @Inject DotComponentOutputManager outputComp;
    
    @Inject ComponentManager compManager;
    @Inject AnalyseManager analyseManager;

    public JsonObject print(String path, String... packages) throws IOException {
        final JsonObjectBuilder obj = Json.createObjectBuilder();

        analyseManager.addClasses(path);
        compManager.reBuildComponent(Arrays.asList(packages));
        
        final String result = outputComp.printDependency(compManager.getComponents().values());
        
        obj.add("root", result);
        
        compManager.getComponents().entrySet().forEach(e -> {
            final Set<JavaClass> componentClasses = e.getValue().getContains();
            final Set<JavaClass> toPrint = new HashSet<>();

            componentClasses.forEach(jc -> {
                toPrint.add(jc);
                toPrint.addAll(jc.getUses());
            });

            
            final String graphViz = output.printDependency(toPrint, c -> {
                Color color = Color.GREEN;
                if (!c.getClassPackage().contains(e.getValue().getQualifiedName())) color = Color.BLUE;
                return color;
            });

            obj.add(e.getKey(), graphViz);
        });
        
        for(final JavaClass jc : analyseManager.getAll()) {
            final Set<JavaClass> toPrint = new HashSet<>();
            toPrint.add(jc);
            toPrint.addAll(jc.getUses());
            toPrint.addAll(jc.getUsedBy());
            
            final String graphViz = output.printDependency(toPrint, c -> {
                Color color = Color.BLUE;
                if (c.getAssignedTo() == jc.getAssignedTo()) color = Color.GREEN;
                return color;
            });

            obj.add(jc.getName(), graphViz);
        }
        
        return obj.build();
    }
}
