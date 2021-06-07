package org.sterl.dependency.component.activity;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.sterl.dependency.analyze.activity.AnalyseManager;
import org.sterl.dependency.analyze.model.JavaClass;
import org.sterl.dependency.component.model.Component;

import lombok.Getter;

@ApplicationScoped
public class ComponentManager {

    @Inject AnalyseManager analyseManager;
    
    private final Set<String> componentBases = new LinkedHashSet<>();
    private final Map<String, String> classToComponent = new LinkedHashMap<>();

    @Getter
    private final Map<String, Component> components = new LinkedHashMap<>();
    
    public void reBuildComponent(Collection<String> componentBases) {
        this.componentBases.clear();
        this.componentBases.addAll(componentBases);
        this.components.clear();
        this.classToComponent.clear();
        buildComponent();
    }

    public void buildComponent() {
        analyseManager.getAll().stream().forEach(this::asignToComponents);
        components.values().forEach(this::callculateDependsOn);
    }
    
    private void callculateDependsOn(Component component) {
        final Set<String> analyzed = new HashSet<>();
        
        // for all classes in the component itself
        component.getContains().forEach(jc -> {
            // we check the used classes
            jc.getUses().forEach(usedClass -> {
                // if we look at this class for the first time
                if (analyzed.add(usedClass.getName())) {
                    String dependantComponentName = classToComponent.get(usedClass.getName());
                    if (dependantComponentName != null && !dependantComponentName.equals(component.getQualifiedName())) {
                        Component dependantComponent = components.get(dependantComponentName);
                        if (dependantComponent != null) component.dependsOn(dependantComponent);
                    }
                }
            });
        });
    }
    
    private void asignToComponents(JavaClass javaClass) {
        final String classPackage = javaClass.getClassPackage();
        List<String> bases = componentBases.stream().filter(cb -> classPackage.contains(cb)).collect(Collectors.toList());
        
        String componentBase = ComponentManagerUtil.determineBestQualified(bases);
        // if we don't find a base we have an "orphan" class which we ignore here ...
        if (componentBase != null) {
            String componentName = ComponentManagerUtil.getComponentName(componentBase, classPackage);
            
            String qualifiedName;
            if (componentBase.equals(classPackage)) {
                qualifiedName = componentBase;
            } else {
                qualifiedName = componentBase + "." + componentName;
            }
            
            final Component component = components.computeIfAbsent(qualifiedName, key -> {
                Component r = new Component(componentName, qualifiedName);
                return r;
            });
            classToComponent.put(javaClass.getName(), qualifiedName);
            component.getContains().add(javaClass);
        }
    }

}
