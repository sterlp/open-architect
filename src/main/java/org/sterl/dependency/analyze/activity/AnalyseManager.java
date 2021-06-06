package org.sterl.dependency.analyze.activity;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.sterl.dependency.analyze.model.JavaClass;
import org.sterl.dependency.analyze.repository.ClassRepository;

import com.github.javaparser.ast.Node;

@ApplicationScoped
public class AnalyseManager {

    @Inject ClassRepository classRepository;
    
    @Inject @Any
    private Instance<AnalyserPlugin> analyser;
    
    public int addClasses(String path) throws IOException {
        final int result = classRepository.addClasses(path);
        classRepository.getAllClasses().forEach(c -> analyse(c));
        return result;
    }
    
    private void analyse(JavaClass jc) {
        analyseChilds(jc, jc.getCu().getChildNodes());
    }
    
    private void analyseChilds(JavaClass jc, List<Node> childNodes) {
        if (childNodes != null && !childNodes.isEmpty()) {
            for (Node n : childNodes) {
                analyser.stream().filter(a -> a.accepts(n)).forEach(a -> a.analyze(n, jc));
                analyseChilds(jc, n.getChildNodes());
            }
        }
    }

    public JavaClass getJavaClass(String nameAsString) {
        return classRepository.getClassByName(nameAsString);
    }
    
    public JavaClass getJavaClass(Class<?> clazz) {
        return classRepository.getClassByName(clazz.getName());
    }
    
    public Collection<JavaClass> getAll() {
        return classRepository.getAllClasses();
    }
}