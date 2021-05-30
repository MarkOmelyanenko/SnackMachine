import java.sql.*;

public class Functionality {

    private final String URL = "jdbc:mysql://localhost:3306/snackmachine";
    private final String USERNAME = "root";
    private  final String PASSWORD = "Mark15032005";

    private Connection connection = null;
    private Statement statement = null;
    private String sql;
    private ResultSet rs;


    // we connect to the database
    public Functionality() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.print("");
        }
    }

    // we create the main db
    public void createDB() {
        try {
            final String URlcreateDB = "jdbc:mysql://localhost:3306/";
            Connection connectionCreateDB = DriverManager.getConnection(URlcreateDB, USERNAME, PASSWORD);
            Statement statementCreateDB = connectionCreateDB.createStatement();
            sql = "CREATE DATABASE snackmachine"; // everything works
            statementCreateDB.executeUpdate(sql);

            statementCreateDB.close();
            connectionCreateDB.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // we create the main table
    public void createMainTable() {
        try {
            Statement statementCreateMainTable = connection.createStatement();
            sql = "CREATE TABLE `snackmachine`.`snacks` (" + // everything works
                    "  `id` INT NOT NULL AUTO_INCREMENT," +
                    "  `category` VARCHAR(50) NOT NULL," +
                    "  `price` DOUBLE(10,2) NOT NULL," +
                    "  `amount` INT NOT NULL DEFAULT '0'," +
                    "  `is_purchased` VARCHAR(10) NOT NULL DEFAULT 'no'," +
                    "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE," +
                    "  PRIMARY KEY (`id`))" +
                    "ENGINE = InnoDB;";
            statementCreateMainTable.executeUpdate(sql);

            statementCreateMainTable.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // we delete our db after "exit"
    public void deleteDB() {
        try {
            Statement statementDeleteDB = connection.createStatement();
            sql = "drop database snackmachine"; // everything works
            statementDeleteDB.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // we add a snack to the database
    public void addCategory(String[] array) {
        try {
            statement = connection.createStatement();

            // data check
            if (array.length == 4) {
                String categoryFromUser1 = array[1].replace("\"", "");
                double priceFromUser = Double.parseDouble(array[2]);
                int amountFromUser = Integer.parseInt(array[3]);

                sql = "select * from snacks";
                rs = statement.executeQuery(sql);

                // category check
                while (rs.next()) {
                    String category = rs.getString("category");

                    if (category.equals(categoryFromUser1)) {
                        System.out.println("This category already exists!");
                        return;
                    }
                }

                String formattedPrice = String.format("%.2f", priceFromUser).replace(",", ".");

                System.out.println(categoryFromUser1 + " " + formattedPrice + " " + amountFromUser);

                // insert a new category
                sql = "insert into snacks(category, price, amount) values ('" + categoryFromUser1 + "', "
                + priceFromUser + ", " + amountFromUser + ")";
                statement.executeUpdate(sql);

            } else if (array.length == 3) {
                String categoryFromUser2 = array[1].replace("\"", "");

                sql = "select * from snacks";
                rs = statement.executeQuery(sql);

                // category check
                while (rs.next()) {
                    String category = rs.getString("category");

                    if (category.equals(categoryFromUser2)) {
                        System.out.println("This category already exists!");
                        return;
                    }
                }

                // insert a new category
                double priceFromUser = Double.parseDouble(array[2]);
                String formattedPrice = String.format("%.2f", priceFromUser).replace(",", ".");

                System.out.println(categoryFromUser2 + " " + formattedPrice + " 0");

                sql = "insert into snacks(category, price) values ('" + categoryFromUser2 + "', "
                        + priceFromUser + ")";
                statement.executeUpdate(sql);
            } else if (array.length <= 2) {
                System.out.println("You have entered insufficient data!");
            }

            connection.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // we add a certain number of snacks
    public void addItem(String[] array) {
        try {
            statement = connection.createStatement();

            // data check
            if (array.length == 3) {
                String categoryFromUser = array[1].replace("\"", "");
                int amountFromUser = Integer.parseInt(array[2]);

                sql = "select * from snacks";
                rs = statement.executeQuery(sql);

                boolean searchedCategory = false;

                // category check
                while (rs.next()) {
                    String category = rs.getString("category");

                    if (categoryFromUser.equals(category)) {
                        searchedCategory = true;
                        break;
                    }
                }

                if (searchedCategory) {
                    Statement statementUpdate = connection.createStatement();
                    sql = "select * from snacks where category='" + categoryFromUser + "'";
                    ResultSet rsUpdate = statementUpdate.executeQuery(sql);
                    int amountRes = 0;

                    // we add an additional amount
                    while (rsUpdate.next()) {
                        int amountFromTable = rsUpdate.getInt("amount");
                        double priceFromTable = rsUpdate.getDouble("price");
                        String formattedPrice = String.format("%.2f", priceFromTable).replace(",", ".");
                        amountRes = amountFromTable + amountFromUser;

                        System.out.println(categoryFromUser + " " + formattedPrice + " " + amountRes);
                    }

                    // we update the amount
                    sql = "update snacks " +
                            "set amount=" + amountRes + " where category='" + categoryFromUser + "'";
                    statementUpdate.executeUpdate(sql);

                    rsUpdate.close();
                    statementUpdate.close();
                } else {
                    System.out.println("There is no such category in the list :(");
                }
            } else if (array.length <= 2) {
                System.out.println("You have entered insufficient data!");
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // we buy a snack
    public void purchase(String[] array) {
        // --- BUG ---
        try {
            statement = connection.createStatement();

            // data check
            if (array.length == 3) {
                String categoryFromUser = array[1].replace("\"", "");
                String dateFromUser = array[2];

                sql = "select * from snacks";
                rs = statement.executeQuery(sql);

                boolean searchedCategory = false;
                int amount = 0;

                // category check
                while (rs.next()) {
                    String category = rs.getString("category");
                    amount = rs.getInt("amount");

                    if (categoryFromUser.equals(category)) {
                        searchedCategory = true;
                        break;
                    }
                }

                if (searchedCategory && amount != 0) {
                    Statement statementUpdate = connection.createStatement();
                    sql = "select * from snacks where category='" + categoryFromUser + "'";
                    ResultSet rsUpdate = statementUpdate.executeQuery(sql);

                    int amountFromTable;
                    int amountRes = 0;
                    int amountPurchasesRes = 0;
                    double priceRes = 0;
                    double priceFromTable = 0;
                    String formattedPrice = null;
                    String isPurchased = "yes";

                    // we subtract 1 from the amount
                    while (rsUpdate.next()) {
                        amountFromTable = rsUpdate.getInt("amount");
                        priceFromTable = rsUpdate.getDouble("price");
                        formattedPrice = String.format("%.2f", priceFromTable).replace(",", ".");
                        amountRes = amountFromTable - 1;
                    }

                    System.out.println(dateFromUser + "\n" + categoryFromUser + " " + formattedPrice);

                    // we update the amount in snacks
                    Statement statementUpdateSnacks = connection.createStatement();
                    sql = "update snacks " + "set amount=" + amountRes + ", is_purchased='" + isPurchased +
                            "' where category='" + categoryFromUser + "'";
                    statementUpdateSnacks.executeUpdate(sql);

                    DatabaseMetaData metaData = connection.getMetaData();
                    ResultSet rsCheckTable;
                    rsCheckTable = metaData.getTables(null, null, dateFromUser, null);

                    // table check
                    if (rsCheckTable.next()) { // the table is found?
                        Statement statementGetCategory = connection.createStatement();
                        sql = "select * from snackmachine.`" + dateFromUser + "`";
                        ResultSet rsGetCategory = statementGetCategory.executeQuery(sql);

                        String category = null;

                        while (rsGetCategory.next()) {
                            category = rsGetCategory.getString("category");
                            int amountPurchases = rsGetCategory.getInt("amount_purchases");
                            amountPurchasesRes = amountPurchases + 1;
                            priceRes = amountPurchasesRes * priceFromTable;

                            // category check
                            if (category.equals(categoryFromUser)) {
                                Statement statementUpdateData = connection.createStatement();

                                // we update the table
                                sql = "update snackmachine.`" + dateFromUser + "` " +
                                        "set full_price=" + priceRes + ", " + "amount_purchases=" + amountPurchasesRes +
                                        " where category='" + categoryFromUser + "'";
                                statementUpdateData.executeUpdate(sql);

                                statementUpdateData.close();

                            } else if (!category.equals(categoryFromUser)) {

                                double fullPriceZero = priceFromTable;
                                int amountPurchasesZero = 1;

                                // we insert new data into the table
                                Statement statementInsertData = connection.createStatement();
                                sql = "insert into snackmachine.`" + dateFromUser + "` (`category`, `full_price`, " +
                                        "`amount_purchases`) " + "VALUES ('" + categoryFromUser + "', '" +
                                        fullPriceZero + "', '" + amountPurchasesZero + "')";
                                statementInsertData.executeUpdate(sql);

                                statementInsertData.close();
                            }

                            statementGetCategory.close();
                            rsGetCategory.close();
                        }


                    } else if (!rsCheckTable.next()){ // the table is not found
                        // we create the table
                        Statement statementCreateTableDate = connection.createStatement();
                        sql = "CREATE TABLE `snackmachine`.`" + dateFromUser + "` (" +
                                "  `id` INT NOT NULL AUTO_INCREMENT," +
                                "  `category` VARCHAR(45) NOT NULL DEFAULT 'category'," +
                                "  `full_price` DOUBLE(10,2) NOT NULL DEFAULT '0.00'," +
                                "  `amount_purchases` INT NOT NULL DEFAULT '0'," +
                                "  PRIMARY KEY (`id`)," +
                                "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);";
                        statementCreateTableDate.executeUpdate(sql);

                        int amountOne = 1;
                        Statement statementUpdateTableDate = connection.createStatement();
                        sql = "insert into snackmachine.`" + dateFromUser + "` (`category`, `full_price`, `amount_purchases`) " +
                                "VALUES ('" + categoryFromUser + "', '" + priceFromTable + "', '" + amountOne + "')";
                        statementUpdateTableDate.executeUpdate(sql);

                        statementUpdateTableDate.close();
                    }

                    rsCheckTable.close();

                    // some checks
                } else if (!searchedCategory) {
                    System.out.println("We do not have such a category in the list :(");
                } else if (amount == 0) {
                    System.out.println("This snack is over!");
                }
            } else if (array.length <= 2) {
                System.out.println("You have entered insufficient data!");
                return;
            }

            statement.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("");
        }
    }

    // we display a list of snacks
    public void list() {
        PreparedStatement statementList = null;
        ResultSet rsList = null;

        // we check if the table is empty
        try {
            sql = "select * from snacks";
            statementList = connection.prepareStatement(sql);
            rsList = statementList.executeQuery();
            int count = 0;

            while(rsList.next()){
                count++;
            }

            if(count == 0){
                System.out.println("There are no categories in the list!");
            }

            rsList.close();
            statementList.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            statement = connection.createStatement();
            sql = "select * from snacks order by amount desc";
            rs = statement.executeQuery(sql);

            // we display all categories
            while (rs.next()) {
                String categoryFromTable = rs.getString("category");
                double priceFromTable = rs.getDouble("price");
                int amountFromTable = rs.getInt("amount");

                String formattedPrice = String.format("%.2f", priceFromTable).replace(",", ".");
                String result = categoryFromTable + " " + formattedPrice + " " + amountFromTable;

                System.out.println(result);
            }

            connection.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // we remove snacks where amount = 0
    public void clear() {
        try {
            statement = connection.createStatement();
            sql = "select * from snacks where amount=0";
            rs = statement.executeQuery(sql);

            boolean isZero = false;

            // we display all categories the amount of which is equal to 0
            while (rs.next()) {
                String categoryFromTable = rs.getString("category");
                double priceFromTable = rs.getDouble("price");

                String formattedPrice = String.format("%.2f", priceFromTable).replace(",", ".");
                String result = categoryFromTable + " " + formattedPrice;
                isZero = true;

                System.out.println(result);
            }

            // isZero check
            if (isZero) {
                sql = "delete from snacks where amount=0";
                statement.executeUpdate(sql);
            } else {
                System.out.println("Nothing to clear!");
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // we receive a report for a specific day or for a specific month
    public void report(String[] array) {
        try {
            // data check
            if (array.length == 2) {
                String dateFromUser = array[1];
                String tableName = null;


                statement = connection.createStatement();
                sql = "SELECT TABLE_NAME " +
                        "FROM INFORMATION_SCHEMA.TABLES " +
                        "WHERE TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA='snackmachine'";
                ResultSet rsTableName = statement.executeQuery(sql);

                // table check
                try {
                    while (rsTableName.next()) {

                        tableName = rsTableName.getString(1);

                        // --- BUG ---
                        if (rsTableName.next() && !tableName.equals("snacks")) {
                            if (tableName.equals(dateFromUser)) { // date like 2021-05-31
                                double fullPriceRes = 0;

                                Statement statementGetData = connection.createStatement();
                                sql = "select * from snackmachine.`" + dateFromUser + "`";
                                ResultSet rsGetData = statementGetData.executeQuery(sql);

                                while (rsGetData.next()) {
                                    String category = rsGetData.getString("category");
                                    double fullPrice = rsGetData.getDouble("full_price");
                                    int amountPurchases = rsGetData.getInt("amount_purchases");

                                    fullPriceRes = fullPriceRes + fullPrice;
                                    String formattedPrice = String.format("%.2f", fullPrice).replace(",", ".");

                                    System.out.println(category + " " + formattedPrice + " " + amountPurchases);
                                }

                                System.out.println(">Total " + fullPriceRes);

                                statementGetData.close();
                                rsGetData.close();

                                break;

                            } else if (tableName.contains(dateFromUser)) { // date like 2021-05

                                double fullPriceRes = 0;

                                Statement statementGetData = connection.createStatement();
                                sql = "select * from snackmachine.`" + dateFromUser + "`";
                                ResultSet rsGetData = statementGetData.executeQuery(sql);

                                while (rsGetData.next()) {
                                    String category = rsGetData.getString("category");
                                    double fullPrice = rsGetData.getDouble("full_price");
                                    int amountPurchases = rsGetData.getInt("amount_purchases");

                                    fullPriceRes = fullPriceRes +fullPrice;
                                    String formattedPrice = String.format("%.2f", fullPrice).replace(",", ".");

                                    System.out.println(category + " " + formattedPrice + " " + amountPurchases);
                                }

                                System.out.println(">Total " + fullPriceRes);

                                statementGetData.close();
                                rsGetData.close();

                                break;
                            }
                        }
                    }

                    // some checks
                    if (!rsTableName.next() && tableName.equals("snacks")) {
                        System.out.println("You did not buy anything!");
                    } else if (!tableName.equals(dateFromUser)) {
                        System.out.println("You did not buy anything during this period!");
                    } else if (!tableName.contains(dateFromUser)) {
                        System.out.println("You did not buy anything during this period!");
                    }
                } catch (NullPointerException e) {
                    System.out.println("");
                }

            } else if (array.length == 1) {
                System.out.println("You have entered insufficient data!");
            }

            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
