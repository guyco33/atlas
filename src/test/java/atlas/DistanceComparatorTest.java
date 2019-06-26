package atlas;


import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DistanceComparatorTest {

  @Test
  public void testCompare() throws Exception { 

    final City rotterdam = new City(51.933900, 4.472554);
    final City amsterdam = new City(52.375186, 4.897244);
    final City utrecht = new City(52.090066, 5.122477);
    final City groningen = new City(53.232561, 6.554476);
    final City schiedam = new City(51.916742, 4.392050);

    DistanceComparator comparator = new DistanceComparator(rotterdam);

    assertEquals(comparator.compare(amsterdam, schiedam), 1);
    assertEquals(comparator.compare(amsterdam, groningen), -1);
    assertEquals(comparator.compare(amsterdam, amsterdam), 0);
    assertEquals(comparator.compare(schiedam, utrecht), -1);
    assertEquals(comparator.compare(utrecht, schiedam), 1);
    assertEquals(comparator.compare(schiedam, rotterdam), 1);
  }
} 
