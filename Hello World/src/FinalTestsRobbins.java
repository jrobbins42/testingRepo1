/*Class  FinalTestsRobbins
 * this class is a test class 
 * @author James Robbins
 * @version 1.0
 * @since 1.0
 
 *	I used chatgpt to help me write these tests 
 * 
*/

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
public class FinalTestsRobbins {
	
	    private GroceryOptimizer optimizer;

	    @BeforeEach
	    void setUp() {
	        optimizer = new GroceryOptimizer();
	        
	        // Setup Store A: Total for [SKU1, SKU2] = 15.50
	        Map<String, Double> storeA = new HashMap<>();
	        storeA.put("SKU1", 10.50);
	        storeA.put("SKU2", 5.00);
	        
	        // Setup Store B: Total for [SKU1, SKU2] = 12.00 (Cheaper)
	        Map<String, Double> storeB = new HashMap<>();
	        storeB.put("SKU1", 8.00);
	        storeB.put("SKU2", 4.00);
	        
	        // Setup Store C: Missing SKU2
	        Map<String, Double> storeC = new HashMap<>();
	        storeC.put("SKU1", 7.00);

	        optimizer.updatePrices("StoreA", storeA);
	        optimizer.updatePrices("StoreB", storeB);
	        optimizer.updatePrices("StoreC", storeC);
	    }

	    @Test
	    void testFindBestTotalTrip_Sorting() {
	        List<String> shoppingList = Arrays.asList("SKU1", "SKU2");
	        List<StoreTrip> results = optimizer.findBestTotalTrip(shoppingList);

	        // Assertions
	        assertEquals(2, results.size(), "Should only return stores that have all items");
	        assertEquals("StoreB", results.get(0).storeName, "Cheapest store should be first");
	        assertEquals(12.00, results.get(0).totalCost);
	        assertEquals("StoreA", results.get(1).storeName, "More expensive store should be second");
	        assertEquals(15.50, results.get(1).totalCost);
	    }

	    @Test
	    void testFindBestTotalTrip_MissingItems() {
	        // Store C only has SKU1. If we search for both, Store C should be excluded.
	        List<String> shoppingList = Arrays.asList("SKU1", "SKU2");
	        List<StoreTrip> results = optimizer.findBestTotalTrip(shoppingList);

	        boolean containsStoreC = results.stream().anyMatch(t -> t.storeName.equals("StoreC"));
	        assertFalse(containsStoreC, "StoreC should not be in the list because it lacks SKU2");
	    }

	    @Test
	    void testEmptyShoppingList() {
	        List<String> shoppingList = new ArrayList<>();
	        List<StoreTrip> results = optimizer.findBestTotalTrip(shoppingList);

	        // If list is empty, all stores technically "have all items" (total 0.0)
	        assertFalse(results.isEmpty());
	        assertEquals(0.0, results.get(0).totalCost);
	    }

	    @Test
	    void testNoStoresFound() {
	        List<String> shoppingList = Arrays.asList("NON_EXISTENT_SKU");
	        List<StoreTrip> results = optimizer.findBestTotalTrip(shoppingList);

	        assertTrue(results.isEmpty(), "Result list should be empty if no store stocks the item");
	    }
	    @Test
	    void testGetStoresData() {
	        assertNotNull(optimizer.getStoresData());
	        assertTrue(optimizer.getStoresData().containsKey("StoreA"));
	    }
	}


      