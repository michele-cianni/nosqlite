package com.github.michele.cianni.nosqlite.core.query;

import com.github.michele.cianni.nosqlite.core.models.Entry;

import java.util.List;
import java.util.function.Predicate;

public interface QueryProcessor {
    List<Entry> executeQuery(Predicate<Entry> filter, String sortField, String order);
}
