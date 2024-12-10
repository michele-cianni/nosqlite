package com.github.michele.cianni.nosqlite.core.command;

import com.github.michele.cianni.nosqlite.core.api.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RollbackCommandTest {

    private Database database;

    private RollbackCommand underTest;

    @BeforeEach
    void setUp() {
        database = mock(Database.class);
        underTest = new RollbackCommand(database);
    }

    @Test
    void shouldReturnSuccessWhenRollbackTransactionReturnsTrue() {
        when(database.rollbackTransaction()).thenReturn(true);

        assertThat(underTest.execute()).isNotNull()
                .satisfies(r -> assertThat(r.status()).isTrue())
                .satisfies(r -> assertThat(r.message()).isEqualTo("Transaction rolled back"));
    }

    @Test
    void shouldReturnFailureWhenRollbackTransactionReturnsFalse() {
        when(database.rollbackTransaction()).thenReturn(false);

        assertThat(underTest.execute()).isNotNull()
                .satisfies(r -> assertThat(r.status()).isFalse())
                .satisfies(r -> assertThat(r.message()).isEqualTo("Error rolling back transaction"));
    }

}