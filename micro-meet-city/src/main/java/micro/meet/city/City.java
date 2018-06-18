package micro.meet.city;

import java.util.*;

public class City {
  public long id;
  public String city;
  public String country;
  public double lon, lat;
  public Map asMap() {
     Map result = new HashMap();
     result.put("id",id);
     result.put("city",city);
     result.put("country",country);
     result.put("lat",lat);
     result.put("lon",lon);
     return result;
  }
  public City() {}
  public City(Map data) {
	this.id = (Long)data.get("id");
	this.city = (String)data.get("city");
	this.country = (String)data.get("country");
	this.lat = (Double)data.get("lat");
	this.lon = (Double)data.get("lon");
  }
}

class CityResult {
    public List<City> results;
}
