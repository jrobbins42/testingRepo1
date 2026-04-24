/*Class AppGUI
 * this class implements the gui portion of my program  
 * @author James Robbins
 * @version 1.0
 * @since 1.0
 
 *I used chatgpt to help write this class
 * No Cheating
 * 
*/
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AppGUI extends JFrame {
    private GroceryOptimizer optimizer;
    private DefaultListModel<String> listModel;
    private JList<String> shoppingListUI;
    private JTextArea resultsArea;
    private JTextField itemInput;

    public AppGUI() {
        optimizer = new GroceryOptimizer();
        initializeMockData(); // Pre-populate the 5 markets

        setTitle("Grocery Price Optimizer - Quick List");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- UI Construction ---
        
        // Top: Input Panel
        JPanel topPanel = new JPanel(new FlowLayout());
        itemInput = new JTextField(10);
        JButton addButton = new JButton("Add SKU");
        topPanel.add(new JLabel("Enter Items such as MILK,EGGS,BREAD,SUGAR,CEREAL:"));
        topPanel.add(itemInput);
        topPanel.add(addButton);

        // Center: Lists
        listModel = new DefaultListModel<>();
        shoppingListUI = new JList<>(listModel);
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultsArea.setBackground(new Color(245, 245, 245));

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        centerPanel.add(new JScrollPane(shoppingListUI));
        centerPanel.add(new JScrollPane(resultsArea));

        // Bottom: Buttons
        JPanel bottomPanel = new JPanel();
        JButton removeButton = new JButton("Remove Selected");
        JButton calculateButton = new JButton("Compare Market Prices");
        bottomPanel.add(removeButton);
        bottomPanel.add(calculateButton);

        // --- Listeners ---
        addButton.addActionListener(e -> addItemWithValidation());
        removeButton.addActionListener(e -> {
            int idx = shoppingListUI.getSelectedIndex();
            if (idx != -1) listModel.remove(idx);
        });
        calculateButton.addActionListener(e -> calculateTrip());

        // Assembly
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** Pre-populates 5 markets with 5 items and random prices */
    private void initializeMockData() {
        String[] markets = {"Market A", "Market B", "Market C", "Market D", "Market E"};
        String[] skus = {"MILK", "EGGS", "BREAD", "SUGAR", "CEREAL"};
        Random rand = new Random();

        for (String store : markets) {
            Map<String, Double> inventory = new HashMap<>();
            for (String sku : skus) {
                // Random price between 1.50 and 6.50
                double randomPrice = 1.50 + (5.00 * rand.nextDouble());
                inventory.put(sku, randomPrice);
            }
            optimizer.updatePrices(store, inventory);
        }
    }

    private void addItemWithValidation() {
        String item = itemInput.getText().trim().toUpperCase();
        if (item.isEmpty()) return;

        if (isItemInSystem(item)) {
            listModel.addElement(item);
            itemInput.setText("");
        } else {
            JOptionPane.showMessageDialog(this, 
                "Item not found! Try: MILK, EGGS, BREAD, SUGAR, or CEREAL.", 
                "Missing SKU", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean isItemInSystem(String sku) {
        for (Map<String, Double> inventory : optimizer.getStoresData().values()) {
            if (inventory.containsKey(sku)) return true;
        }
        return false;
    }

    private void calculateTrip() {
        if (listModel.isEmpty()) {
            resultsArea.setText("Please add items to your list first.");
            return;
        }

        List<String> myBag = new ArrayList<>();
        for (int i = 0; i < listModel.size(); i++) {
            myBag.add(listModel.getElementAt(i));
        }

        List<StoreTrip> results = optimizer.findBestTotalTrip(myBag);
        
        StringBuilder sb = new StringBuilder("--- STORE COMPARISON ---\n\n");
        sb.append(String.format("%-15s %-10s\n", "Market Name", "Total Cost"));
        sb.append("---------------------------\n");
        
        if (results.isEmpty()) {
            sb.append("No stores carry all these items.");
        } else {
            for (StoreTrip trip : results) {
                sb.append(String.format("%-15s $%7.2f\n", trip.storeName, trip.totalCost));
            }
        }
        resultsArea.setText(sb.toString());
    }
}