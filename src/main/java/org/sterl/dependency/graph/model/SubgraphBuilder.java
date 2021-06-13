package org.sterl.dependency.graph.model;

import lombok.Getter;

public class SubgraphBuilder<BuildType> extends AbstractGraph<SubgraphBuilder<BuildType>> {
    @Getter
    private final String name;
    private final BuildType parent;

    SubgraphBuilder(
            BuildType parent,
            String name, String tab, StringBuilder result) {
        super(tab, result);
        
        this.name = name;
        this.parent = parent;

        result.append(tab)
              .append("subgraph \"cluster_")
              .append(name)
              .append("\"")
              .append(" {");
              
        tab().append("label=").append(escapeName(name)).append(END_NEW_LINE);
    }
    
    public SubgraphBuilder<SubgraphBuilder<BuildType>> cluster(String name) {
        return new SubgraphBuilder<>(this, name, tab + TAB, result);
    }
    
    public BuildType build() {
        close();
        return parent;
    }

}
