package org.sterl.dependency.graph.plugin;

import org.sterl.dependency.graph.model.Color;

@FunctionalInterface
public interface ColorPicker<T> {
    Color pick(T value);
}
