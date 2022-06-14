package org.neurodb;

class Test {
    public static void main(String[] args) {
        try {
            NeuroDBDriver neuroDBDriver = new NeuroDBDriver("127.0.0.1", 8839);
            ResultSet resultSet = neuroDBDriver.executeQuery("match (n) return n");
            resultSet = neuroDBDriver.executeQuery("match (n)-[r]->(m) return n,r,m");
            neuroDBDriver.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
