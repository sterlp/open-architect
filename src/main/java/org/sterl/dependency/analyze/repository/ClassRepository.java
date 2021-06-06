package org.sterl.dependency.analyze.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.sterl.dependency.analyze.model.JavaClass;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.Provider;
import com.github.javaparser.Providers;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;

@ApplicationScoped
public class ClassRepository {
    
    private final Map<String, JavaClass> classes = new LinkedHashMap<>();
    private final JavaParser javaParser;

    public ClassRepository() {
        final CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        RepositoryTypeResolver typeSolver = new RepositoryTypeResolver(this);
        combinedTypeSolver.add(typeSolver);
        // Configure JavaParser to use type resolution
        final JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        javaParser = new JavaParser();
        javaParser.getParserConfiguration().setSymbolResolver(symbolSolver);
    }
    
    /**
     * Adds all classes in the given repository to our class cache
     * @param directory
     * @return
     * @throws IOException
     */
    public int addClasses(String directory) throws IOException {
        final List<Path> files = Files.walk(Paths.get(directory))
                .filter((i) -> i.getFileName().toString().endsWith(".java"))
                .collect(Collectors.toList());
        
        for (Path path : files) {
            final JavaClass c = parseFile(javaParser, path);
            classes.put(c.getName(), c);
        }
        return files.size();
    }

    public JavaClass getClassByName(String name) {
        return classes.get(name);
    }
    
    public Collection<JavaClass> getAllClasses() {
        return classes.values();
    }
    
    public static JavaClass parseFile(JavaParser javaParser, Path path) throws IOException {
        try (Provider p = Providers.provider(path)) {
            final ParseResult<CompilationUnit> result = javaParser.parse(ParseStart.COMPILATION_UNIT, p);
            if (result.isSuccessful()) {
                final CompilationUnit cu = result.getResult().get().setStorage(path);
                return new JavaClass(cu);
            } else {
                // TODO maybe we should define a smarter way here, basically don't stop on the first error.
                throw new ParseProblemException(result.getProblems());
            }
        }
    }
}