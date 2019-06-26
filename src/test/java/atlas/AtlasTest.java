package atlas;

import org.junit.Test;

import static org.junit.Assert.*;

public class AtlasTest {

    @Test
    public void find() {
        Atlas atlas = new Atlas();
        City city = atlas.find(29.896322,35.059227);
        assertEquals(city.name, "Eilat");
        assertEquals(city.admin1, "Southern District");
        assertEquals(city.countryName, "Israel");
        assertEquals(city.capital, "Jerusalem");
        assertEquals(city.currencyCode, "ILS");
        assertEquals(city.continent, "AS");

    }
}