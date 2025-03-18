package com.TheOffice.theOffice.dataLoader;

import com.TheOffice.theOffice.staticModels.Local;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class LocalDataLoader {
    public static List<Local> localList;
    private static final LocalDataLoader INSTANCE = new LocalDataLoader();

    private LocalDataLoader() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File("src/main/java/com/TheOffice/theOffice/json/local.json");
            List<Local> localWrapper = objectMapper.readValue(jsonFile, List.class);
            localList = localWrapper;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LocalDataLoader getInstance() {
        return INSTANCE;
    }

    public Optional<Local> getLocalById(Long id) {
        return localList.stream()
                .filter(local -> local.getId().equals(id))
                .findFirst();
    }

}
