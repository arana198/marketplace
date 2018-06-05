package com.marketplace.utils;

import java.util.HashMap;
import java.util.Map;

public class RolesExtractorUtils {
  public static String extract(final Map<String, String> roles) {
    final StringBuilder roleStr = new StringBuilder();

    roles.forEach((key, value) -> {
      roleStr.append(key);
      roleStr.append(":");
      roleStr.append(value);
      roleStr.append(",");
    });

    roleStr.setLength(roleStr.length() - 1);
    return roleStr.toString();
  }

  public static Map<String, String> extract(final String roles) {
    String[] keyValues = roles.split(",");
    Map<String, String> rolesMap = new HashMap<String, String>();
    for (int i = 0; i < keyValues.length; i++) {
      String[] keyValue = keyValues[i].split(":");
      rolesMap.put(keyValue[0], keyValue[1]);
    }

    return rolesMap;
  }
}
