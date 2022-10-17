package org.example.dto;


public class Currency {
    private long id;
    private long resourceId;
    private String name;
    private int count;

    public Currency(long id, long resourceId, String name, int count) {
        this.id = id;
        this.resourceId = resourceId;
        this.name = name;
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getResourceId() {
        return resourceId;
    }

    public void setResourceId(long resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", resourceId=" + resourceId +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
