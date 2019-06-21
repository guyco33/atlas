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
        //    City{
        //      geoNameId=3164603
        //      name='Venice'
        //      latitude=45.43713
        //      longitude=12.33265
        //      countryCode='IT'
        //      timeZone='Europe/Rome'
        //      admin1='Veneto'
        //      admin2='Provincia di Venezia'
        //    }

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
        //      City{
        //        geoNameId=3164603
        //        name='Venice'
        //        latitude=45.43713
        //        longitude=12.33265
        //        countryCode='IT'
        //        timeZone='Europe/Rome'
        //        admin1='Veneto'
        //        admin2='Provincia di Venezia'
        //      }
        //      City{
        //        geoNameId=3175265
        //        name='Giudecca'
        //        latitude=45.42477
        //        longitude=12.32906
        //        countryCode='IT'
        //        timeZone='Europe/Rome'
        //        admin1='Veneto'
        //        admin2='Provincia di Venezia'
        //      }
        //      City{
        //        geoNameId=3172456
        //        name='Murano'
        //        latitude=45.45857
        //        longitude=12.35683
        //        countryCode='IT'
        //        timeZone='Europe/Rome'
        //        admin1='Veneto'
        //        admin2='Provincia di Venezia'
        //      }
    }
}

