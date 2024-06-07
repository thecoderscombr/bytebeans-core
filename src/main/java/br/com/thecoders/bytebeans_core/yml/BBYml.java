/*
 * Copyright (c) 2024. This project is fully authored by The_Coders™ (https://thecoders.com.br)
 * and is available under GNU AFFERO GENERAL PUBLIC LICENSE Version 3, 19 November 2007
 */

package br.com.thecoders.bytebeans_core.yml;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class BBYml {

    public Map<String, Object> read(File settings)  {

        Yaml yaml = new Yaml();

        try (InputStream inputStream = new FileInputStream(settings)) {

            Map<String, Object> obj = yaml.load(inputStream);

            BBYmlParser BBYmlParser = new BBYmlParser();
            BBYmlParser.parse(obj, null);

            return BBYmlParser.getParsed();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}