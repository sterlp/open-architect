package org.sterl.dependency.analyze.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(of = "uses")
public class Usage {

    private final JavaClass uses;
}
