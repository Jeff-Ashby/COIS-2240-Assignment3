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

}
