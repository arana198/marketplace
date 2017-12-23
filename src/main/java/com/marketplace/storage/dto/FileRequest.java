package com.marketplace.storage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Builder
@Data
@JsonDeserialize(builder = FileRequest.FileRequestBuilder.class)
@JsonIgnoreProperties
public class FileRequest {

    public enum FileType {
        PROFILE_IMAGE("PROFILE_IMAGE"),
        BROKER_CERTIFICATE("BROKER_CERTIFICATE");

        private String value;

        FileType(String value) {
            this.value = value;
        }

        public static FileType getRoleFromString(String value) {
            for (FileType file : FileType.values()) {
                if (file.getValue().equalsIgnoreCase(value)) {
                    return file;
                }
            }

            return null;
        }

        public String getValue() {
            return this.value;
        }
    }

    @Valid
    @NotNull(message = "bucket is mandatory")
    private final BucketRequest bucket;

    @NotNull(message = "name is mandatory")
    private final String name;

    @NotNull(message = "description is mandatory")
    private final String description;

    @NotNull(message = "fileType is mandatory")
    private final FileType fileType;

    private final boolean show;

    @JsonPOJOBuilder(withPrefix = "")
    public static class FileRequestBuilder {
    }
}
