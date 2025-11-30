public abstract class Vehicle {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus { Available, Held, Rented, UnderMaintenance, OutOfService }

    public Vehicle(String make, String model, int year) {
    	this.make = capitalize(make);
    	this.model = capitalize(model);
    	
        this.year = year;
        this.status = VehicleStatus.Available;
        this.licensePlate = null;
    }
    
    private boolean isValidPlate(String plate) { // private method to verify if a plate is not null, empty, and follows correct syntax
        if (plate == null || plate.equals("")) {
            return false;
        }
        return plate.matches("[A-Z]{3}\\d{3}");
    }

	private String capitalize(String input) { // new method to reduce redundancy
		if (input == null || input.isEmpty())
    		return null;
    	else
    		return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
	}

    public Vehicle() {
        this(null, null, 0);
    }

    public void setLicensePlate(String plate) { // added verification with private method
    	if (this.isValidPlate(plate)) {
    		this.licensePlate = plate == null ? null : plate.toUpperCase();
    	} else {
    		throw new IllegalArgumentException("Error, plate does not follow correct pattern. (ABC123)");
    	}
    }

    public void setStatus(VehicleStatus status) {
    	this.status = status;
    }

    public String getLicensePlate() { return licensePlate; }

    public String getMake() { return make; }

    public String getModel() { return model;}

    public int getYear() { return year; }

    public VehicleStatus getStatus() { return status; }

    public String getInfo() {
        return "| " + licensePlate + " | " + make + " | " + model + " | " + year + " | " + status + " |";
    }

}
