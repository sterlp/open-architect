package org.sterl.dependency.graph.model;

public class GraphBuilder extends AbstractGraph<GraphBuilder> {

    public GraphBuilder() {
        super("", new StringBuilder());
        
        result.append("digraph ");
        result.append(" {");
        result.append(NEW_LINE);
    }
    
    public SubgraphBuilder<GraphBuilder> cluster(String name) {
        return new SubgraphBuilder<>(this, name, tab + TAB, result);
    }
    
    public StringBuilder build() {
        return close();
    }

}
