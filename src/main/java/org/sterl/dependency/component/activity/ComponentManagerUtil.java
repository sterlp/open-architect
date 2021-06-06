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
        String base = null;
        if (!bases.isEmpty()) {
            base = bases.get(0);
            if (bases.size() > 1) {
                int dotCount = StringUtils.countMatches(base, ".");
                for (int i = 1; i < bases.size(); i++) {
                    int newCount = StringUtils.countMatches(bases.get(i), ".");
                    if (newCount > dotCount) {
                        dotCount = newCount;
                        base = bases.get(i);
                    }
                }
            }
        }
        return base;
    }
}
