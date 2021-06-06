package org.sterl.dependency.analyze.model;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.jboss.weld.exceptions.IllegalArgumentException;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data @ToString(of = "name") @EqualsAndHashCode(of = "name")
public class JavaClass {

    final String name;
    final String simpleName;
    final String classPackage;

    final CompilationUnit cu; 
    /** Optional type if it is real type */
    final TypeDeclaration<?> type;
    
    private final Set<JavaClass> uses = new LinkedHashSet<>();
    
    public enum ClassType {
        CLASS,
        INTERFACE
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
        if (javaClass == this) {
            throw new IllegalArgumentException("Self reference makes no sense ...");
        }
        uses.add(javaClass);
    }
}