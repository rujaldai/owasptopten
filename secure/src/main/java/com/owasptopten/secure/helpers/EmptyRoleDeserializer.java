package com.owasptopten.secure.helpers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.owasptopten.secure.userdetails.enums.Roles;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Secure role deserializer that:
 * - Handles null values safely
 * - Validates role values
 * - Provides proper error handling
 * - Logs deserialization issues
 */
@Slf4j
public class EmptyRoleDeserializer extends JsonDeserializer<List<Roles>> {

    @Override
    public List<Roles> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        try {
            List<String> roleStrings = jsonParser.readValueAs(new TypeReference<List<String>>() {});
            
            if (roleStrings == null) {
                return Collections.emptyList();
            }

            return roleStrings.stream()
                    .map(role -> {
                        try {
                            return Roles.valueOf(role.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            log.warn("Invalid role value encountered: {}", role);
                            return null;
                        }
                    })
                    .filter(role -> role != null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error deserializing roles: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<Roles> getNullValue(DeserializationContext context) {
        return Collections.emptyList();
    }
} 