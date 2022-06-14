package org.neurodb;

import java.util.List;
import java.util.Map;

public class Node{
    Long id;
    List<String> labels;
    Map<String,ColVal> properties;

    public Node(Long id, List<String> labels, Map<String, ColVal> properties) {
        this.id = id;
        this.labels = labels;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Map<String, ColVal> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, ColVal> properties) {
        this.properties = properties;
    }
}

