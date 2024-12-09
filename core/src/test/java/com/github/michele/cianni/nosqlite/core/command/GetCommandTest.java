package com.github.michele.cianni.nosqlite.core.command;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.models.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GetCommandTest {

    private Database database;

    @BeforeEach
    void setUp() {
        database = Mockito.mock(Database.class);
    }

    @Test
    void shouldReturnSuccessWhenEntryIsFound() {
        String key = "validKey";
        Entry entry = new Entry(key, JsonNodeFactory.instance.objectNode().put("key", "value"));
        GetCommand underTest = new GetCommand(database, key);


        when(database.get(key)).thenReturn(Optional.of(entry));

        assertThat(underTest.execute())
                .isNotNull()
                .satisfies(r -> assertThat(r.status()).isTrue())
                .satisfies(r -> assertThat(r.message()).isEqualTo("Entry found"))
                .satisfies(r -> assertThat(r.values()).contains(entry));

        verify(database, times(1)).get(key);
    }

    @Test
    void shouldReturnErrorWhenEntryIsNotFound() {
        String key = "invalidKey";
        GetCommand underTest = new GetCommand(database, key);

        when(database.get(key)).thenReturn(Optional.empty());

        assertThat(underTest.execute())
                .isNotNull()
                .satisfies(r -> assertThat(r.status()).isFalse())
                .satisfies(r -> assertThat(r.message()).isEqualTo("Entry not found"))
                .satisfies(r -> assertThat(r.values()).isEmpty());

        verify(database, times(1)).get(key);
    }
}
