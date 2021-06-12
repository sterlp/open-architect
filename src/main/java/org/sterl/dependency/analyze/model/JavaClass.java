package org.sterl.dependency.analyze.model;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.sterl.dependency.component.model.Component;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.TypeDeclaration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data @ToString(of = "name") @EqualsAndHashCode(of = "name")
public class JavaClass {

    final String name;
    final String simpleName;
    final String classPackage;
    Component assignedTo;

    final CompilationUnit cu; 
    /** Optional type if it is real type */
    final TypeDeclaration<?> type;
    
    private final Set<JavaClass> uses = new LinkedHashSet<>();
    private final Set<JavaClass> usedBy = new LinkedHashSet<>();
    
    public enum ClassType {
        CLASS,
        INTERFACE,
        ANNOTATION
    }
    
    public JavaClass (CompilationUnit compilationUnit) throws IOException {
        cu = compilationUnit;
        simpleName = cu.getPrimaryTypeName().get();
        classPackage = cu.getPackageDeclaration().get().getNameAsString();
        name = classPackage + "." + simpleName;
        Optional<TypeDeclaration<?>> primaryType = cu.getPrimaryType();
        if (primaryType.isPresent()) type = primaryType.get();
        else type = null;
    }
    
    public void uses(JavaClass javaClass) {
        if (javaClass == null || this.equals(javaClass) || uses.contains(javaClass)) {
            // ignored!
        } else {
            uses.add(javaClass);
            javaClass.usedBy(this);
        }
    }

    private void usedBy(JavaClass javaClass) {
        if (javaClass == null || this.equals(javaClass) || usedBy.contains(javaClass)) {
            // nope
        } else {
            this.usedBy.add(javaClass);
            javaClass.uses(this);
        }
    }

    public NodeList<ImportDeclaration> getImports() {
        return cu.getImports();
    }

    /**
     * Resolves the given Class in scope of this class.
     * @return the full name
     */
    public String resolveType(String clazz) {
        final Optional<ImportDeclaration> f = cu.getImports().stream().filter(i -> i.getNameAsString().endsWith(clazz))
            .findFirst();
        
        if (f.isPresent()) {
            return f.get().getNameAsString();
        } else {
            return this.classPackage + "." + clazz;
        }
    }
}