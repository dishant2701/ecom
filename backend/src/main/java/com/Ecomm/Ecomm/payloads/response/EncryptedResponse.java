package com.Ecomm.Ecomm.payloads.response;

import com.Ecomm.Ecomm.utils.Json;

public class EncryptedResponse {
  private final String data;

  public EncryptedResponse(Object obj) throws Exception {
    this.data = Json.serialize(obj);
  }

  public String getData() {
    return data;
  }

}
