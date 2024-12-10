package com.github.michele.cianni.nosqlite.core.command;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.models.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GetAllCommandTest {

    private Database database;

    private GetAllCommand underTest;

    @BeforeEach
    void setUp() {
        database = mock(Database.class);
        underTest = new GetAllCommand(database);
    }

    @Test
    void shouldReturnSuccessWhenDatabaseHasEntries() {
        Entry entry = new Entry("key", JsonNodeFactory.instance.objectNode().put("key", "value"));

        when(database.getAll()).thenReturn(List.of(entry));

        assertThat(underTest.execute())
                .isNotNull()
                .satisfies(r -> assertThat(r.status()).isTrue())
                .satisfies(r -> assertThat(r.message()).isEqualTo("Entries found"))
                .satisfies(r -> assertThat(r.values()).contains(entry));

        verify(database, times(1)).getAll();
    }

    @Test
    void shouldReturnFailureWhenDatabaseHasNoEntries() {
        when(database.getAll()).thenReturn(List.of());

        assertThat(underTest.execute())
                .isNotNull()
                .satisfies(r -> assertThat(r.status()).isFalse())
                .satisfies(r -> assertThat(r.message()).isEqualTo("No entries found"))
                .satisfies(r -> assertThat(r.values()).isEmpty());

        verify(database, times(1)).getAll();
    }

}