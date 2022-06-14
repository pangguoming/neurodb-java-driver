package org.neurodb;

import java.util.Map;

public class Link{
    Long id;
    Long startNodeId;
    Long endNodeId;

    String Type;
    Map<String,ColVal> properties;

    public Link(Long id, Long startNodeId, Long endNodeId, String type, Map<String, ColVal> properties) {
        this.id = id;
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        Type = type;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStartNodeId() {
        return startNodeId;
    }

    public void setStartNodeId(Long startNodeId) {
        this.startNodeId = startNodeId;
    }

    public Long getEndNodeId() {
        return endNodeId;
    }

    public void setEndNodeId(Long endNodeId) {
        this.endNodeId = endNodeId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Map<String, ColVal> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, ColVal> properties) {
        this.properties = properties;
    }
}

