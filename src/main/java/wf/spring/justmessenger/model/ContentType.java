package wf.spring.justmessenger.model;


import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public enum ContentType {

    APPLICATION("application"),
    AUDIO("audio"),
    IMAGE("image"),
    MULTIPART("multipart"),
    TEXT("text"),
    VIDEO("video"),
    OTHER("other");


    private static final HashMap<String, ContentType> valuesMap = new HashMap<>(8);
    static {
        for(ContentType contentType : values()) {
            if(contentType == null) continue;
            valuesMap.put(contentType.getName(), contentType);
        }
    }


    @Getter
    private final String name;

    ContentType(String name) {
        this.name = name;
    }



    public static ContentType getByName(@Nullable String name) {
        if(name == null) return OTHER;

        if(!name.contains("/"))
            return valuesMap.getOrDefault(name, OTHER);

        return getByName(name.split("/")[0]);
    }

}
