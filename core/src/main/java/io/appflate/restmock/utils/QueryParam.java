package io.appflate.restmock.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * A key/value set representing query parameters in an HTTP request.
 */
public class QueryParam {

    private final String key;
    private final List<String> values;

    public QueryParam(String key, List<String> values) {
        this.key = key;
        this.values = values;
    }

    public QueryParam(String key, String... values) {
        this.key = key;
        this.values = new LinkedList<>();

        for (String val : values) {
            this.values.add(val);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof QueryParam)) {
            return false;
        }

        QueryParam otherQueryParam = (QueryParam) other;

        if (this == other) {
            return true;
        }

        if (!this.key.equals(otherQueryParam.key)) {
            return false;
        }

        if (this.values.size() != otherQueryParam.values.size()) {
            return false;
        }

        for (String nextVal : this.values) {
            if (!otherQueryParam.values.contains(nextVal)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + (key == null ? 0 : key.hashCode());
        result = 37 * result + (values == null ? 0 : values.hashCode());

        return result;
    }

    public String getKey() {
        return this.key;
    }

    public List<String> getValues() {
        return this.values;
    }
}
