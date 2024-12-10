package com.github.michele.cianni.nosqlite.core.command;

import com.github.michele.cianni.nosqlite.core.api.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DeleteCommandTest {

    private Database database;

    @BeforeEach
    void setUp() {
        database = mock(Database.class);
    }

    @Test
    void shouldReturnSuccessWhenKeyIsDeleted() {
        String key = "testKey";
        DeleteCommand underTest = new DeleteCommand(database, key);

        when(database.delete(key)).thenReturn(true);

        assertThat(underTest.execute())
                .isNotNull()
                .satisfies(r -> assertThat(r.status()).isTrue())
                .satisfies(r -> assertThat(r.message()).isEqualTo("Entry deleted"));

        verify(database, times(1)).delete(key);
    }

    @Test
    void shouldReturnFailureWhenKeyNotFound() {
        String key = "missingKey";
        DeleteCommand underTest = new DeleteCommand(database, key);

        when(database.delete(key)).thenReturn(false);

        assertThat(underTest.execute())
                .isNotNull()
                .satisfies(r -> assertThat(r.status()).isFalse())
                .satisfies(r -> assertThat(r.message()).isEqualTo("Entry not found"));

        verify(database, times(1)).delete(key);
    }
}
