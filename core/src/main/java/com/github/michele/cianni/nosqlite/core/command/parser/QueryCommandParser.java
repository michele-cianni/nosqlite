package com.github.michele.cianni.nosqlite.core.command.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.michele.cianni.nosqlite.core.api.Database;
import com.github.michele.cianni.nosqlite.core.command.Command;
import com.github.michele.cianni.nosqlite.core.command.QueryCommand;
import com.github.michele.cianni.nosqlite.core.command.TypeCommand;
import com.github.michele.cianni.nosqlite.core.models.Entry;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class QueryCommandParser implements CommandParser {

    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private final Database database;
    private final ObjectMapper mapper;

    public QueryCommandParser(Database database) {
        this.database = database;
        this.mapper = new ObjectMapper();
    }

    @Override
    public Command parse(String commandLine) throws CommandParsingException {
        List<String> args = List.of(commandLine.trim().split(" "));

        if (args.isEmpty()) {
            throw new CommandParsingException("Empty command");
        }

        if (args.size() < 2 || args.size() > 5) {
            throw new CommandParsingException("Invalid number of arguments: " + commandLine);
        }

        String commandName = args.getFirst();
        if (TypeCommand.QUERY.check(commandName)) {
            List<String> parameters = args.subList(1, args.size());
            if (parameters.isEmpty() || parameters.size() > 4) {
                throw new CommandParsingException("Invalid number of parameters: " + parameters);
            }

            Predicate<Entry> filter = parseFilterJson(parameters.getFirst());
            if (parameters.size() == 1) {
                return new QueryCommand(database, filter);
            }
            if (parameters.size() == 4) {
                String sortKeyword = parameters.get(1);
                checkSortKeyword(sortKeyword);

                String fieldOrder = parameters.get(2);
                checkFieldOrder(fieldOrder);

                String order = parameters.getLast();
                checkOrder(order);
                return new QueryCommand(database, filter, fieldOrder, order);
            }
        }

        throw new CommandParsingException("Command is not a query command: " + commandName);
    }


    private Predicate<Entry> parseFilterJson(String filterJson) throws CommandParsingException {
        try {
            JsonNode filterJsonNode = mapper.readTree(filterJson);
            String fieldNameToFilter = filterJsonNode.fieldNames().next();

            JsonNode operatorJsonNode = filterJsonNode.get(fieldNameToFilter);
            String operator = operatorJsonNode.fieldNames().next();
            String value = operatorJsonNode.get(operator).asText();

            Optional<DollarOperator> dollarOperatorFound = DollarOperator.fromValue(operator);

            if (dollarOperatorFound.isEmpty()) {
                throw new CommandParsingException("Invalid operator: " + operator);
            }

            return entry -> dollarOperatorFound.get().filter(entry, fieldNameToFilter, value);
        } catch (JsonProcessingException e) {
            throw new CommandParsingException("Invalid filter JSON: " + filterJson);
        }
    }

    private void checkSortKeyword(String sort) throws CommandParsingException {
        if (!sort.equalsIgnoreCase("sort")) {
            throw new CommandParsingException("Missing sort keyword");
        }
    }

    private void checkFieldOrder(String field) throws CommandParsingException {
        if (field.isBlank()) {
            throw new CommandParsingException("Empty field");
        }
    }

    private void checkOrder(String order) throws CommandParsingException {
        if (order != null && (!ASC.equalsIgnoreCase(order) && !DESC.equalsIgnoreCase(order))) {
            throw new CommandParsingException("Invalid order: " + order);
        }
    }

    private enum DollarOperator {
        EQUALS("$eq") {
            @Override
            boolean filter(Entry entry, String field, String value) {
                return entry.value().get(field).asText().equals(value);
            }
        },
        NOT_EQUALS("$ne") {
            @Override
            boolean filter(Entry entry, String field, String value) {
                return !entry.value().get(field).asText().equals(value);
            }
        },
        GREATER_THAN("$gt") {
            @Override
            boolean filter(Entry entry, String field, String value) {
                return entry.value().get(field).asInt() > Integer.parseInt(value);
            }
        },
        LOWER_THAN("$lt") {
            @Override
            boolean filter(Entry entry, String field, String value) {
                return entry.value().get(field).asInt() < Integer.parseInt(value);
            }
        },
        GREATER_THAN_EQUALS("$gte") {
            @Override
            boolean filter(Entry entry, String field, String value) {
                return entry.value().get(field).asInt() >= Integer.parseInt(value);
            }
        },
        LOWER_THAN_EQUALS("$lte") {
            @Override
            boolean filter(Entry entry, String field, String value) {
                return entry.value().get(field).asInt() <= Integer.parseInt(value);
            }
        };

        private final String value;

        DollarOperator(String value) {
            this.value = value;
        }

        public static Optional<DollarOperator> fromValue(String value) {
            for (DollarOperator operator : values()) {
                if (operator.getValue().equals(value)) {
                    return Optional.of(operator);
                }
            }
            return Optional.empty();
        }

        public String getValue() {
            return value;
        }

        abstract boolean filter(Entry entry, String field, String value);
    }
}
