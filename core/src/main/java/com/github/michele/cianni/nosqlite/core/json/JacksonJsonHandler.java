package com.github.michele.cianni.nosqlite.core.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.michele.cianni.nosqlite.core.models.Entry;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class JacksonJsonHandler implements JsonHandler {

    private final ObjectMapper mapper;

    public JacksonJsonHandler() {
        this(new ObjectMapper());
    }

    protected JacksonJsonHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void serializeEntries(Collection<Entry> values, File file) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, values);
    }

    @Override
    public Collection<Entry> deserializeEntries(File file) throws IOException {
        if (!file.exists()) {
            return Collections.emptyList();
        }
        return mapper.readValue(file, getCollectionType());
    }

    private CollectionType getCollectionType() {
        return mapper.getTypeFactory().constructCollectionType(List.class, Entry.class);
    }
}
