package com.github.michele.cianni.nosqlite.core.query;

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
    void shouldReturnEmptyListWhenNoData() {
        when(dataStorage.loadAll()).thenReturn(List.of());

        List<Entry> result = underTest.executeQuery(entry -> true, null, null);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFilterWhenAcceptAll() {
        ObjectNode value1 = JsonNodeFactory.instance.objectNode();
        value1.put("field", "value1");

        List<Entry> entries = List.of(new Entry("key1", value1));

        when(dataStorage.loadAll()).thenReturn(entries);

        Predicate<Entry> filter = entry -> true;

        assertThat(underTest.executeQuery(filter, null, null))
                .containsExactlyElementsOf(entries);
    }

    @Test
    void shouldFilterWhenRejectAll() {
        ObjectNode value1 = JsonNodeFactory.instance.objectNode();
        value1.put("field", "value1");

        List<Entry> entries = List.of(new Entry("key1", value1));

        when(dataStorage.loadAll()).thenReturn(entries);

        Predicate<Entry> filter = entry -> false;

        assertThat(underTest.executeQuery(filter, null, null))
                .isEmpty();
    }

    @Test
    void shouldFilterAscendingWhenOrderIsNull() {
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

        String sortField = "field";

        assertThat(underTest.executeQuery(filter, sortField, null))
                .containsExactlyElementsOf(entries);
    }

    @Test
    void shouldFilterAscendingWhenOrderIsAsc() {
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
        String sortField = "field";
        String order = "ASC";

        assertThat(underTest.executeQuery(filter, sortField, order))
                .containsExactlyElementsOf(entries);
    }

    @Test
    void shouldFilterDescendingWhenOrderIsDesc() {
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
        String sortField = "field";
        String order = "DESC";

        assertThat(underTest.executeQuery(filter, sortField, order))
                .containsExactlyElementsOf(entries.reversed());
    }

    @Test
    void shouldFilterWhenFieldIsPresentAndOrderIsAsc() {
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
        String sortField = "field2";
        String order = "ASC";

        List<Entry> result = underTest.executeQuery(filter, sortField, order);

        assertThat(result)
                .extracting(e -> e.value().get("field2").asInt())
                .containsExactly(10, 15);
    }

    @Test
    void shouldFilterWhenFieldIsPresentAndOrderIsDesc() {
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
        String sortField = "field2";
        String order = "DESC";

        List<Entry> result = underTest.executeQuery(filter, sortField, order);

        assertThat(result)
                .extracting(e -> e.value().get("field2").asInt())
                .containsExactly( 15, 10);
    }
}
