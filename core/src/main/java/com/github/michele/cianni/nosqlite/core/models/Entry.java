package com.github.michele.cianni.nosqlite.core.models;

import com.fasterxml.jackson.databind.JsonNode;

public record Entry(String key, JsonNode value) {
}
