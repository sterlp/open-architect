package org.sterl.dependency.graph.activity;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import org.sterl.dependency.analyze.model.JavaClass;
import org.sterl.dependency.component.model.Component;
import org.sterl.dependency.graph.model.Color;
import org.sterl.dependency.graph.model.DotConstants;
import org.sterl.dependency.graph.model.GraphBuilder;
import org.sterl.dependency.graph.model.SubgraphBuilder;
import org.sterl.dependency.graph.plugin.ColorPicker;

@ApplicationScoped
public class DotClassOutputManager implements DotConstants {

    public String printDependency(
            Collection<JavaClass> toPrint, 
            ColorPicker<JavaClass> colorPicker) {
        
        final GraphBuilder builder = new GraphBuilder();
        final Map<Component, Set<JavaClass>> classes = new LinkedHashMap<>();
        
        toPrint.forEach(jc -> 
            classes.computeIfAbsent(jc.getAssignedTo(), c -> new LinkedHashSet<>())
                   .add(jc));
        
        // add the classes
        addSubgraphs(builder, classes, colorPicker);
        
        // add the dependencies
        for (JavaClass javaClass : toPrint) {
            for (JavaClass used : javaClass.getUses()) {
                if (toPrint.contains(used)) {
                    builder.use(javaClass, used);
                }
            }
            for (JavaClass usedBy : javaClass.getUsedBy()) {
                if (toPrint.contains(usedBy)) {
                    builder.use(usedBy, javaClass);
                }
            }
        }

        return builder.build().append(NEW_LINE).toString();
    }
    
    void addSubgraphs(GraphBuilder builder,
            final Map<Component, Set<JavaClass>> classes, ColorPicker<JavaClass> colorPicker) {

        SubgraphBuilder<GraphBuilder> current = null;
        for (Entry<Component, Set<JavaClass>> e : classes.entrySet()) {
            if (e.getKey() == null) {
                System.err.println("WARN: Classes without component -> " + e.getValue());
            } else {
                final String compName = e.getKey().getQualifiedName();
                if (current == null) {
                    current = builder.cluster(compName);
                } else if (!current.getName().equals(compName)) {
                    current = current.build().cluster(compName);
                }
                for (JavaClass jc : e.getValue()) {
                    current.box(jc, colorPicker);
                }
            }
        }
        // close last subgraph
        if (current != null) {
            current.build();
        }
    }
    
    
    public String printDependency(Collection<JavaClass> classes) {
        return printDependency(classes, c -> Color.GREEN);
    }
    
}
