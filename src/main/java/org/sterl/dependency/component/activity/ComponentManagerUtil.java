package org.sterl.dependency.component.activity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

class ComponentManagerUtil {

    static String getComponentName(String base, String packageName) {
        String result;
        if (base.length() < packageName.length()) {
            result = packageName.substring(base.length() + 1, packageName.length());
            int nextDot = result.indexOf('.');
            if (nextDot > 1) {
                result = result.substring(0, nextDot);
            }
        } else {
            result = packageName.substring(packageName.lastIndexOf('.') + 1);
        }
        return result;
    }
    
    static String determineBestQualified(List<String> bases) {
        if (bases.isEmpty()) return null;
        else if (bases.size() == 1) return bases.get(0);
        else {
            // up sort, longest path first
            bases.sort((s1, s2) -> s2.length() - s1.length());
            return bases.get(0);
        }
    }
}
