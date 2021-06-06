package org.sterl.dependency.plugin.imports;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.sterl.dependency.analyze.activity.AnalyseManager;
import org.sterl.dependency.analyze.activity.AnalyserPlugin;
import org.sterl.dependency.analyze.model.JavaClass;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;

@ApplicationScoped
public class ImportAnalyser implements AnalyserPlugin {
    @Inject AnalyseManager am;

    public void analyze(Node node, JavaClass javaClass) {
        ImportDeclaration id = (ImportDeclaration)node;
        
        if (!id.getNameAsString().equals(javaClass.getName())) {
            JavaClass c = am.getJavaClass(id.getNameAsString());
            if (c != null) {
                javaClass.uses(c);
            }
        }
    }
    
    public boolean accepts(Node node) {
        return node instanceof ImportDeclaration;
    }
}
