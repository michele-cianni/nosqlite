package com.github.michele.cianni.nosqlite.core.command;

import com.github.michele.cianni.nosqlite.core.api.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BeginCommandTest {

    private Database database;

    private BeginCommand underTest;

    @BeforeEach
    void setUp() {
        database = mock(Database.class);
        underTest = new BeginCommand(database);
    }

    @Test
    void shouldReturnSuccessWhenBeginTransactionReturnsTrue() {
        when(database.beginTransaction()).thenReturn(true);

        assertThat(underTest.execute()).isNotNull()
                .satisfies(r -> assertThat(r.status()).isTrue())
                .satisfies(r -> assertThat(r.message()).isEqualTo("Transaction started"));

        verify(database, times(1)).beginTransaction();
    }

    @Test
    void shouldReturnFailureWhenBeginTransactionReturnsFalse() {
        when(database.beginTransaction()).thenReturn(false);

        assertThat(underTest.execute()).isNotNull()
                .satisfies(r -> assertThat(r.status()).isFalse())
                .satisfies(r -> assertThat(r.message()).isEqualTo("Error starting transaction"));

        verify(database, times(1)).beginTransaction();
    }
}
