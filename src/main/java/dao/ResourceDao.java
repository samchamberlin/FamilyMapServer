package dao;

import model.Location;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/** ResourceDao interacts with the resources directory to receive data like names and locations */
public class ResourceDao {

    public static final ArrayList<String> maleNames;
    public static final ArrayList<String> femaleNames;
    public static final ArrayList<String> lastNames;
    public static final ArrayList<Location> locations;

    static {
        maleNames = readNames("src/main/resources/mnames.json");
        femaleNames = readNames("src/main/resources/fnames.json");
        lastNames = readNames("src/main/resources/snames.json");
        locations = readLocations("src/main/resources/locations.json");
    }

    /** readNames parses names from JSON files
     * @return names An array list of names
     */
    private static ArrayList<String> readNames(String path) {

        ArrayList<String> names = new ArrayList<>();

        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            String jsonData = scanner.useDelimiter("\\A").next();

            //Parse into Json array
            JSONArray jsonArray = new JSONArray(jsonData);

            //Convert to array list
            for (int i = 0; i < jsonArray.length(); i++) {
                names.add(jsonArray.getString(i));
            }

        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return names;
    }

    /** readLocations parses locations from JSON files
     * @return locations An array list of locations
     */
    private static ArrayList<Location> readLocations(String path) {

        ArrayList<Location> locations = new ArrayList<>();

        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            String jsonData = scanner.useDelimiter("\\A").next();

            //Parse into Json array
            JSONArray jsonArray = new JSONArray(jsonData);

            //Convert to array list
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject l = jsonArray.getJSONObject(i);
                locations.add(new Location(l.getString("country"), l.getString("city"),
                                            l.getDouble("latitude"), l.getDouble("longitude")));
            }

        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return locations;
    }

}


//    /**
//     * @return ArrayList of location objects from locations.json
//     */
//    private static ArrayList<Location> readLocations() {
//
//        ArrayList<Location> locs = new ArrayList<>();
//
//        try {
//
//            // read json file as one string
//            File locationsFile = new File("server/src/main/resources/json/locations.json");
//            Scanner scanner = new Scanner(locationsFile);
//            String locationsJson = scanner.useDelimiter("\\A").next();
//
//            // convert string to json array
//            JSONArray jsonArray = null;
//            jsonArray = new JSONArray(locationsJson);
//
//            // convert json array to array list
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject location = jsonArray.getJSONObject(i);
//                String country = location.getString("country");
//                String city = location.getString("city");
//                double latitude = location.getDouble("latitude");
//                double longitude = location.getDouble("longitude");
//
//                locs.add( new Location(
//                        country,
//                        city,
//                        latitude,
//                        longitude
//                ) );
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(1);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return locs;
//
//    }
//




//
//    public static Location getRandomLocation() {
//
//        Random rand = new Random();
//        int randLocationIndex = rand.nextInt(locations.size());
//        return locations.get(randLocationIndex);
//
//    }
//
//    public static String getRandomMaleName() {
//
//        Random rand = new Random();
//        int randIndex = rand.nextInt(maleNames.size());
//        return maleNames.get(randIndex);
//
//    }
//
//    public static String getRandomFemaleName() {
//
//        Random rand = new Random();
//        int randIndex = rand.nextInt(femaleNames.size());
//        return femaleNames.get(randIndex);
//
//    }
//
//    public static String getRandomLastName() {
//
//        Random rand = new Random();
//        int randIndex = rand.nextInt(lastNames.size());
//        return lastNames.get(randIndex);
//
//    }
//
//}