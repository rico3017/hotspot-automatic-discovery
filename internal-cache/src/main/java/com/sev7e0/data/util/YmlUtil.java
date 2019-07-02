package com.sev7e0.data.util;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class YmlUtil {

    /**
     * @param path
     * @return
     */
    public static Map<String, Object> readYml(String path) {
        HashMap hashMap = new HashMap();
        try {
            File file = new File(path);
            Yaml yaml = new Yaml();
            hashMap = yaml.loadAs(new FileInputStream(file), HashMap.class);
        } catch (Exception e) {
            log.error("read file error: {}", e.getMessage());
        }
        return hashMap;
    }
}
