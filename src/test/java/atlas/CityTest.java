package atlas; 

import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static atlas.Index.ADMIN1_DATA_FILE_NAME;
import static atlas.Index.ADMIN2_DATA_FILE_NAME;
import static atlas.Index.COUNTY_DATA_FILE_NAME;
import static atlas.Index.DATA_FOLDER_NAME;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CityTest {

  @Test
  public void testCity() {
    String line = "3428453\tSanta Ana\tSanta Ana\tSanta Ana\t-30.90004\t-57.93162\tP\tPPL\tAR\t\t08\t30028\t\t\t2059\t\t50\tAmerica/Argentina/Cordoba\t2016-01-30\n";
    Map<String, ArrayList<String>> adminMap = new LinkedHashMap<>();
    adminMap.putAll(Utils.read(new File(DATA_FOLDER_NAME, ADMIN1_DATA_FILE_NAME), "\t"));
    adminMap.putAll(Utils.read(new File(DATA_FOLDER_NAME, ADMIN2_DATA_FILE_NAME), "\t"));
    adminMap.putAll(Utils.read(new File(DATA_FOLDER_NAME, COUNTY_DATA_FILE_NAME), "\t"));
    City city = new City(line,adminMap);
    Map info = city.info();
    Map expected = new LinkedHashMap<String, Object>();
    expected.put("geoNameId",3428453);
    expected.put("name","Santa Ana");
    expected.put("latitude",-30.90004);
    expected.put("longitude",-57.93162);
    expected.put("countryCode","AR");
    expected.put("timeZone","America/Argentina/Cordoba");
    expected.put("admin1","Entre Rios");
    expected.put("admin2","Departamento de Federación");
    expected.put("countryName","Argentina");
    expected.put("capital","Buenos Aires");
    expected.put("countryArea",2766890.0);
    expected.put("countryPopulation",41343201L);
    expected.put("continent","SA");
    expected.put("currencyCode","ARS");
    expected.put("currencyName","Peso");
    assertEquals(info, expected);
  }

  @Test
  public void testDistanceTo() throws Exception {

    final City rotterdam = new City(51.933900, 4.472554);
    final City amsterdam = new City(52.375186, 4.897244);
    final City utrecht = new City(52.090066, 5.122477);
    final City groningen = new City(53.232561, 6.554476);

    // http://www.nhc.noaa.gov/gccalc.shtml
    assertTrue(approximateOk(rotterdam.distanceTo(amsterdam), 57));
    assertTrue(approximateOk(rotterdam.distanceTo(groningen), 201));
    assertTrue(approximateOk(utrecht.distanceTo(utrecht), 0));
  }

  private static boolean approximateOk(double distance, double expected) {
    double delta = Math.abs((distance / 1000.0) - expected);

    // Must be accurate within 3 km.
    return delta <= 3;
  }


} 
