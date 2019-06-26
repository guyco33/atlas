package atlas;

import java.util.List;

public class Example
{
    public static void main(String[] args)
    {
        // (lat, lng) somewhere in Venice
        double lat = 45.436636;
        double lng = 12.326413;

        if (args.length >= 2) {
            lat = Double.parseDouble(args[0]);
            lng = Double.parseDouble(args[1]);
        }

        // Find a single city
        City city = new Atlas().find(lat, lng);

        System.out.println(city);

        int limit = 3;
        int maxDistance = 5000;

        if (args.length >= 3) {
            limit = Integer.parseInt(args[2]);
        }
        if (args.length >= 4) {
            maxDistance = Integer.parseInt(args[3]);
        }

        // Finds 3 cities around the (lat,lng) in a radius of 5 kilometers
        List<City> cities = new Atlas()
                .withLimit(limit)
                .withMaxDistance(maxDistance)
                .findAll(lat, lng);

        for (City c : cities) {
            System.out.println(c);
        }
    }
}

