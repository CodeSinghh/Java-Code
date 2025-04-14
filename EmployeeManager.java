import java.io.*;
import java.util.*;

class Employee {
    int id;
    String name;
    String department;
    double salary;

    Employee(int id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public String toString() {
        return String.format("ID: %d | Name: %s | Dept: %s | Salary: %.2f", id, name, department, salary);
    }
}

public class EmployeeManager {
    private static final String FILE_PATH = "employees.txt";
    private List<Employee> employees = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        EmployeeManager manager = new EmployeeManager();
        manager.loadEmployees();
        manager.run();
    }

    private void run() throws IOException {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n1. View All\n2. Add Employee\n3. Search by Department\n4. Save & Exit");
            choice = sc.nextInt(); sc.nextLine();
            switch (choice) {
                case 1: viewAll(); break;
                case 2: addEmployee(sc); break;
                case 3: searchByDept(sc); break;
                case 4: saveEmployees(); break;
                default: System.out.println("Invalid choice");
            }
        } while (choice != 4);
    }

    private void viewAll() {
        if (employees.isEmpty()) {
            System.out.println("No employees to display.");
            return;
        }
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    private void addEmployee(Scanner sc) {
        System.out.print("Enter ID: ");
        int id = sc.nextInt(); sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Department: ");
        String dept = sc.nextLine();
        System.out.print("Enter Salary: ");
        double salary = sc.nextDouble(); sc.nextLine();

        employees.add(new Employee(id, name, dept, salary));
        System.out.println("Employee added!");
    }

    private void searchByDept(Scanner sc) {
        System.out.print("Enter Department to search: ");
        String dept = sc.nextLine();
        employees.stream()
                .filter(e -> e.department.equalsIgnoreCase(dept))
                .forEach(System.out::println);
    }

    private void saveEmployees() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Employee e : employees) {
                writer.write(String.format("%d,%s,%s,%.2f\n", e.id, e.name, e.department, e.salary));
            }
        }
        System.out.println("Employees saved successfully.");
    }

    private void loadEmployees() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                employees.add(new Employee(
                        Integer.parseInt(parts[0]), parts[1], parts[2], Double.parseDouble(parts[3])));
            }
        } catch (IOException e) {
            System.out.println("Error loading employees.");
        }
    }
}
