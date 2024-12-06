package com.github.michele.cianni.nosqlite.core.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.models.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class InsertCommandTest {

    private Database database;
    private String key;
    private Entry entry;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        database = mock(Database.class);
        key = "testKey";
        entry = new Entry(key, JsonNodeFactory.instance.objectNode().put("key", "value"));
    }

    @Test
    void shouldReturnSuccessWhenEntryIsInserted() {
        InsertCommand underTest = new InsertCommand(database, key, entry);

        when(database.put(key, entry)).thenReturn(true);

        assertThat(underTest.execute())
                .isNotNull()
                .satisfies(r -> assertThat(r.status()).isTrue())
                .satisfies(r -> assertThat(r.message()).isEqualTo("Entry inserted successfully"));

        verify(database, times(1)).put(key, entry);
    }

    @Test
    void shouldReturnErrorWhenEntryIsNotInserted() {
        InsertCommand underTest = new InsertCommand(database, key, entry);

        when(database.put(key, entry)).thenReturn(false);

        assertThat(underTest.execute())
                .isNotNull()
                .satisfies(r -> assertThat(r.status()).isFalse())
                .satisfies(r -> assertThat(r.message()).isEqualTo("Error inserting entry"));

        verify(database, times(1)).put(key, entry);
    }
}
