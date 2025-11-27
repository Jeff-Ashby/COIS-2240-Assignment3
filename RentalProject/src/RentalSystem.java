import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.FileWriter; // import file writing libraries.
import java.io.IOException;
import java.io.File; // import file reading libraries
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RentalSystem {
	private List<Vehicle> vehicles = new ArrayList<>();
	private List<Customer> customers = new ArrayList<>();
	private RentalHistory rentalHistory = new RentalHistory();
	private static final RentalSystem system = new RentalSystem(); // new singleton object

	public void addVehicle(Vehicle vehicle) {
		this.saveVehicle(vehicle); // saves vehicle info to the .txt file
		vehicles.add(vehicle);
	}

	private RentalSystem() { // private constructor to prevent objects being made elsewhere.
		this.loadData();
	}

	public static RentalSystem getInstance() { // public access method.
		return system;
	}

	public void addCustomer(Customer customer) {
		this.saveCustomer(customer); // saves customer info to the .txt file
		customers.add(customer);
	}

	public void saveVehicle(Vehicle v) { // void to write vehicle history to a .txt file.
		try {
			FileWriter writer = new FileWriter("vehicles.txt", true);
			writer.write(v.getInfo());
			writer.write("\n");

			writer.close();
		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		}
	}

	public void saveCustomer(Customer c) { // void to write customer history to a .txt file.
		try {
			FileWriter writer = new FileWriter("customers.txt", true);
			writer.write(c.toString());
			writer.write("\n");
			writer.close();
		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		}
	}

	public void saveRecord(RentalRecord r) { // void to write record history to a .txt file.
		try {
			FileWriter writer = new FileWriter("rental_records.txt", true);
			writer.write(r.toString());
			writer.write("\n");
			writer.close();
		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		}
	}

	public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
		if (vehicle.getStatus() == Vehicle.VehicleStatus.Available) {
			vehicle.setStatus(Vehicle.VehicleStatus.Rented);
			// create record object, then add it and save it to the .txt file
			RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");
			rentalHistory.addRecord(record);
			this.saveRecord(record);
			System.out.println("Vehicle rented to " + customer.getCustomerName());
		} else {
			System.out.println("Vehicle is not available for renting.");
		}
	}

	public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
		if (vehicle.getStatus() == Vehicle.VehicleStatus.Rented) {
			vehicle.setStatus(Vehicle.VehicleStatus.Available);
			// create record object, then add it and save it to the .txt file
			RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
			rentalHistory.addRecord(record);
			this.saveRecord(record);
			System.out.println("Vehicle returned by " + customer.getCustomerName());
		} else {
			System.out.println("Vehicle is not rented.");
		}
	}

	public void displayVehicles(Vehicle.VehicleStatus status) {
		// Display appropriate title based on status
		if (status == null) {
			System.out.println("\n=== All Vehicles ===");
		} else {
			System.out.println("\n=== " + status + " Vehicles ===");
		}

		// Header with proper column widths
		System.out.printf("|%-16s | %-12s | %-12s | %-12s | %-6s | %-18s |%n", " Type", "Plate", "Make", "Model",
				"Year", "Status");
		System.out.println(
				"|--------------------------------------------------------------------------------------------|");

		boolean found = false;
		for (Vehicle vehicle : vehicles) {
			if (status == null || vehicle.getStatus() == status) {
				found = true;
				String vehicleType;
				if (vehicle instanceof Car) {
					vehicleType = "Car";
				} else if (vehicle instanceof Minibus) {
					vehicleType = "Minibus";
				} else if (vehicle instanceof PickupTruck) {
					vehicleType = "Pickup Truck";
				} else {
					vehicleType = "Unknown";
				}
				System.out.printf("| %-15s | %-12s | %-12s | %-12s | %-6d | %-18s |%n", vehicleType,
						vehicle.getLicensePlate(), vehicle.getMake(), vehicle.getModel(), vehicle.getYear(),
						vehicle.getStatus().toString());
			}
		}
		if (!found) {
			if (status == null) {
				System.out.println("  No Vehicles found.");
			} else {
				System.out.println("  No vehicles with Status: " + status);
			}
		}
		System.out.println();
	}

	public void displayAllCustomers() {
		for (Customer c : customers) {
			System.out.println("  " + c.toString());
		}
	}

	public void displayRentalHistory() {
		if (rentalHistory.getRentalHistory().isEmpty()) {
			System.out.println("  No rental history found.");
		} else {
			// Header with proper column widths
			System.out.printf("|%-10s | %-12s | %-20s | %-12s | %-12s |%n", " Type", "Plate", "Customer", "Date",
					"Amount");
			System.out.println("|-------------------------------------------------------------------------------|");

			for (RentalRecord record : rentalHistory.getRentalHistory()) {
				System.out.printf("| %-9s | %-12s | %-20s | %-12s | $%-11.2f |%n", record.getRecordType(),
						record.getVehicle().getLicensePlate(), record.getCustomer().getCustomerName(),
						record.getRecordDate().toString(), record.getTotalAmount());
			}
			System.out.println();
		}
	}

	private void loadData() {
		try (Scanner scanner = new Scanner(new File("vehicles.txt"))) { // this is the portion that loads the vehicles.
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				String[] data = line.split("\\|"); // split and trim the data
				for (int i = 0; i < data.length; i++) {
					data[i] = data[i].trim();
				}

				if (data[7].startsWith("Seats: ")) { // is a car
					Vehicle v = null;
					v = new Car(data[2], data[3], Integer.valueOf(data[4]), Integer.valueOf(data[7].split(": ")[1]));
					v.setLicensePlate(data[1]);
					vehicles.add(v);
				} else if (data[7].startsWith("Accessible: ")) { // is a bus
					Vehicle v = null;
					boolean access;
					if (data[7].contains("Yes")) {
						access = true;
					} else {
						access = false;
					}
					v = new Minibus(data[2], data[3], Integer.valueOf(data[4]), access);
					v.setLicensePlate(data[1]);
					vehicles.add(v);
				} else { // is a truck
					Vehicle v = null;
					boolean trailer;
					if (data[8].contains("Yes")) {
						trailer = true;
					} else {
						trailer = false;
					}
					v = new PickupTruck(data[2], data[3], Integer.valueOf(data[4]),
							Double.valueOf(data[7].split(": ")[1]), trailer);
					v.setLicensePlate(data[1]);
					vehicles.add(v);
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try (Scanner scanner = new Scanner(new File("customers.txt"))) { // this is the portion that loads the customers.
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				String[] data = line.split("\\|"); // split and trim the data
				for (int i = 0; i < data.length; i++) {
					data[i] = data[i].trim();
				}
				// finally add the customer to the list
				Customer c = new Customer(Integer.valueOf(data[0].split(": ")[1]), data[1].split(": ")[1]);
				customers.add(c);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try (Scanner scanner = new Scanner(new File("rental_records.txt"))) { // this is the portion that loads the records.
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				String[] data = line.split("\\|"); // split and trim the data
				for (int i = 0; i < data.length; i++) {
					data[i] = data[i].trim();
				}
				Customer cust = null; // find the customer being dealt with in this record by name
				for (Customer c : customers) {
					if (c.getCustomerName().equals(data[2].split(": ")[1])) {
						cust = c;
					}
				}
				Vehicle veh = this.findVehicleByPlate(data[1].split(": ")[1]); // find the vehicle being dealt with in this record by license plate.
				
				
				RentalRecord r = new RentalRecord(veh, cust, LocalDate.parse(data[3].split(": ")[1]), Double.parseDouble(data[4].split(": ")[1].replace("$", "")), data[0]);
				rentalHistory.addRecord(r);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public Vehicle findVehicleByPlate(String plate) {
		for (Vehicle v : vehicles) {
			if (v.getLicensePlate().equalsIgnoreCase(plate)) {
				return v;
			}
		}
		return null;
	}

	public Customer findCustomerById(int id) {
		for (Customer c : customers)
			if (c.getCustomerId() == id)
				return c;
		return null;
	}
}