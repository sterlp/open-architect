package org.sterl.dependency.plugin.imports;

public class Foo {
    /*
    if (n instanceof VariableDeclarator) {
        System.out.println("VariableDeclarator --> " + n + " <--");
    } else if (n instanceof FieldDeclaration) {
        System.out.println("FieldDeclaration -->" + n + " <--");
        System.out.println("** " + ((FieldDeclaration)n).getElementType().getMetaModel().getQualifiedClassName());
        
    } else if (n instanceof ClassOrInterfaceDeclaration) {
        for (AnnotationExpr ann: ((ClassOrInterfaceDeclaration)n).getAnnotations()) {
            System.out.println("ClassOrInterfaceDeclaration: " + ann.toString());
        }
        System.out.println(((ClassOrInterfaceDeclaration)n).getImplementedTypes());
    } else if (n instanceof MethodCallExpr) {
        MethodCallExpr mc = (MethodCallExpr)n;
        System.out.println("MethodCallExpr " + mc + " --> " + mc.resolve().getClassName()); // ClassRepository
        
    } else if (n instanceof ImportDeclaration) {
        System.out.println("Import: " + ((ImportDeclaration)n).getNameAsString());
    } else {
        //System.out.println("Nope: " + n.getClass());
    }
    */
}
