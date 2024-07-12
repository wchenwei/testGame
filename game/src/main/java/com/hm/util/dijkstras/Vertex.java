package com.hm.util.dijkstras;

/**
 * Description:
 * User: yang xb
 * Date: 2018-11-12
 */
public class Vertex implements Comparable<Vertex> {
    private Integer cityId;
    private Integer distance;

    public Vertex(Integer cityId) {
        super();
        this.cityId = cityId;
        this.distance = 1;
    }

    public Vertex(Integer cityId, Integer distance) {
        super();
        this.cityId = cityId;
        this.distance = distance;
    }

    public Integer getCityId() {
        return cityId;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((distance == null) ? 0 : distance.hashCode());
        result = prime * result + ((cityId == null) ? 0 : cityId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;

        Vertex other = (Vertex) obj;
        if (distance == null) {
            if (other.distance != null)
                return false;
        } else if (!distance.equals(other.distance)) {
            return false;
        }

        return cityId == null ? other.cityId == null : cityId.equals(other.cityId);
    }

    @Override
    public String toString() {
        return "Vertex [cityId=" + cityId + ", distance=" + distance + "]";
    }

    @Override
    public int compareTo(Vertex o) {
        if (this.distance < o.distance)
            return -1;
        else if (this.distance > o.distance)
            return 1;
        else
            return this.getCityId().compareTo(o.getCityId());
    }

}

