/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.yml;

import java.util.HashMap;
import java.util.Map;

public class BBYmlParser {

    private final Map<String, Object> toBeFilled = new HashMap<>();

    public Map<String, Object> getParsed() {
        return toBeFilled;
    }

    public void parse(Map<?, ?> obj, String parentKey) {

        for (Map.Entry<?, ?> entry : obj.entrySet()) {

            if (entry.getValue() != null && entry.getValue() instanceof Map<?, ?> map)

                parse(map, (parentKey == null ? "" : parentKey +  ".") + entry.getKey());

            else

                toBeFilled.put((parentKey == null ? "" : parentKey + ".") + entry.getKey(), entry.getValue());

        }
    }
}
