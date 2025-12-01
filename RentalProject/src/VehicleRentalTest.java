import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class VehicleRentalTest {

	@Test
	void testLicensePlate() {
		Vehicle test = new Car("Test Model", "Test Company", 2025, 4); // instantiate test object

		// test valid plates
		assertDoesNotThrow(() -> test.setLicensePlate("AAA100")); 
		assertDoesNotThrow(() -> test.setLicensePlate("ABC567"));
		assertDoesNotThrow(() -> test.setLicensePlate("ZZZ999"));
		
		// test invalid plates
		assertThrows(IllegalArgumentException.class, () -> test.setLicensePlate("")); 
		assertThrows(IllegalArgumentException.class, () -> test.setLicensePlate(null));
		assertThrows(IllegalArgumentException.class, () -> test.setLicensePlate("AAA1000"));
		assertThrows(IllegalArgumentException.class, () -> test.setLicensePlate("ZZZ99"));
	}
	
	@Test
	void testRentAndReturnVehicle() {
		// instantiate test objects
		Vehicle testVehicle = new Car("Test Model", "Test Company", 2025, 4); 
		Customer testCustomer = new Customer(001, "Jane");
		RentalSystem testSystem = RentalSystem.getInstance();
		
		
		// test to see if vehicle is initially available
		assertTrue(testVehicle.getStatus() == Vehicle.VehicleStatus.Available, "Vehicle is not set to available");
		
		// test to see if vehicle is able to be rented
		assertTrue(testSystem.rentVehicle(testVehicle, testCustomer, null, 0), "Vehicle not successfully rented.");
		
		// test to see if vehicle is now considered rented
		assertTrue(testVehicle.getStatus() == Vehicle.VehicleStatus.Rented, "Vehicle is not set to rented");

		// test to see if you can rent the vehicle test (should be false)
		assertFalse(testSystem.rentVehicle(testVehicle, testCustomer, null, 0), "Vehicle is not properly rented");
		
		// test to see if the return worked
		assertTrue(testSystem.returnVehicle(testVehicle, testCustomer, null, 0), "Vehicle failed to be returned."); 
		
		// test to see if vehicle is now available
		assertTrue(testVehicle.getStatus() == Vehicle.VehicleStatus.Available, "Vehicle is not available");

		// test to see if you can return the vehicle again
		assertFalse(testSystem.returnVehicle(testVehicle, testCustomer, null, 0), "Vehicle was returned when available (error).");
	}

}
