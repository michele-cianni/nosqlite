package com.github.michele.cianni.nosqlite.core.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.michele.cianni.nosqlite.core.models.Entry;
import com.github.michele.cianni.nosqlite.core.storage.DataStorage;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class NoSQLiteQueryProcessor extends AbstractQueryProcessor {

    public NoSQLiteQueryProcessor(DataStorage dataStorage) {
        super(dataStorage);
    }

    @Override
    protected Comparator<Entry> createComparator(List<String> sortFields) {
        return (e1, e2) -> {
            for (String field : sortFields) {
                JsonNode val1 = e1.value().get(field);
                JsonNode val2 = e2.value().get(field);
                if (val1 == null || val2 == null) continue;

                int cmp = compare(val1, val2);

                if (cmp != 0) return cmp;
            }
            return 0;
        };
    }

    private int compare(JsonNode val1, JsonNode val2) {
        int cmp;

        if (val1.isNumber() && val2.isNumber()) {
            cmp = Double.compare(val1.asDouble(), val2.asDouble());
        } else if (val1.isBoolean() && val2.isBoolean()) {
            cmp = Boolean.compare(val1.asBoolean(), val2.asBoolean());
        } else {
            cmp = val1.asText().compareTo(val2.asText());
        }
        return cmp;
    }
}
