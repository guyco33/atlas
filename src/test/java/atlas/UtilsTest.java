package atlas;

import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import static atlas.Index.COUNTY_DATA_FILE_NAME;
import static atlas.Index.DATA_FOLDER_NAME;
import static org.testng.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void readCountryData() {
        Map<String, ArrayList<String>> map;
        map = Utils.read(new File(DATA_FOLDER_NAME, COUNTY_DATA_FILE_NAME), "\t");
        assertEquals(map.get("IL").get(3),"Israel");
        assertEquals(map.get("GB").get(3),"United Kingdom");
        assertEquals(map.get("RU").get(3),"Russia");
    }
}