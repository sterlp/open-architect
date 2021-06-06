package org.sterl.dependency.analyze.activity;

import org.sterl.dependency.analyze.model.JavaClass;

import com.github.javaparser.ast.Node;

public interface AnalyserPlugin {

    void analyze(Node node, JavaClass javaClass);
    public boolean accepts(Node node);
}
