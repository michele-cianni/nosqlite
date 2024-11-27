package com.github.michele.cianni.nosqlite.core.query;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.michele.cianni.nosqlite.core.models.Entry;
import com.github.michele.cianni.nosqlite.core.storage.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class NoSQLiteQueryProcessorTest {

    private DataStorage dataStorage;

    private NoSQLiteQueryProcessor underTest;

    @BeforeEach
    void setup() {
        dataStorage = Mockito.mock(DataStorage.class);
        underTest = new NoSQLiteQueryProcessor(dataStorage);
    }

    @Test
    void executeQuery_shouldReturnFilteredAndSortedResults() {
        ObjectNode value1 = JsonNodeFactory.instance.objectNode();
        value1.put("field1", "value1");
        value1.put("field2", 10);

        ObjectNode value2 = JsonNodeFactory.instance.objectNode();
        value2.put("field1", "value2");
        value2.put("field2", 5);

        ObjectNode value3 = JsonNodeFactory.instance.objectNode();
        value3.put("field1", "value3");
        value3.put("field2", 15);

        List<Entry> entries = List.of(
                new Entry("key1", value1),
                new Entry("key2", value2),
                new Entry("key3", value3)
        );

        when(dataStorage.loadAll()).thenReturn(entries);

        Predicate<Entry> filter = entry -> entry.value().get("field2").asInt() > 5;
        List<String> sortFields = List.of("field2");

        List<Entry> result = underTest.executeQuery(filter, sortFields);

        assertThat(result)
                .extracting(e -> e.value().get("field2").asInt())
                .containsExactly(10, 15);
    }

    @Test
    void executeQuery_shouldHandleEmptySortFields() {
        ObjectNode value1 = JsonNodeFactory.instance.objectNode();
        value1.put("field", "value1");

        List<Entry> entries = List.of(new Entry("key1", value1));

        when(dataStorage.loadAll()).thenReturn(entries);

        Predicate<Entry> filter = entry -> true;
        List<String> sortFields = List.of();

        List<Entry> result = underTest.executeQuery(filter, sortFields);

        assertThat(result).containsExactlyElementsOf(entries);
    }

    @Test
    void executeQuery_shouldHandleAcceptAllFilter() {
        ObjectNode value1 = JsonNodeFactory.instance.objectNode();
        value1.put("field", "value1");

        ObjectNode value2 = JsonNodeFactory.instance.objectNode();
        value2.put("field", "value2");

        List<Entry> entries = List.of(
                new Entry("key1", value1),
                new Entry("key2", value2)
        );

        when(dataStorage.loadAll()).thenReturn(entries);

        Predicate<Entry> filter = entry -> true;
        List<String> sortFields = List.of("field");

        List<Entry> result = underTest.executeQuery(filter, sortFields);

        assertThat(result).hasSize(2);
    }

    @Test
    void executeQuery_shouldHandleEmptyDataList() {
        when(dataStorage.loadAll()).thenReturn(List.of());

        Predicate<Entry> filter = entry -> true;
        List<String> sortFields = List.of();

        List<Entry> result = underTest.executeQuery(filter, sortFields);

        assertThat(result).isEmpty();
    }

    @Test
    void executeQuery_shouldHandleFilterRejectAll() {
        ObjectNode value1 = JsonNodeFactory.instance.objectNode();
        value1.put("field", "value1");

        List<Entry> entries = List.of(new Entry("key1", value1));

        when(dataStorage.loadAll()).thenReturn(entries);

        Predicate<Entry> filter = entry -> false;
        List<String> sortFields = List.of("field");

        List<Entry> result = underTest.executeQuery(filter, sortFields);

        assertThat(result).isEmpty();
    }

    @Test
    void executeQuery_shouldHandleMissingFieldsInJsonNode() {
        ObjectNode value1 = JsonNodeFactory.instance.objectNode();
        value1.put("field1", "value1");

        ObjectNode value2 = JsonNodeFactory.instance.objectNode();
        value2.put("field2", 5);

        ObjectNode value3 = JsonNodeFactory.instance.objectNode();

        List<Entry> entries = List.of(
                new Entry("key1", value1),
                new Entry("key2", value2),
                new Entry("key3", value3)
        );

        when(dataStorage.loadAll()).thenReturn(entries);

        Predicate<Entry> filter = entry -> true;
        List<String> sortFields = List.of("field1", "field2");

        List<Entry> result = underTest.executeQuery(filter, sortFields);

        assertThat(result)
                .extracting(e -> {
                    JsonNode value = e.value();
                    return value.has("field1") ? value.get("field1").asText() : null;
                })
                .containsExactly("value1", null, null);
    }

}
