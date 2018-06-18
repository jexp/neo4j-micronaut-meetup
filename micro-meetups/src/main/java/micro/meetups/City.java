package micro.meetups;

import java.util.*;

public class City {
  public String zip;
  public String country;
  public String localized_country_name;
  public double distance;
  public String city;
  public double lon, lat;
  public double ranking;
  public long id;
  public int member_count;
  // {"zip":"meetup15","country":"de","localized_country_name":"Germany","distance":0.43443689334362534,"city":"Dresden","lon":13.739999771118164,"ranking":0,
  // "id":1007712,"member_count":73,"lat":51.04999923706055}

  public Map<String,Object> asMap() {
      Map<String,Object> result = new HashMap<>();
      result.put("id",id);
      result.put("city",city);
      result.put("country",country);
      return result;
  }
}

class CityResult {
    public List<City> results;
}