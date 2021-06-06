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
            if (node.toString().equals("dependsOnCounter.addOne()")) {
                d.resolve();
            }
            ResolvedMethodDeclaration resolve = d.resolve();
            String name = resolve.getPackageName() + "." + resolve.getClassName();
            JavaClass calls = am.getJavaClass(name);
            // exclude calls to ourself ...
            if (calls != null && calls != javaClass) {
                javaClass.uses(calls);
            }
        } catch(Exception e) {
            // TODO logging?
        } catch (NoClassDefFoundError e) {
            System.out.println(node);
        }
    }
    
    public boolean accepts(Node node) {
        return node instanceof MethodCallExpr;
    }
}