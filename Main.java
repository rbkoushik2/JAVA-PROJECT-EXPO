import java.util.*;
import java.io.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<String[]> couriers = new ArrayList<>();
    static ArrayList<String[]> customers = new ArrayList<>();
    static ArrayList<String[]> deliveries = new ArrayList<>();
    static ArrayList<String[]> routes = new ArrayList<>();
    static final String FILE = "courier_data.txt";

    public static void main(String[] args) {
        loadData();
        int choice;

        do {
            System.out.println("\n=== COURIER DELIVERY AND ROUTE TRACKING ===");
            System.out.println("1. Add Courier");
            System.out.println("2. View Couriers");
            System.out.println("3. Search Courier");
            System.out.println("4. Add Customer");
            System.out.println("5. View Customers");
            System.out.println("6. Create Delivery");
            System.out.println("7. Track Delivery");
            System.out.println("8. Update Delivery Status");
            System.out.println("9. View Route");
            System.out.println("10. Add Route");
            System.out.println("11. Exit");
            System.out.print("Enter choice: ");

            choice = readInt();

            switch (choice) {
                case 1: addCourier(); break;
                case 2: view(couriers, "Courier"); break;
                case 3: search(couriers, "Courier"); break;
                case 4: addCustomer(); break;
                case 5: view(customers, "Customer"); break;
                case 6: createDelivery(); break;
                case 7: search(deliveries, "Delivery"); break;
                case 8: updateStatus(); break;
                case 9: viewRoute(); break;
                case 10: addRoute(); break;
                case 11:
                    saveData();
                    System.out.println("Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 11);
    }

    static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }

    static String input(String message) {
        System.out.print(message);
        return sc.nextLine().trim();
    }

    static String nextId(ArrayList<String[]> list, String prefix) {
        int max = 100;
        for (String[] item : list) {
            try {
                int number = Integer.parseInt(
                    item[0].substring(prefix.length())
                );
                if (number > max) max = number;
            } catch (Exception e) {
                // Ignore IDs that don't match the expected format.
            }
        }
        return prefix + (max + 1);
    }

    static void addCourier() {
        String id = nextId(couriers, "C");
        System.out.println("Generated Courier ID: " + id);
        String name = input("Courier name: ");
        String phone = input("Phone: ");
        String vehicle = input("Vehicle: ");

        couriers.add(new String[]{id, name, phone, vehicle});
        saveData();
        System.out.println("Courier added successfully!");
    }

    static void addCustomer() {
        String id = nextId(customers, "U");
        System.out.println("Generated Customer ID: " + id);
        String name = input("Customer name: ");
        String phone = input("Phone: ");
        String address = input("Address: ");

        customers.add(new String[]{id, name, phone, address});
        saveData();
        System.out.println("Customer added successfully!");
    }

    static void createDelivery() {
        if (customers.isEmpty() || couriers.isEmpty()) {
            System.out.println("Add a courier and customer first.");
            return;
        }

        String id = nextId(deliveries, "D");
        System.out.println("Generated Delivery ID: " + id);

        String customerId = input("Enter Customer ID: ");
        if (find(customers, customerId) == null) {
            System.out.println("Customer not found!");
            return;
        }

        String courierId = input("Enter Courier ID: ");
        if (find(couriers, courierId) == null) {
            System.out.println("Courier not found!");
            return;
        }

        String pickup = input("Pickup location: ");
        String destination = input("Destination: ");

        deliveries.add(new String[]{
            id, customerId, courierId, pickup, destination, "Booked"
        });

        saveData();
        System.out.println("Delivery created! Delivery ID: " + id);
    }

    static void addRoute() {
        String id = nextId(routes, "R");
        System.out.println("Generated Route ID: " + id);

        String start = input("Starting location: ");
        String destination = input("Destination: ");
        System.out.print("Distance in km: ");

        double distance;
        try {
            distance = Double.parseDouble(sc.nextLine().trim());
            if (distance < 0) {
                System.out.println("Distance cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid distance.");
            return;
        }

        routes.add(new String[]{
            id, start, destination, String.valueOf(distance)
        });

        saveData();
        System.out.println("Route added successfully! Route ID: " + id);
    }

    static String[] find(ArrayList<String[]> list, String id) {
        for (String[] item : list) {
            if (item[0].equalsIgnoreCase(id)) return item;
        }
        return null;
    }

    static void view(ArrayList<String[]> list, String type) {
        if (list.isEmpty()) {
            System.out.println("No " + type.toLowerCase() + " records found.");
            return;
        }

        for (String[] item : list) {
            System.out.println("--------------------");
            for (int i = 0; i < item.length; i++) {
                System.out.print(item[i] + " | ");
            }
            System.out.println();
        }
    }

    static void search(ArrayList<String[]> list, String type) {
        String id = input("Enter " + type + " ID: ");
        String[] item = find(list, id);

        if (item == null) {
            System.out.println(type + " not found!");
        } else {
            System.out.println(type + " details:");
            for (String value : item) {
                System.out.println(value);
            }
        }
    }

    static void viewRoute() {
        String id = input("Enter Route ID (e.g. R101): ");
        String[] route = find(routes, id);

        if (route == null) {
            System.out.println("Route not found! Add a route using option 10.");
        } else {
            System.out.println("Route ID: " + route[0]);
            System.out.println("Start: " + route[1]);
            System.out.println("Destination: " + route[2]);
            System.out.println("Distance: " + route[3] + " km");
        }
    }

    static void updateStatus() {
        String id = input("Enter Delivery ID: ");
        String[] delivery = find(deliveries, id);

        if (delivery == null) {
            System.out.println("Delivery not found!");
            return;
        }

        System.out.println("1. Booked");
        System.out.println("2. In Transit");
        System.out.println("3. Out for Delivery");
        System.out.println("4. Delivered");
        System.out.print("Choose status: ");

        switch (readInt()) {
            case 1: delivery[5] = "Booked"; break;
            case 2: delivery[5] = "In Transit"; break;
            case 3: delivery[5] = "Out for Delivery"; break;
            case 4: delivery[5] = "Delivered"; break;
            default:
                System.out.println("Invalid status.");
                return;
        }

        saveData();
        System.out.println("Status updated to: " + delivery[5]);
    }

    static void saveData() {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE))) {
            saveRecords(out, "C", couriers);
            saveRecords(out, "U", customers);
            saveRecords(out, "D", deliveries);
            saveRecords(out, "R", routes);
        } catch (IOException e) {
            System.out.println("Warning: Could not save data.");
        }
    }

    static void saveRecords(PrintWriter out, String type,
                            ArrayList<String[]> list) {
        for (String[] item : list) {
            out.print(type);
            for (String value : item) {
                out.print("|" + value.replace("|", "/"));
            }
            out.println();
        }
    }

    static void loadData() {
        File file = new File(FILE);
        if (!file.exists()) return;

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = in.readLine()) != null) {
                String[] data = line.split("\\|", -1);
                if (data.length < 2) continue;

                String[] record = Arrays.copyOfRange(data, 1, data.length);

                switch (data[0]) {
                    case "C":
                        if (record.length == 4) couriers.add(record);
                        break;
                    case "U":
                        if (record.length == 4) customers.add(record);
                        break;
                    case "D":
                        if (record.length == 6) deliveries.add(record);
                        break;
                    case "R":
                        if (record.length == 4) routes.add(record);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not load saved data.");
        }
    }
}