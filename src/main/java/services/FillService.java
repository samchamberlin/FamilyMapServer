package services;

import dao.*;
import model.Event;
import model.Location;
import model.Person;
import model.User;
import requests.FillRequest;
import results.Result;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/** The FillService class populates the server's database with generated data for the specified user name.
 * The required "userName" parameter must be a user already registered with the server. If there is
 * any data in the database already associated with the given user name, it is deleted. The
 * optional “generations” parameter lets the caller specify the number of generations of ancestors
 * to be generated, and must be a non-negative integer (the default is 4, which results in 31 new
 * persons each with associated events).
 * */
public class FillService {

    private final ArrayList<String> maleNames = ResourceDao.maleNames;
    private final ArrayList<String> femaleNames = ResourceDao.femaleNames;
    private final ArrayList<String> lastNames = ResourceDao.lastNames;
    private final ArrayList<Location> locations  = ResourceDao.locations;
    int numPersons;
    int numEvents;

    /** The fill method takes a FillRequest object and returns a Result object
     * @param request Contains the userName and numGenerations to fill
     * @return result Contains the message and success
     */
    public Result fill(FillRequest request) throws DataAccessException {

        numPersons = 0;
        numEvents = 0;

        //delete all data associated with the userName
        deleteUsernameData(request.getUsername());

        //get the user
        Database db = new Database();
        Connection conn = db.openConnection();
        UserDao uDao = new UserDao(conn);
        User user = uDao.find(request.getUsername());
        db.closeConnection(true);

        //check that user is valid
        if (user == null) {
            throw new DataAccessException(request.getUsername() + "isn't a user.");
        }

        //get user's person
        Person person = new Person(user.getPersonID(), user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getGender(), null, null, null);

        //generate events for that person
        String birthID = UUID.randomUUID().toString().substring(0, 8);
        Double birthLat = getRandomLocation().getLatitude();
        Double birthLong = getRandomLocation().getLongitude();
        String birthCountry = getRandomLocation().getCountry();
        String birthCity = getRandomLocation().getCity();
        int birthYear = 2000;
        Event birth = new Event(birthID, person.getAssociatedUsername(), person.getPersonID(), birthLat, birthLong,
                                birthCountry, birthCity, "Birth", birthYear);

        //add to database
        conn = db.openConnection();
        try {
            PersonDao pDao = new PersonDao(conn);
            pDao.insert(person);
            numPersons++;

            EventDao eDao = new EventDao(conn);
            eDao.insert(birth);
            numEvents++;

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            e.printStackTrace();
        }

        //generate family for that person
        generateFamily(person, birthYear, request.getNumGenerations());

        return new Result("Successfully added " + numPersons + " persons and " +
                numEvents + " events.", true);
    }

    /** Deletes all events associated with the userName
     * @param userName userName of person
     */
    private void deleteUsernameData(String userName) throws DataAccessException {
        //Open database
        Database db = new Database();
        Connection conn = db.openConnection();
        String sql = "DELETE FROM Event WHERE associatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
            db.closeConnection(true);
        } catch (SQLException | DataAccessException e) {
            db.closeConnection(false);
            throw new DataAccessException(e.getMessage());
        }

        conn = db.openConnection();
        sql = "DELETE FROM Person WHERE associatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
            db.closeConnection(true);
        } catch (SQLException | DataAccessException e) {
            db.closeConnection(false);
            throw new DataAccessException(e.getMessage());
        }

    }

    private void generateFamily (Person person, int personBirthYear, int numGenerations) throws DataAccessException {
        if (numGenerations == 0) {
            return;
        }

        //generate father and mother
        String fatherPersonID = UUID.randomUUID().toString().substring(0, 8);
        String fatherFirstName = getRandomName(maleNames);
        String fatherLastName = person.getLastName();
        Person father = new Person(fatherPersonID, person.getAssociatedUsername(), fatherFirstName, fatherLastName,
                                    'm', "", "", "");
        String motherPersonID = UUID.randomUUID().toString().substring(0, 8);
        String motherFirstName = getRandomName(femaleNames);
        String motherLastName = getRandomName(lastNames);
        Person mother = new Person(motherPersonID, person.getAssociatedUsername(), motherFirstName, motherLastName,
                'f', "", "", "");
        father.setSpouseID(motherPersonID);
        mother.setSpouseID(fatherPersonID);

        //generate father and mother events
        String fatherBirthID = UUID.randomUUID().toString().substring(0, 8);
        Location birthLocation = getRandomLocation();
        Double birthLat = birthLocation.getLatitude();
        Double birthLong = birthLocation.getLongitude();
        String birthCountry = birthLocation.getCountry();
        String birthCity = birthLocation.getCity();
        int birthYear = personBirthYear - 25;
        Event fatherBirth = new Event(fatherBirthID, person.getAssociatedUsername(), fatherPersonID, birthLat, birthLong,
                                        birthCountry, birthCity, "Birth", birthYear);
        String motherBirthID = UUID.randomUUID().toString().substring(0, 8);
        Event motherBirth = new Event(motherBirthID, person.getAssociatedUsername(), motherPersonID, birthLat, birthLong,
                                        birthCountry, birthCity, "Birth", birthYear);

        String fatherMarriageID = UUID.randomUUID().toString().substring(0, 8);
        int marriageYear = birthYear + 20;
        Event fatherMarriage = new Event(fatherMarriageID, person.getAssociatedUsername(), fatherPersonID, birthLat, birthLong,
                                            birthCountry, birthCity, "Marriage", marriageYear);
        String motherMarriageID = UUID.randomUUID().toString().substring(0, 8);
        Event motherMarriage = new Event(motherMarriageID, person.getAssociatedUsername(), motherPersonID, birthLat, birthLong,
                birthCountry, birthCity, "Marriage", marriageYear);

        String fatherDeathID = UUID.randomUUID().toString().substring(0, 8);
        int fatherDeathYear = birthYear + 75;
        Event fatherDeath = new Event(fatherDeathID, person.getAssociatedUsername(), fatherPersonID, birthLat, birthLong,
                                    birthCountry, birthCity, "Death", fatherDeathYear);
        String motherDeathID = UUID.randomUUID().toString().substring(0, 8);
        int motherDeathYear = birthYear + 80;
        Event motherDeath = new Event(motherDeathID, person.getAssociatedUsername(), motherPersonID, birthLat, birthLong,
                birthCountry, birthCity, "Death", motherDeathYear);

        //link child
        person.setFatherID(fatherPersonID);
        person.setMotherID(motherPersonID);

        //add to database
        Database db = new Database();
        Connection conn = db.openConnection();
        try {
            PersonDao pDao = new PersonDao(conn);
            pDao.delete(person.getPersonID());
            pDao.insert(person);
            pDao.insert(father);
            pDao.insert(mother);
            numPersons += 2;

            EventDao eDao = new EventDao(conn);
            eDao.insert(fatherBirth);
            eDao.insert(fatherMarriage);
            eDao.insert(fatherDeath);
            eDao.insert(motherBirth);
            eDao.insert(motherMarriage);
            eDao.insert(motherDeath);
            numEvents += 6;

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            e.printStackTrace();
        }

        //recurse
        generateFamily(father, birthYear, numGenerations - 1);
        generateFamily(mother, birthYear, numGenerations - 1);
    }

    private String getRandomName(ArrayList<String> names) {
        Random rand = new Random();
        int randLocationIndex = rand.nextInt(names.size());
        return names.get(randLocationIndex);
    }

    public Location getRandomLocation() {
        Random rand = new Random();
        int randLocationIndex = rand.nextInt(locations.size());
        return locations.get(randLocationIndex);

    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

}