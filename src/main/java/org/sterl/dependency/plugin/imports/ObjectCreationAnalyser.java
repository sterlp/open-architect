package org.sterl.dependency.plugin.imports;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.sterl.dependency.analyze.activity.AnalyseManager;
import org.sterl.dependency.analyze.activity.AnalyserPlugin;
import org.sterl.dependency.analyze.model.JavaClass;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.ObjectCreationExpr;

@ApplicationScoped
public class ObjectCreationAnalyser implements AnalyserPlugin {
    @Inject AnalyseManager am;

    public void analyze(Node node, JavaClass javaClass) {
        final ObjectCreationExpr d = (ObjectCreationExpr)node;

        JavaClass objCreated = am.getJavaClass(d.getTypeAsString());
        if (objCreated == null) {
            objCreated = am.getJavaClass(javaClass.resolveType(d.getTypeAsString()));
        }
        javaClass.uses(objCreated);
    }

    public boolean accepts(Node node) {
        return node instanceof ObjectCreationExpr;
    }
}