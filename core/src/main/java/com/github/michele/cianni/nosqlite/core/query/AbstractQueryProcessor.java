package com.github.michele.cianni.nosqlite.core.query;

import com.github.michele.cianni.nosqlite.core.models.Entry;
import com.github.michele.cianni.nosqlite.core.storage.DataStorage;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractQueryProcessor implements QueryProcessor {

    private final DataStorage dataStorage;

    protected AbstractQueryProcessor(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public List<Entry> executeQuery(Predicate<Entry> filter, List<String> sortFields) {
        Comparator<Entry> comparator = createComparator(sortFields);
        return dataStorage.loadAll().stream()
                .filter(filter)
                .sorted(comparator)
                .toList();
    }

    protected abstract Comparator<Entry> createComparator(List<String> sortFields);
}
