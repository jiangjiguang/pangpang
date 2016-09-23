package com.pangpang.util.ip;

/**
 * Created by jiangjg on 2016/9/23.
 */
public class IpSegmentRegion {
    int country;
    int region;
    int city;
    int county;

    int index;
    public IpSegmentRegion(int country,int region, int city, int county){
        this.country=country;
        this.region=region;
        this.city=city;
        this.county=county;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getCounty() {
        return county;
    }

    public void setCounty(int county) {
        this.county = county;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + city;
        result = prime * result + country;
        result = prime * result + county;
        result = prime * result + region;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IpSegmentRegion other = (IpSegmentRegion) obj;
        if (city != other.city)
            return false;
        if (country != other.country)
            return false;
        if (county != other.county)
            return false;
        if (region != other.region)
            return false;
        return true;
    }

    public String toString(){
        return new StringBuilder().append(country).append(",").append(region).append(",").append(city).append(",").append(county).append(",").append(index).toString();
    }

}
