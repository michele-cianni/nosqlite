package com.github.michele.cianni.nosqlite.core.command;

import com.github.michele.cianni.nosqlite.core.console.ConsoleCommandParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HelpCommandTest {

    private ConsoleCommandParser parser;
    private HelpCommand helpCommand;

    @BeforeEach
    void setUp() {
        parser = mock(ConsoleCommandParser.class);
        helpCommand = new HelpCommand(parser);
    }

    @Test
    void shouldReturnSupportedCommands() {
        List<TypeCommand> supportedCommands = List.of(TypeCommand.GET, TypeCommand.INSERT, TypeCommand.HELP);
        when(parser.getSupportedCommands()).thenReturn(supportedCommands);

        CommandResult result = helpCommand.execute();

        assertThat(result).isNotNull()
                .satisfies(r -> assertThat(r.status()).isTrue())
                .satisfies(r -> assertThat(r.message()).isEqualTo("Supported commands"))
                .satisfies(r -> assertThat(r.values()).containsExactlyInAnyOrder(
                        supportedCommands.stream().map(TypeCommand::getDescription).sorted().toArray()
                ));

        verify(parser).getSupportedCommands();
    }
}