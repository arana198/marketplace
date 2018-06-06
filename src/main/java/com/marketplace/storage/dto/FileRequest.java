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

     @Valid
     @NotNull(message = "bucket is mandatory")
     private final BucketRequest bucket;

     @NotNull(message = "name is mandatory")
     private final String name;

     private final String description;

     @NotNull(message = "fileType is mandatory")
     private final FileType fileType;

     public enum FileType {
          PROFILE_IMAGE("PROFILE_IMAGE", true),
          MORTGAGE_APPLICATION_DOCUMENT("MORTGAGE_APPLICATION_DOCUMENT", false),
          BROKER_CERTIFICATE("BROKER_CERTIFICATE", false);

          private final String value;
          private final boolean unrestricted;

          FileType(final String value, final boolean unrestricted) {
               this.value = value;
               this.unrestricted = unrestricted;
          }

          public static FileType getRoleFromString(final String value) {
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

          public boolean isPublic() {
               return unrestricted;
          }
     }

     @JsonPOJOBuilder(withPrefix = "")
     public static class FileRequestBuilder {
     }
}
