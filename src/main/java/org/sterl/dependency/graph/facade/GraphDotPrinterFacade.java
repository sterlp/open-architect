package org.sterl.dependency.graph.facade;

import java.io.IOException;
import java.util.Arrays;
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
            final String graphViz = output.printDependency(componentClasses, c -> {
                Color color = Color.GREEN;
                if (!c.getClassPackage().contains(e.getValue().getQualifiedName())) color = Color.BLUE;
                return color;
            });

            obj.add(e.getKey(), graphViz);
        });
        
        return obj.build();
    }
}
