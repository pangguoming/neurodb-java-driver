package org.neurodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordSet{

    List<String> labels=null;
    List<String> types=null;
    List<String> keyNames=null;
    List<Node> nodes=null;
    List<Link> links=null;
    List<List<ColVal>> records=null;

    public RecordSet() {
        nodes=new ArrayList<Node>();
        links=new ArrayList<Link>();
        records=new ArrayList<List<ColVal>>();
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getKeyNames() {
        return keyNames;
    }

    public void setKeyNames(List<String> keyNames) {
        this.keyNames = keyNames;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<List<ColVal>> getRecords() {
        return records;
    }

    public void setRecords(List<List<ColVal>> records) {
        this.records = records;
    }
}
