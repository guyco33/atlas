package atlas;

import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.lang.Math.min;

public class CityIndexS2 implements Index
{
    private final static int LOW_LEVEL = 7;
    private final static int HIGH_LEVEL = 13;

    static class LevelMap
    {
        Map<String, Set<City>> map;
        int level;

        LevelMap() {}
        LevelMap(int level)
        {
            this.level = level;
            this.map = new HashMap<>();
        }

        void add(String cell, City city)
        {
            Set<City> cities = map.get(cell);
            if (cities == null) {
                cities = new HashSet<>();
                map.put(cell, cities);
            }
            cities.add(city);
        }
    }

    private final Map<Integer, LevelMap> s2MultiLevelIndex;

    @SuppressWarnings("unused")
    private CityIndexS2()
    {
        // Public no-args constructor needed for serialization lib.
        this(new HashSet<City>());
    }

    private CityIndexS2(Set<City> cities)
    {
        this.s2MultiLevelIndex = new HashMap<>();
        for (City city : cities) {
            this.insert(city);
        }
    }

    private void insert(City city)
    {
        for (int level = LOW_LEVEL; level <= HIGH_LEVEL; level++) {
            LevelMap levelMap = s2MultiLevelIndex.get(level);
            if (levelMap == null) {
                levelMap = new LevelMap(level);
                s2MultiLevelIndex.put(level, levelMap);
            }
            S2CellId cellId = S2CellId.fromLatLng(S2LatLng.fromDegrees(city.latitude, city.longitude)).parent(level);
            List<S2CellId> neighbours = new ArrayList<S2CellId>();
            cellId.getAllNeighbors(level, neighbours);
            for (S2CellId neighbour: neighbours ) {
                levelMap.add(neighbour.toToken(), city);
            }
        }
    }

    public City nearestNeighbour(double latitude, double longitude, double maxDistance)
    {
        List<City> hits = this.nearestNeighbours(latitude, longitude, maxDistance, 1);
        return hits.isEmpty() ? null : hits.get(0);
    }

    public List<City> nearestNeighbours(double latitude, double longitude, double maxDistance, int maxHits)
    {
        City center = new City(latitude, longitude);
        List<City> hits = new ArrayList<>();
        for (int level = HIGH_LEVEL; level >= LOW_LEVEL; level--) {
            SortedSet<City> levelMatched = new TreeSet<>(new DistanceComparator(center));
            S2CellId cellId = S2CellId.fromLatLng(S2LatLng.fromDegrees(latitude, longitude)).parent(level);
            List<S2CellId> neighbours = new ArrayList<S2CellId>();
            cellId.getAllNeighbors(level, neighbours);
            for (S2CellId neighbour: neighbours ) {
                Set<City> cities = s2MultiLevelIndex.get(level).map.get(neighbour.toToken());
                if (cities != null) {
                    levelMatched.addAll(cities);
                }
            }
            for (City hit : levelMatched) {
                hits.add(hit);
            }
            if(hits.size() >= maxHits) {
                break;
            }
        }
        return hits.subList(0,min(maxHits,hits.size()));
    }

    /**
     * Reads all cities and serializes this index which will be included
     * in the JAR file after doing a {@code mvn package}
     *
     * @param args an optional comma separated list of country codes to
     *             include in the index. When no parameter is used, all
     *             cities will be imported.
     * @throws FileNotFoundException when the file with cities couldn't
     *                               be found: ./{@value #INDEX_FOLDER_NAME}/{@value #CITY_DATA_FILE_NAME}
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        Set<String> countryCodes = new HashSet<>();
        Set<String> skipCountryCodes = new HashSet<>();

        if (args.length > 0) {
            for (String arg : Arrays.asList(args[0].toUpperCase().trim().split(","))) {
                if (arg.startsWith("~"))
                    skipCountryCodes.add(arg.substring(1));
                else {
                    countryCodes.add(arg);
                }
            }
        }

        if (countryCodes.isEmpty() && skipCountryCodes.isEmpty()) {
            System.out.printf("Reading all cities from %s/%s\n", DATA_FOLDER_NAME, ADMIN1_DATA_FILE_NAME);
        } else {
            if (!countryCodes.isEmpty()) {
                System.out.printf("Reading cities with country codes %s from %s/%s\n", countryCodes, DATA_FOLDER_NAME, ADMIN1_DATA_FILE_NAME);
            }
            if (!skipCountryCodes.isEmpty()) {
                System.out.printf("Skipping cities with country codes %s from %s/%s\n", skipCountryCodes, DATA_FOLDER_NAME, ADMIN1_DATA_FILE_NAME);
            }
        }

        Map<String, String> adminMap = new LinkedHashMap<>();

        adminMap.putAll(Utils.read(new File(DATA_FOLDER_NAME, ADMIN1_DATA_FILE_NAME), "\t"));
        adminMap.putAll(Utils.read(new File(DATA_FOLDER_NAME, ADMIN2_DATA_FILE_NAME), "\t"));

        Scanner scanner = new Scanner(new File(DATA_FOLDER_NAME, CITY_DATA_FILE_NAME));
        Set<City> cities = new LinkedHashSet<>();

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();
            City city = new City(line, adminMap);

            if (!skipCountryCodes.contains(city.countryCode.toUpperCase()) && (countryCodes.isEmpty() || countryCodes.contains(city.countryCode.toUpperCase()))) {
                cities.add(city);
            }
        }

        System.out.printf("Finished loading %s cities, writing index to disk...\n", cities.size());

        Utils.serialize(new CityIndexS2(cities), new File(INDEX_FOLDER_NAME, INDEX_FILE_NAME));

        System.out.printf("OK!\nRun `mvn package` to create a JAR file containing the newly created index.\n");
    }
}
