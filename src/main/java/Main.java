import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        new Functionality().createDB(); // we create the db
        new Functionality().createMainTable(); // we create the new table

        Scanner sc = new Scanner(System.in);
        String command;

        // command processing
        while (!(command = sc.nextLine()).equals("end")) {
            Split split = new Split();
            String[] array = split.splitString(command);

            if ("addCategory".equals(array[0])) { // addCategory command (done), there is some checks
                new Functionality().addCategory(array);
            } else if ("addItem".equals(array[0])) { // addItem command (done), there is some checks
                new Functionality().addItem(array);
            } else if ("purchase".equals(array[0])){ // purchase command, there is a bug
                new Functionality().purchase(array);
            } else if ("list".equals(array[0])){ // list command (done), there is a check
                new Functionality().list();
            } else if ("clear".equals(array[0])){ // clear command (done), there is a check
                new Functionality().clear();
            } else if ("report".equals(array[0])){ // report command, there is a bug
                new Functionality().report(array);
            } else if ("exit".equals(array[0])) { // exit command (done)
                System.out.println("You are out of the program!");
                new Functionality().deleteDB();
                break;
            }
        }
    }
}
