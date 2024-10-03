import java.sql.*;



public class JDBCRunner {

    private static final String PROTOCOL = "jdbc:postgresql://";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL_LOCALE_NAME = "localhost/";

    private static final String DATABASE_NAME = "CarService";

    public static final String DATABASE_URL = PROTOCOL + URL_LOCALE_NAME + "NewCarService";
    public static final String USER_NAME = "postgres";
    public static final String DATABASE_PASS = "postgres";

    public static void main(String[] args) {

        checkDriver();
        checkDB();
        System.out.println(DATABASE_URL);


        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS)) {

            addNewCustomer(connection, "Михаил", "Пирогов", 28, new Date(96, 3, 3));

            addNewCar(connection, "Nissan", "Gray", 150, new Date(118, 8, 19), 15, "B276KU178");

            addNewApplication(connection, new Timestamp(124, 10, 1, 16, 28, 17, 43), "Замена масла в коробке", 15, 6);

            updateInfoCustomer(connection, 11, 46);
            updateInfoCar(connection, "B888PM888", "green", 900);
            updateInfoApplication(connection, 2, "Разбито лобовое стекло");


            getSortInfoCustomers(connection);
            System.out.println();
            getCars(connection);
            System.out.println();
            getApplications(connection);

            getCarsAndApplications(connection, "B321YT190");
            getCustomersAndCars(connection, 7);
            getCustomersAndApplications(connection, 10);

            deleteApplication(connection, 3);



        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                System.out.println("Произошло дублирование данных");
            } else throw new RuntimeException(e);
        }
    }

    public static void checkDriver () {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Нет JDBC-драйвера! Подключите JDBC-драйвер к проекту согласно инструкции.");
            throw new RuntimeException(e);
        }
    }

    public static void checkDB () {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS);
        } catch (SQLException e) {
            System.out.println("Нет базы данных! Проверьте имя базы, путь к базе или разверните локально резервную копию согласно инструкции");
            throw new RuntimeException(e);
        }
    }

    static void getCustomers(Connection connection) throws SQLException {

        int param0 = -1, param3 = -1;
        String param1 = null, param2 = null;
        Date param4 = null;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM customers;");

        while (rs.next()) {
            param0 = rs.getInt("id");
            param1 = rs.getString("name");
            param2 = rs.getString("surname");
            param3 = rs.getInt("age");
            param4 = rs.getDate("birth_date");
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3 + " | " + param4);
        }
    }

    static void getCars (Connection connection) throws SQLException {

        int param0 = -1,param3 = -1, param6 = -1;
        String param1 = null, param2 = null, param5 = null;
        Date param4 = null;


        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM cars;");

        while (rs.next()) {
            param0 = rs.getInt("id");
            param1 = rs.getString("manufacturer");
            param2 = rs.getString("color");
            param3 = rs.getInt("horsepower");
            param4 = rs.getDate("date_production");
            param5 = rs.getString("number_car");
            param6 = rs.getInt("customer_id");
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3 + " | " + param4 + " | " + param5 + " | " + param6);
        }
    }

    static void getApplications (Connection connection) throws SQLException {

        int param0 = -1, param3 = -1, param4 = -1;
        String param2 = null;
        Timestamp param1 = null;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM applications;");

        while (rs.next()) {
            param0 = rs.getInt("id");
            param1 = rs.getTimestamp("date_applications");
            param2 = rs.getString("breakdown");
            param3 = rs.getInt("customer_id");
            param4 = rs.getInt("car_id");

            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3 + " | " + param4);
        }
    }



    static void addNewCustomer(Connection connection, String name, String surname, int age, Date birthDate) throws SQLException {
        if (name == null || name.isBlank() || surname == null || surname.isBlank() || age < 0 || birthDate == null) return;

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO customers(name, surname, age, birth_date) VALUES (?, ?, ?, ?) RETURNING id;", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, name);
        statement.setString(2, surname);
        statement.setInt(3, age);
        statement.setDate(4, birthDate);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            System.out.println("Идентификатор клиента " + rs.getInt("id"));
            System.out.println();
        }

        System.out.println("INSERTed " + count + " customer");

    }


    static void addNewCar (Connection connection, String manufacturer, String color, int horsepower, Date date_production, int customerId, String numberCar) throws SQLException {
        if (manufacturer == null || manufacturer.isBlank() || color == null || color.isBlank() || horsepower < 0 || date_production == null || numberCar == null || numberCar.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO cars(manufacturer, color, horsepower, date_production, number_car, customer_id) VALUES (?, ?, ?, ?, ?, ?) RETURNING id;", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, manufacturer);
        statement.setString(2, color);
        statement.setInt(3, horsepower);
        statement.setDate(4, date_production);
        statement.setInt(6, customerId);
        statement.setString(5, numberCar);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            System.out.println("Идентификатор машины " + rs.getInt(1));
            System.out.println();
        }

        System.out.println("INSERTed " + count + " car");

    }

    static void addNewApplication(Connection connection, Timestamp dateApplication, String breakdown, int customerId, int carId) throws SQLException {
        if (dateApplication == null || breakdown == null || breakdown.isBlank() || customerId < 0 || carId < 0) return;

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO applications(date_applications, breakdown, customer_id, car_id) VALUES (?, ?, ?, ?);");
        statement.setTimestamp(1, dateApplication);
        statement.setString(2, breakdown);
        statement.setInt(3, customerId);
        statement.setInt(4, carId);

        int count = statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            System.out.println("Идентификатор обращения " + rs.getInt(1));
            System.out.println();
        }

        System.out.println("INSERTed " + count + " applications");

    }

    static void updateInfoCustomer(Connection connection, int id, int newAge) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("UPDATE customers SET age=? WHERE id=?;");
        statement.setInt(1, newAge);
        statement.setInt(2, id);

        int count = statement.executeUpdate();

        System.out.println("UPDATEd " + count + " customer");

    }

    static void updateInfoCar (Connection connection, String numberCar, String newColor, int newHorsepower) throws SQLException {
        if (numberCar == null || numberCar.isBlank() || newHorsepower < 0 || newColor == null || newColor.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement("UPDATE cars SET color=?, horsepower=? WHERE number_car=?;");
        statement.setString(1, newColor);
        statement.setInt(2, newHorsepower);
        statement.setString(3, numberCar);

        int count = statement.executeUpdate();

        System.out.println("UPDATEd " + count + " cars");

    }

    static void updateInfoApplication(Connection connection, int id, String breakdown) throws SQLException {
        if (id < 0 || breakdown == null || breakdown.isBlank()) return;

        PreparedStatement statement = connection.prepareStatement("UPDATE applications SET breakdown=? WHERE id=?;");
        statement.setString(1, breakdown);
        statement.setInt(2, id);

        int count = statement.executeUpdate();

        System.out.println("UPDATEd " + count + " cars");

    }


    static void deleteCar(Connection connection, String numberCar) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("DELETE FROM cars WHERE number_car= ?");
        statement.setString(1, numberCar);

        int count = statement.executeUpdate();
        System.out.println("DELETEd " + count + " cars");

    }

    static void deleteCustomer(Connection connection, int id) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("DELETE FROM customers WHERE id= ?");
        statement.setInt(1, id);

        int count = statement.executeUpdate();
        System.out.println("DELETEd " + count + " customer");

    }

    static void deleteApplication(Connection connection, int id) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("DELETE FROM applications WHERE id= ?");
        statement.setInt(1, id);

        int count = statement.executeUpdate();
        System.out.println("DELETEd " + count + " application");

    }

    static void getCustomersAndCars(Connection connection, int id) throws SQLException {
        if (id < 0) return;

        PreparedStatement statement = connection.prepareStatement(
                "SELECT customers.name || ' ' || customers.surname AS full_name, cars.number_car FROM customers " +
                        "JOIN cars " +
                        "ON customers.id=cars.customer_id" +
                        " WHERE customers.id = ?");
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString(1) + " | " + rs.getString(2));
        }

    }


    static void getCustomersAndApplications(Connection connection, int id) throws SQLException {
        if (id < 0) return;

        PreparedStatement statement = connection.prepareStatement(
                "SELECT customers.name || ' ' || customers.surname AS full_name, applications.date_applications FROM customers " +
                        "LEFT JOIN applications " +
                        "ON customers.id = applications.customer_id" +
                        " WHERE customers.id < ?");
        statement.setInt(1, id);

        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString(1) + " | " + rs.getString(2));
        }

    }

    static void getCarsAndApplications(Connection connection, String numberCar) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(
                "SELECT applications.breakdown FROM applications " +
                        "JOIN cars " +
                        "ON applications.car_id = cars.id" +
                        " WHERE cars.number_car = ?");
        statement.setString(1, numberCar);

        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString(1) );
        }

    }


    static void getSortInfoCustomers(Connection connection) throws SQLException {

        int param0 = -1, param3 = -1;
        String param1 = null, param2 = null;
        Date param4 = null;

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM customers ORDER BY name;");

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
