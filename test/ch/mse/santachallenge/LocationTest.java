package ch.mse.santachallenge;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LocationTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDistanceTo() {
		Location a = new Location(60.75473, 3.39283);
		Location b = new Location(-14.54166, -22.09598);
		assertEquals(8643.28251787, a.distanceTo(b), 1E-6);
		assertEquals(8643.28251787, b.distanceTo(a), 1E-6);
		assertEquals(0.0, a.distanceTo(a), 1E-6);
	}

}
