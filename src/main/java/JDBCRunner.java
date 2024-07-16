import java.sql.*;
import java.util.Scanner;

public class JDBCRunner {

    private static final String PROTOCOL = "jdbc:postgresql://";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL_LOCALE_NAME = "localhost/";

    private static final String DATABASE_NAME = "CarService";

    public static final String DATABASE_URL = PROTOCOL + URL_LOCALE_NAME + DATABASE_NAME;
    public static final String USER_NAME = "postgres";
    public static final String DATABASE_PASS = "postgres";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS)) {


//            addNewOwner(connection, "Александр", "Киселев", 34, new Date(90, 2, 21));
//            addNewCar(connection, "volkswagen", "green", 380, new Date(21, 4, 11), 4, "ур123к");
//            addNewRequest(connection, new Date(124, 6, 15), "Прокол колеса", 3, 8);


//            deleteOwner(connection, 3);
//            deleteCar(connection, 8);
//            deleteRequest(connection,7);
//            deleteRequest(connection, 6);


//            updateInfoOwner(connection, 3, 54);
//            updateInfoCar(connection, 6, "yellow", 210);
//            updateInfoRequest(connection, 5, "Прокол колеса");
//
//            getRequests(connection);
//            getOwners(connection); System.out.println();
//            getCars(connection);

//            getOwnersAndCars(connection, 10);
//            getOwnersAndRequest(connection, 10);

//            getSortInfoOwners(connection);





        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                System.out.println("Произошло дублирование данных");
            } else throw new RuntimeException(e);
        }
    }

    static void getOwners (Connection connection) throws SQLException {

        int param0 = -1, param3 = -1;
        String param1 = null, param2 = null;
        Date param4 = null;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM owner;");

        while (rs.next()) {
            param0 = rs.getInt(1);
            param1 = rs.getString(2);
            param2 = rs.getString(3);
            param3 = rs.getInt(4);
            param4 = rs.getDate(5);
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3 + " | " + param4);
        }
    }

    static void getCars (Connection connection) throws SQLException {

        int param0 = -1, param2 = -1, param4 = -1;
        String param1 = null, param5 = null, param6 = null;
        Date param3 = null;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM car;");

        while (rs.next()) {
            param0 = rs.getInt(1);
            param1 = rs.getString(2);
            param2 = rs.getInt(3);
            param3 = rs.getDate(4);
            param4 = rs.getInt(5);
            param5 = rs.getString(6);
            param6 = rs.getString(7);
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3 + " | " + param4 + " | " + param5 + " | " + param6);
        }
    }

    static void getRequests (Connection connection) throws SQLException {

        int param0 = -1, param3 = -1, param4 = -1;
        String param2 = null;
        Date param1 = null;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM requests;");

        while (rs.next()) {
            param0 = rs.getInt(1);
            param1 = rs.getDate(2);
            param2 = rs.getString(3);
            param3 = rs.getInt(4);
            param4 = rs.getInt(5);

            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3 + " | " + param4);
        }
    }



    static void addNewOwner (Connection connection, String name, String surname, int old, Date birthDate) throws SQLException {
        if (name == null || name.isBlank() || surname == null || surname.isBlank() || old < 0 || birthDate == null) return;

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO owner(name, surname, old, birth_date) VALUES (?, ?, ?, ?);");
        statement.setString(1, name);
        statement.setString(2, surname);
        statement.setInt(3, old);
        statement.setDate(4, birthDate);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            System.out.println("Идентификатор владельца " + rs.getInt(1));
            System.out.println();
        }

        System.out.println("INSERTed " + count + " owner");

    }


    static void addNewCar (Connection connection, String brand, String color, int horsepower, Date dateRelease, int ownerId, String numberCar) throws SQLException {
        if (brand == null || brand.isBlank() || color == null || color.isBlank() || horsepower < 0 || dateRelease == null || numberCar == null || numberCar.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO car(color, horsepower, date_release, owner_id, brand, number_car) VALUES (?, ?, ?, ?, ?, ?);");
        statement.setString(5, brand);
        statement.setString(1, color);
        statement.setInt(2, horsepower);
        statement.setDate(3, dateRelease);
        statement.setInt(4, ownerId);
        statement.setString(6, numberCar);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            System.out.println("Идентификатор машины " + rs.getInt(1));
            System.out.println();
        }

        System.out.println("INSERTed " + count + " car");

    }

    static void addNewRequest (Connection connection, Date requestDate, String breakdown, int ownerId, int carId) throws SQLException {
        if (requestDate == null || breakdown == null || breakdown.isBlank() || ownerId < 0 || carId < 0) return;

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO requests(request_date, breakdown, owner_id, car_id) VALUES (?, ?, ?, ?);");
        statement.setDate(1, requestDate);
        statement.setString(2, breakdown);
        statement.setInt(3, ownerId);
        statement.setInt(4, carId);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            System.out.println("Идентификатор запроса " + rs.getInt(1));
            System.out.println();
        }

        System.out.println("INSERTed " + count + " request");

    }

    static void updateInfoOwner (Connection connection, int id, int newOld) throws SQLException {
        if (id < 0 || newOld < 0) return;

        PreparedStatement statement = connection.prepareStatement("UPDATE owner SET old=? WHERE id=?;");
        statement.setInt(1, newOld);
        statement.setInt(2, id);

        int count = statement.executeUpdate();

        System.out.println("UPDATEd " + count + " owners");

    }

    static void updateInfoCar (Connection connection, int id, String newColor, int newHorsepower) throws SQLException {
        if (id < 0 || newHorsepower < 0 || newColor == null || newColor.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement("UPDATE car SET color=?, horsepower=? WHERE id=?;");
        statement.setString(1, newColor);
        statement.setInt(2, newHorsepower);
        statement.setInt(3, id);

        int count = statement.executeUpdate();

        System.out.println("UPDATEd " + count + " cars");

    }

    static void updateInfoRequest (Connection connection, int id, String breakdown) throws SQLException {
        if (id < 0 || breakdown == null || breakdown.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement("UPDATE requests SET breakdown=? WHERE id=?;");
        statement.setString(1, breakdown);
        statement.setInt(2, id);

        int count = statement.executeUpdate();

        System.out.println("UPDATEd " + count + " cars");

    }


    static void deleteCar(Connection connection, int id) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("DELETE FROM car WHERE id= ?");
        statement.setInt(1, id);

        int count = statement.executeUpdate();
        System.out.println("DELETEd " + count + " cars");

    }

    static void deleteOwner(Connection connection, int id) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("DELETE FROM owner WHERE id= ?");
        statement.setInt(1, id);

        int count = statement.executeUpdate();
        System.out.println("DELETEd " + count + " owner");

    }

    static void deleteRequest(Connection connection, int id) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("DELETE FROM requests WHERE id= ?");
        statement.setInt(1, id);

        int count = statement.executeUpdate();
        System.out.println("DELETEd " + count + " request");

    }

    static void getOwnersAndCars (Connection connection, int id) throws SQLException {
        if (id < 0) return;

        PreparedStatement statement = connection.prepareStatement(
                "SELECT owner.name || ' ' || owner.surname AS full_name, car.number_car FROM owner " +
                        "JOIN car " +
                        "ON owner.id=car.owner_id" +
                        " WHERE owner.id = ?");
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString(1) + " | " + rs.getString(2));
        }

    }


    static void getOwnersAndRequest (Connection connection, int id) throws SQLException {
        if (id < 0) return;

        PreparedStatement statement = connection.prepareStatement(
                "SELECT owner.name || ' ' || owner.surname AS full_name, requests.request_date FROM owner " +
                        "LEFT JOIN requests " +
                        "ON owner.id = requests.owner_id" +
                        " WHERE owner.id < ?");
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString(1) + " | " + rs.getString(2));
        }

    }

    static void getSortInfoOwners (Connection connection) throws SQLException {

        int param0 = -1, param3 = -1;
        String param1 = null, param2 = null;
        Date param4 = null;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM owner ORDER BY name;");

        while (rs.next()) {
            param0 = rs.getInt(1);
            param1 = rs.getString(2);
            param2 = rs.getString(3);
            param3 = rs.getInt(4);
            param4 = rs.getDate(5);
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3 + " | " + param4);
        }
    }



}
