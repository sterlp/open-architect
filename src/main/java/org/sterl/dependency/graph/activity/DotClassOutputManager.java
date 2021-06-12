package org.sterl.dependency.graph.activity;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import org.sterl.dependency.analyze.model.JavaClass;
import org.sterl.dependency.component.model.Component;
import org.sterl.dependency.graph.model.Color;
import org.sterl.dependency.graph.plugin.ColorPicker;

@ApplicationScoped
public class DotClassOutputManager implements DotOutputGenerator {

    public String printDependency(
            Collection<JavaClass> classes, 
            ColorPicker<JavaClass> colorPicker) {

        final StringBuilder result = new StringBuilder();
        result.append("digraph {").append(NEW_LINE);

        final Set<JavaClass> printed = new HashSet<>();
        
        final Set<JavaClass> usedClassesInGraph = new LinkedHashSet<>();

        classes.stream().forEach(c -> {
            final String name = escapeName(c.getName());//.replaceAll("\\.", "_");
            printed.add(c);

            result.append(TAB)
                  .append(name).append(" [label=\"").append(c.getSimpleName()).append("\" ")
                  .append("shape=box fontcolor=white fillcolor=\"" + colorPicker.pick(c).get() + "\" style=filled")
                  .append("]")
                  .append(END_NEW_LINE);
            
            // print all used arrows
            if (!c.getUses().isEmpty()) {
                c.getUses().stream().forEach(used -> {
                    usedClassesInGraph.add(used);
                    result.append(TAB).append(name).append(USES).append(escapeName(used.getName()))
                          .append(END_NEW_LINE);
                });
                result.append(NEW_LINE);
            }
        });

        Set<JavaClass> toPrint = usedClassesInGraph.stream().filter(u -> !printed.contains(u))
                .collect(Collectors.toSet());
        // ensure all used classes are added to the graph, maybe now in different packages
        final StringBuilder subs = appendWithSubgraph(toPrint, colorPicker);
        result.append(subs);

        result.append("}").append(NEW_LINE);
        return result.toString();
    }
    
    StringBuilder appendWithSubgraph(Collection<JavaClass> value, ColorPicker<JavaClass> colorPicker) {
        final StringBuilder result = new StringBuilder();
        final Map<Component, Set<JavaClass>> classes = new LinkedHashMap<>();
        
        value.forEach(jc -> 
            classes.computeIfAbsent(jc.getAssignedTo(), c -> new LinkedHashSet<>())
                   .add(jc));
        
        Component current = null;
        for (Entry<Component, Set<JavaClass>> e : classes.entrySet()) {
            if (current != e.getKey()) {
                // close subgraph
                if (current != null) {
                    result.append("}");
                }
                // and start a new one
                current = e.getKey();
                result.append(TAB)
                      .append("subgraph ")
                          .append(escapeName("cluster_" + current.getQualifiedName()))
                          .append(" {")
                          .append(NEW_LINE)
                      .append(TABS_2).append("label = ")
                      .append(escapeName(current.getQualifiedName()))
                      .append(END_NEW_LINE);
            }
            for (JavaClass u : value) {
                result.append(TABS_2)
                    .append(escapeName(u.getName())).append(" [label=\"").append(u.getSimpleName()).append("\" ")
                    .append("shape=box fontcolor=white fillcolor=\"" + colorPicker.pick(u).get() + "\" style=filled")
                    .append("]")
                    .append(END_NEW_LINE);
            }
        }
        // close last subgraph
        if (current != null) {
            result.append(TAB) .append("}").append(NEW_LINE);
        }
        return result;
    }
    
    public String printDependency(Collection<JavaClass> classes) {
        return printDependency(classes, c -> Color.GREEN);
    }
    
}
