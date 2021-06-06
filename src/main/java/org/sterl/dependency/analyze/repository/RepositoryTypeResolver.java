package org.sterl.dependency.analyze.repository;

import org.sterl.dependency.analyze.model.JavaClass;

import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionFactory;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

class RepositoryTypeResolver implements TypeSolver {
    private final ClassRepository repository;
    private TypeSolver parent;
    
    public RepositoryTypeResolver(ClassRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    public TypeSolver getParent() {
        return parent;
    }

    @Override
    public void setParent(TypeSolver parent) {
        this.parent = parent;
    }

    @Override
    public SymbolReference<ResolvedReferenceTypeDeclaration> tryToSolveType(String name) {
        SymbolReference<ResolvedReferenceTypeDeclaration> result;
        JavaClass javaClass = repository.getClassByName(name);

        if (javaClass != null && javaClass.getType() != null) {
            result = SymbolReference.solved(JavaParserFacade.get(this).getTypeDeclaration(javaClass.getType()));
        } else {
            if (name.equals("java.lang.Object")) {
                result = SymbolReference.solved(ReflectionFactory.typeDeclarationFor(Object.class, getRoot()));
                /*
                try {
                    ClassLoader classLoader = ReflectionTypeSolver.class.getClassLoader();
                    Class<?> clazz = classLoader.loadClass(name);
                    result = SymbolReference.solved(ReflectionFactory.typeDeclarationFor(clazz, getRoot()));
                } catch (ClassNotFoundException e) {
                    // TODO log
                    result = SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
                }
                */
            } else {
                result = SymbolReference.unsolved(ResolvedReferenceTypeDeclaration.class);
            }
        }
        return result;
    }
}