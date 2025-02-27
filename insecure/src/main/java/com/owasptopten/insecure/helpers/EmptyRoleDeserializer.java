package com.owasptopten.insecure.helpers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.owasptopten.insecure.userdetails.enums.Roles;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EmptyRoleDeserializer extends JsonDeserializer<List<Roles>> {

    @Override
    public List<Roles> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return jsonParser
                .<List<String>>readValueAs(new TypeReference<List<String>>() {}).stream()
                .map(Roles::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<Roles> getNullValue(DeserializationContext context) {
        return Collections.emptyList();
    }
}
