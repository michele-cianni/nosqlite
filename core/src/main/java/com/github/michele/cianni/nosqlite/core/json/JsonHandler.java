package com.github.michele.cianni.nosqlite.core.json;

import com.github.michele.cianni.nosqlite.core.models.Entry;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public interface JsonHandler {

    void serializeEntries(Collection<Entry> values, File file) throws IOException;

    Collection<Entry> deserializeEntries(File file) throws IOException;
}
