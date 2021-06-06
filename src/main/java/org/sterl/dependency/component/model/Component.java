package org.sterl.dependency.component.model;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.sterl.dependency.analyze.model.JavaClass;

import lombok.Data;

@Data
public class Component {
    private String name;
    private String qualifiedName;

    private Set<JavaClass> contains = new LinkedHashSet<>();
    private Map<String, DependsOnCounter> dependsOn = new LinkedHashMap<>();

    @Override
    public String toString() {
        return "Component [name=" + name + ", qualifiedName=" + qualifiedName + ", contains=" + contains.size() + ", dependsOn="
                + dependsOn.size() + "]";
    }

    /**
     * Counter and addeder method if we depend here on a given component
     * @param dependantComponent the {@link Component} used by this {@link Component}
     */
    public Component dependsOn(final Component dependantComponent) {
        DependsOnCounter dependsOnCounter = dependsOn.computeIfAbsent(dependantComponent.getQualifiedName(), k -> new DependsOnCounter(dependantComponent));
        dependsOnCounter.addOne();
        return this;
    }
}
