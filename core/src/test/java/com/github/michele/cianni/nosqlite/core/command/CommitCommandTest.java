package com.github.michele.cianni.nosqlite.core.command;

import com.github.michele.cianni.nosqlite.core.api.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CommitCommandTest {

    private Database database;

    private CommitCommand underTest;

    @BeforeEach
    void setUp() {
        database = mock(Database.class);
        underTest = new CommitCommand(database);
    }

    @Test
    void shouldReturnSuccessWhenCommitTransactionReturnsTrue() {
        when(database.commitTransaction()).thenReturn(true);

        assertThat(underTest.execute()).isNotNull()
                .satisfies(r -> assertThat(r.status()).isTrue())
                .satisfies(r -> assertThat(r.message()).isEqualTo("Transaction committed"));

        verify(database, times(1)).commitTransaction();
    }
}