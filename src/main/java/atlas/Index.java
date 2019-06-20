package atlas;

import java.io.Serializable;
import java.util.List;

public interface Index extends Serializable {

    String DATA_FOLDER_NAME = "./data";
    String ADMIN1_DATA_FILE_NAME = "admin1CodesASCII.txt";
    String ADMIN2_DATA_FILE_NAME = "admin2Codes.txt";
    String CITY_DATA_FILE_NAME = "cities1000.txt";

    String INDEX_FOLDER_NAME = "./src/main/resources";
    String INDEX_FILE_NAME = "index.ser";

    City nearestNeighbour(double latitude, double longitude, double maxDistance);

    List<City> nearestNeighbours(double latitude, double longitude, double maxDistance, int maxHits);


}
