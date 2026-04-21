import java.util.*;

public class GroceryOptimizer {
    // 1. HASH MAP: Store Name (String) -> {SKU (String): Price (Double)}
    private Map<String, Map<String, Double>> storesData;

    public GroceryOptimizer() {
        this.storesData = new HashMap<>();
    }

    /** Adds or updates a store's inventory prices. */
    public void updatePrices(String storeName, Map<String, Double> pricing) {
        storesData.put(storeName, pricing);
    }

    /** Finds the best store for a full 'grocery bag' trip. */  
    public List<StoreTrip> findBestTotalTrip(List<String> shoppingList) {
        // 2. DYNAMIC ARRAY: To hold valid store trip objects for sorting
        List<StoreTrip> tripCosts = new ArrayList<>();

        for (Map.Entry<String, Map<String, Double>> entry : storesData.entrySet()) {
            String storeName = entry.getKey();
            Map<String, Double> inventory = entry.getValue();
            
            double totalPrice = 0;
            boolean hasAllItems = true;

            for (String sku : shoppingList) {
                if (inventory.containsKey(sku)) {
                    totalPrice += inventory.get(sku);
                } else {
                    hasAllItems = false;
                    break;
                }
            }

            if (hasAllItems) {
                tripCosts.add(new StoreTrip(storeName, Math.round(totalPrice * 100.0) / 100.0));
            }
        }

        // 3. SELECTION SORT: Sorting from lowest to highest cost
        int n = tripCosts.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (tripCosts.get(j).totalCost < tripCosts.get(minIdx).totalCost) {
                    minIdx = j;
                }
            }
            // Swap
            StoreTrip temp = tripCosts.get(minIdx);
            tripCosts.set(minIdx, tripCosts.get(i));
            tripCosts.set(i, temp);
        }

        return tripCosts;
    }

	public Map<String, Map<String, Double>> getStoresData() {
	    return this.storesData;
	}
    
}
