package com.marketplace.location.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Delegate;

class PostcodeResponse {
     @Delegate
     private final PostcodeData result;

     @JsonCreator
     PostcodeResponse(@JsonProperty("result") final PostcodeData result) {
          this.result = result;
     }
}

@Data
class PostcodeData {
     private final String postcode;
     private final String outcode;
     private final float longitude;
     private final float latitude;

     @JsonCreator
     public PostcodeData(@JsonProperty("location") final String postcode,
                         @JsonProperty("outcode") final String outcode,
                         @JsonProperty("longitude") final float longitude,
                         @JsonProperty("latitude") final float latitude) {
          this.postcode = postcode;
          this.outcode = outcode;
          this.longitude = longitude;
          this.latitude = latitude;
     }
}