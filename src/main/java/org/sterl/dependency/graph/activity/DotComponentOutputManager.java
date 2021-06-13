package org.sterl.dependency.graph.activity;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.sterl.dependency.component.model.Component;
import org.sterl.dependency.graph.model.Color;
import org.sterl.dependency.graph.model.DotConstants;

@ApplicationScoped
public class DotComponentOutputManager implements DotConstants {

    public String printDependency(Collection<Component> components) {
        StringBuilder result = new StringBuilder();
        result.append("digraph {").append(NEW_LINE);
        
        components.stream().forEach(c -> {
            final String name = escapeName(c.getQualifiedName());//.replaceAll("\\.", "_");
            result.append(TAB)
                  .append(name)
                      .append(" [label=<\n ").append("<table><tr><td><b>" + c.getName() + "</b></td></tr><tr><td>classes " + c.getContains().size()  + "</td></tr></table>")
                      .append(" \n> \n")
                  .append("shape=component fontcolor=white fillcolor=\"" + Color.DARK.get() + "\" style=filled")
                  .append("]").append(NEW_LINE);
            if (!c.getDependsOn().isEmpty()) {
                c.getDependsOn().values().stream().forEach(used -> {
                    result.append(TAB).append(name).append(USES).append(escapeName(used.getComponent().getQualifiedName()))
                          .append(" [label=\"" + used.getCount() + "\"]")
                          .append(NEW_LINE);
                });
                result.append(NEW_LINE);
            }
        });
        
        result.append("}").append(NEW_LINE);
        return result.toString();
    }
}
