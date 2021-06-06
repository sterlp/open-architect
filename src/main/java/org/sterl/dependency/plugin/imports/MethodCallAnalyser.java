package org.sterl.dependency.plugin.imports;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.sterl.dependency.analyze.activity.AnalyseManager;
import org.sterl.dependency.analyze.activity.AnalyserPlugin;
import org.sterl.dependency.analyze.model.JavaClass;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;

@ApplicationScoped
public class MethodCallAnalyser implements AnalyserPlugin {
    @Inject AnalyseManager am;

    public void analyze(Node node, JavaClass javaClass) {
        MethodCallExpr d = (MethodCallExpr)node;
        try {
            final ResolvedMethodDeclaration resolve = d.resolve();
            final String name = resolve.getPackageName() + "." + resolve.getClassName();
            javaClass.uses(am.getJavaClass(name));
        } catch(Exception e) {
            System.err.println("WARN: " + e.getMessage());
            // TODO
            // throw new RuntimeException(e);
        } catch (NoClassDefFoundError e) {
            System.out.println("WARN: " + e.getMessage() + " - " + node);
        }
    }
    
    public boolean accepts(Node node) {
        return node instanceof MethodCallExpr;
    }
}