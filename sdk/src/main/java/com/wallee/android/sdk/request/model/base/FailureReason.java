package com.wallee.android.sdk.request.model.base;

import java.util.List;
import java.util.Map;

/**
 * Created by simonwalter on 31.07.17.
 */

public class FailureReason {

    private FailureCategory category;
    private Map<String, String> description;
    private List<Long> features;
    private long id;
    private Map<String, String> name;

    public FailureCategory getCategory() {
        return category;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public List<Long> getFeatures() {
        return features;
    }

    public long getId() {
        return id;
    }

    public Map<String, String> getName() {
        return name;
    }
}
