package org.sterl.dependency.graph.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.sterl.dependency.analyze.model.JavaClass;
import org.sterl.dependency.graph.plugin.ColorPicker;

@SuppressWarnings("unchecked")
public abstract class AbstractGraph<T> implements DotConstants {
    protected final String tab;
    protected final StringBuilder result;
    
    private final Set<String> addedUsed = new HashSet<>();
    
    protected AbstractGraph(String tab, StringBuilder result) {
        super();
        this.tab = tab;
        this.result = result;
    }
    
    public T box(JavaClass jc, ColorPicker<JavaClass> cp) {
        tab().append(escapeName(jc.getName()))
             .append(" [label=").append(escapeName(jc.getSimpleName()))
             .append(" shape=box fontcolor=white fillcolor=")
             .append(escapeName(cp.pick(jc).get()))
             .append(" style=filled")
             .append("]")
             .append(END_NEW_LINE);
        
        return (T)this;
    }
    
    public T use(JavaClass user, JavaClass used) {
        if (addedUsed.add(user.getName() + used.getName())) {
            tab().append(escapeName(user.getName()))
                .append(USES)
                .append(escapeName(used.getName()))
                .append(END_NEW_LINE);
        }

        return (T)this;
    }
    
    protected StringBuilder tab() {
        result.append(tab).append(TAB);
        return result;
    }

    protected StringBuilder close() {
        result.append(NEW_LINE).append(tab).append("}").append(NEW_LINE);
        return result;
    }
}
