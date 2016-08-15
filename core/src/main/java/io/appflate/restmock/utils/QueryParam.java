package io.appflate.restmock.utils;

/**
 * A key/value set representing query parameters in an HTTP request.
 */
public class QueryParam {
  private String key;
  private String value;

  public QueryParam (String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return this.key;
  }

  public String getValue() {
    return this.value;
  }
}
