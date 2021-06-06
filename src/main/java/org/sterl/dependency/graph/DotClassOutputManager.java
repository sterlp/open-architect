package org.sterl.dependency.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import org.sterl.dependency.analyze.model.JavaClass;

@ApplicationScoped
public class DotClassOutputManager implements CommonStrings {
    public String printDependency(Collection<JavaClass> classes, ColorPicker<JavaClass> colorPicker) {
        StringBuilder result = new StringBuilder();
        result.append("digraph {").append(NEW_LINE);
        
        Set<String> printed = new HashSet<>();
        Set<JavaClass> usedClassesInGraph = new HashSet<>();

        classes.stream().forEach(c -> {
            final String name = escapeName(c.getName());//.replaceAll("\\.", "_");
            printed.add(name);

            result.append(TAB)
                  .append(name).append(" [label=\"").append(c.getSimpleName()).append("\" ")
                  .append("shape=box fontcolor=white fillcolor=\"" + colorPicker.pick(c).rgb + "\" style=filled")
                  .append("]").append(NEW_LINE);
            if (!c.getUses().isEmpty()) {
                c.getUses().stream().forEach(used -> {
                    usedClassesInGraph.add(used);
                    result.append(TAB).append(name).append(USES).append(escapeName(used.getName())).append(NEW_LINE);
                });
                result.append(NEW_LINE);
            }
        });
        usedClassesInGraph.forEach(u -> {
            if (!printed.contains(u.getName())) {
                result.append(TAB)
                    .append(escapeName(u.getName())).append(" [label=\"").append(u.getSimpleName()).append("\" ")
                    .append("shape=box fontcolor=white fillcolor=\"" + colorPicker.pick(u).rgb + "\" style=filled")
                    .append("]").append(NEW_LINE);
            }
        });
        
        result.append("}").append(NEW_LINE);
        return result.toString();
    }
    
    public String printDependency(Collection<JavaClass> classes) {
        return printDependency(classes, c -> Color.GREEN);
    }
    
}
