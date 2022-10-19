package io.github.andreaszks.ergasales.app;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import io.github.andreaszks.ergasales.model.Customer;
import io.github.andreaszks.ergasales.model.CustomerRegistry;
import io.github.andreaszks.ergasales.model.Product;
import io.github.andreaszks.ergasales.model.ProductQuantityPair;
import io.github.andreaszks.ergasales.model.Registry;
import io.github.andreaszks.ergasales.model.SaleRegistry;
import io.github.andreaszks.ergasales.tools.IDTools;
import io.github.andreaszks.ergasales.tools.SwingTools;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

/** New Sales are created in this panel. */
public class NewSalePanel extends JPanel {

  private static final long serialVersionUID = 1L;
  private JPanel panelCustomerSelect;
  private JPanel panelDetails;
  private JPanel panelAvailableLabel;
  private JScrollPane panelAvailableProductsTable;
  private JPanel panelUsedLabel;
  private JScrollPane panelProductsUsedTable;
  private JPanel panelControls;
  private JLabel lblSelectCustomer;
  private JLabel lblDate;
  private JLabel lblNotes;
  private JLabel lblSalePrice;
  private JLabel lblAvailableProducts;
  private JLabel lblProductsUsed;
  private DefaultComboBoxModel<String> customersModel;
  private JComboBox<String> comboBox;
  private JTextField textSearchCustomer;
  private JButton btnSearchCustomer;
  private DatePicker datePicker;
  private JTextField textNotes;
  private JSpinner spinnerPrice;
  private DefaultTableModel availableProductsTableModel;
  private JTable availableProductsTable;
  private JSpinner spinnerQuantity;
  private JButton btnAdd;
  private DefaultTableModel productsUsedTableModel;
  private JTable productsUsedTable;
  private JButton btnNewSale;
  private JButton btnRemove;
  private JTextField textSearchProduct;
  private JButton btnSearchProduct;

  /** Create the panel. */
  public NewSalePanel() {

    panelCustomerSelect = new JPanel();
    panelCustomerSelect.setBorder(new LineBorder(new Color(0, 0, 0)));

    panelDetails = new JPanel();
    panelDetails.setBorder(new LineBorder(new Color(0, 0, 0)));

    panelAvailableLabel = new JPanel();
    panelAvailableLabel.setBorder(new LineBorder(new Color(0, 0, 0)));

    panelAvailableProductsTable = new JScrollPane();

    panelUsedLabel = new JPanel();
    panelUsedLabel.setBorder(new LineBorder(new Color(0, 0, 0)));

    panelProductsUsedTable = new JScrollPane();

    panelControls = new JPanel();
    panelControls.setBorder(new LineBorder(new Color(0, 0, 0)));

    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
        groupLayout
            .createParallelGroup(Alignment.LEADING)
            .addComponent(panelCustomerSelect, GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
            .addComponent(panelDetails, GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
            .addComponent(panelAvailableLabel, GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
            .addComponent(panelUsedLabel, GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
            .addComponent(panelControls, GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
            .addComponent(panelProductsUsedTable, GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
            .addComponent(
                panelAvailableProductsTable, GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE));
    groupLayout.setVerticalGroup(
        groupLayout
            .createParallelGroup(Alignment.TRAILING)
            .addGroup(
                groupLayout
                    .createSequentialGroup()
                    .addComponent(
                        panelCustomerSelect,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelDetails,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelAvailableLabel,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelAvailableProductsTable, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelUsedLabel,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelProductsUsedTable,
                        GroupLayout.PREFERRED_SIZE,
                        121,
                        GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelControls,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)));

    // 1ST PANEL - CUSTOMER SELECT
    lblSelectCustomer = new JLabel("Select Customer");

    comboBox = new JComboBox<>();
    customersModel = new DefaultComboBoxModel<>(new String[] {""});
    comboBox.setModel(customersModel);

    // Make the size of comboBox to be fixed and fit all names and IDs by default
    StringBuilder sb = new StringBuilder();
    int placeholderLength =
        (CustomerRegistry.MAX_NAME_LENGTH * 2)
            + CustomerRegistry.PHONE_LENGTH
            + IDTools.ID_LENGTH
            + 3;
    // +3 = space between last/first, first/phone, phone/id
    for (int i = 0; i < placeholderLength; i++) sb.append("W");

    comboBox.setPrototypeDisplayValue(sb.toString());
    comboBox.setFont(
        new Font("Courier New", comboBox.getFont().getStyle(), comboBox.getFont().getSize()));

    textSearchCustomer = new JTextField();
    textSearchCustomer.setColumns(10);

    btnSearchCustomer = new JButton("Search");

    panelCustomerSelect.add(lblSelectCustomer);
    panelCustomerSelect.add(comboBox);
    panelCustomerSelect.add(textSearchCustomer);
    panelCustomerSelect.add(btnSearchCustomer);

    // 2ND PANEL - DETAILS
    lblDate = new JLabel("Date");

    DatePickerSettings dateSettings = new DatePickerSettings();
    dateSettings.setAllowEmptyDates(false);
    datePicker = new DatePicker(dateSettings);
    datePicker.setDateToToday();
    datePicker.getComponentDateTextField().setEditable(false);

    lblNotes = new JLabel("Notes");

    textNotes = new JTextField();
    textNotes.setColumns(25);

    lblSalePrice = new JLabel("Sale Price");

    spinnerPrice = new JSpinner();
    spinnerPrice.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
    ((DefaultEditor) spinnerPrice.getEditor()).getTextField().setColumns(4);
    ((JSpinner.NumberEditor) spinnerPrice.getEditor()).getFormat().setMaximumFractionDigits(2);

    panelDetails.add(lblDate);
    panelDetails.add(datePicker);
    panelDetails.add(lblNotes);
    panelDetails.add(textNotes);
    panelDetails.add(lblSalePrice);
    panelDetails.add(spinnerPrice);

    // 3RD PANEL - AVAILABLE PRODUCTS LABEL
    lblAvailableProducts = new JLabel("Available Products / Search");

    textSearchProduct = new JTextField();
    textSearchProduct.setColumns(15);

    btnSearchProduct = new JButton("Search");

    panelAvailableLabel.add(lblAvailableProducts);
    panelAvailableLabel.add(textSearchProduct);
    panelAvailableLabel.add(btnSearchProduct);

    // 4TH PANEL - AVAILABLE PRODUCTS TABLE
    availableProductsTableModel =
        new DefaultTableModel(null, new String[] {"ID", "Product", "Type"}) {
          private static final long serialVersionUID = 1L;

          @Override
          public boolean isCellEditable(int row, int column) {
            // all cells false
            return false;
          }
        };

    availableProductsTable = new JTable();
    availableProductsTable.setModel(availableProductsTableModel);
    availableProductsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    panelAvailableProductsTable.setViewportView(availableProductsTable);

    // 5TH PANEL - PRODUCTS USED LABEL
    lblProductsUsed = new JLabel("Products Used");

    spinnerQuantity = new JSpinner();
    spinnerQuantity.setModel(new SpinnerNumberModel(1, 1, null, 1));
    ((DefaultEditor) spinnerQuantity.getEditor()).getTextField().setColumns(2);

    btnAdd = new JButton("Add");
    btnRemove = new JButton("Remove");

    panelUsedLabel.add(lblProductsUsed);
    panelUsedLabel.add(spinnerQuantity);
    panelUsedLabel.add(btnAdd);
    panelUsedLabel.add(btnRemove);

    // 6TH PANEL - PRODUCTS USED TABLE
    productsUsedTableModel =
        new DefaultTableModel(null, new String[] {"ID", "Product", "Type", "Quantity"}) {
          private static final long serialVersionUID = 1L;

          @Override
          public boolean isCellEditable(int row, int column) {
            // all cells false
            return false;
          }
        };

    productsUsedTable = new JTable();
    productsUsedTable.setModel(productsUsedTableModel);
    panelProductsUsedTable.setViewportView(productsUsedTable);

    // 7TH PANEL - CONTROLS - NEW SALE BUTTON
    btnNewSale = new JButton("New Sale");

    panelControls.add(btnNewSale);

    setLayout(groupLayout);
    initListeners();
  }

  private void initListeners() {
    textSearchCustomer.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusGained(FocusEvent arg0) {
            textSearchCustomer.setText("");
          }
        });

    btnSearchCustomer.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {

            List<Customer> customers = Registry.getInstance().getCustomerRegistry().getCustomers();
            List<Customer> filteredCustomers =
                Registry.getInstance()
                    .getCustomerRegistry()
                    .getCustomersByTerm(textSearchCustomer.getText());

            if (filteredCustomers.isEmpty()) {
              SwingTools.showErrorDialog("No customer found with this term!");
              updateCustomersModel(customers);
            } else {
              updateCustomersModel(filteredCustomers);
            }
          }
        });

    textSearchProduct.addFocusListener(
        new FocusAdapter() {
          @Override
          public void focusGained(FocusEvent arg0) {
            textSearchProduct.setText("");
          }
        });

    btnSearchProduct.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {

            String productTerm = textSearchProduct.getText();
            List<Product> foundProducts =
                Registry.getInstance().getProductRegistry().getProductsByTerm(productTerm);

            // Remove already added products from being added again to available table

            ArrayList<Product> temporaryProductsUsed = new ArrayList<>();
            for (int i = 0; i < productsUsedTable.getRowCount(); i++) {
              Product product =
                  Registry.getInstance()
                      .getProductRegistry()
                      .getProductById((String) productsUsedTable.getValueAt(i, 0));
              temporaryProductsUsed.add(product);
            }
            foundProducts.removeAll(temporaryProductsUsed);

            if (foundProducts.isEmpty()) {
              JOptionPane.showMessageDialog(null, "No products found");
            }

            updateModelAvailable(foundProducts);
          }
        });

    btnAdd.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {

            int row = availableProductsTable.getSelectedRow();
            if (availableProductsTableModel.getRowCount() != 0 && row != -1) {

              String productId = String.valueOf(availableProductsTableModel.getValueAt(row, 0));
              String productName = String.valueOf(availableProductsTableModel.getValueAt(row, 1));
              String productType = String.valueOf(availableProductsTableModel.getValueAt(row, 2));
              Integer productQuantity =
                  Integer.parseInt(String.valueOf(spinnerQuantity.getValue()));

              availableProductsTableModel.removeRow(row);
              productsUsedTableModel.addRow(
                  new Object[] {productId, productName, productType, productQuantity});

              productsUsedTableModel.fireTableDataChanged();
              availableProductsTableModel.fireTableDataChanged();
              SwingTools.resizeColumns(productsUsedTable);
              SwingTools.resizeColumns(availableProductsTable);

              spinnerQuantity.setValue(1);
              btnNewSale.setEnabled(true);
            }
          }
        });

    btnRemove.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {

            int row = productsUsedTable.getSelectedRow();
            if (productsUsedTableModel.getRowCount() != 0 && row != -1) {

              String productId = String.valueOf(productsUsedTableModel.getValueAt(row, 0));
              Product productObject =
                  Registry.getInstance().getProductRegistry().getProductById(productId);

              productsUsedTableModel.removeRow(row);
              availableProductsTableModel.addRow(
                  new Object[] {
                    productObject.getId(), productObject.getName(), productObject.getType()
                  });

              productsUsedTableModel.fireTableDataChanged();
              availableProductsTableModel.fireTableDataChanged();
              SwingTools.resizeColumns(productsUsedTable);
              SwingTools.resizeColumns(availableProductsTable);

              if (productsUsedTable.getRowCount() == 0) btnNewSale.setEnabled(false);
            }
          }
        });

    btnNewSale.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {

            int lengthBeforeId =
                CustomerRegistry.MAX_NAME_LENGTH * 2 + CustomerRegistry.PHONE_LENGTH + 3;
            int lengthWithId = lengthBeforeId + IDTools.ID_LENGTH;

            String strippedCustomerId =
                comboBox
                    .getSelectedItem()
                    .toString()
                    .substring(lengthBeforeId, lengthWithId)
                    .replace(" ", "");

            LocalDate date = datePicker.getDate();
            String notes = textNotes.getText();
            float price = Float.parseFloat(spinnerPrice.getValue().toString());

            if (notes.length() < SaleRegistry.MAX_NOTES_LENGTH) {

              ArrayList<ProductQuantityPair> productsUsed = new ArrayList<>();

              for (int i = 0; i < productsUsedTable.getRowCount(); i++) {

                Product product =
                    Registry.getInstance()
                        .getProductRegistry()
                        .getProductById((String) productsUsedTable.getValueAt(i, 0));
                int quantity = Integer.parseInt(String.valueOf(productsUsedTable.getValueAt(i, 3)));

                productsUsed.add(new ProductQuantityPair(product, quantity));
              }

              if (Registry.getInstance()
                  .getSaleRegistry()
                  .createSale(date, strippedCustomerId, productsUsed, notes, price)) {
                JOptionPane.showMessageDialog(null, "New Sale created successfully!");
                ApplicationFrame.showSalesCard();
              } else {
                SwingTools.showErrorDialog(
                    "Sale failed to create!\nPossible cause: no more available IDs");
              }

            } else {
              SwingTools.showErrorDialog(
                  "Notes exceeding character limit (Maximum characters: "
                      + SaleRegistry.MAX_NOTES_LENGTH
                      + ")");
            }
            ApplicationFrame.checkUnsavedChanges();
          }
        });
  }

  /**
   * Populate the comboBox with specified Customers.
   *
   * @param list
   */
  public void updateCustomersModel(List<Customer> list) {

    btnNewSale.setEnabled(false);
    customersModel.removeAllElements();

    for (Customer customer : list) {
      String format =
          "%-"
              + CustomerRegistry.MAX_NAME_LENGTH
              + "s"
              + " %-"
              + CustomerRegistry.MAX_NAME_LENGTH
              + "s"
              + " %-"
              + CustomerRegistry.PHONE_LENGTH
              + "s"
              + " %"
              + IDTools.ID_LENGTH
              + "s";
      customersModel.addElement(
          String.format(
              format,
              customer.getLastName(),
              customer.getFirstName(),
              customer.getPhone(),
              customer.getId()));
    }

    if (customersModel.getSize() != 0)
      updateModelAvailable(Registry.getInstance().getProductRegistry().getProducts());
  }

  /**
   * Puts a single customer in the comboBox.
   *
   * @param c
   */
  public void updateCustomersModel(Customer c) {
    ArrayList<Customer> customerInArrayList = new ArrayList<>();
    customerInArrayList.add(c);
    updateCustomersModel(customerInArrayList);
  }

  private void updateModelAvailable(List<Product> list) {
    availableProductsTableModel.setRowCount(0);

    Collections.sort(list, (a, b) -> a.toString().compareTo(b.toString()));

    for (Product product : list) {
      availableProductsTableModel.addRow(
          new Object[] {product.getId(), product.getName(), product.getType()});
    }

    availableProductsTableModel.fireTableDataChanged();
    SwingTools.resizeColumns(availableProductsTable);
    if (productsUsedTable.getRowCount() > 0) btnNewSale.setEnabled(true);
  }

  /** Clears all fields in the panel. */
  public void clearFields() {
    textNotes.setText("");
    textSearchCustomer.setText("");
    textSearchProduct.setText("");
    spinnerPrice.setValue(0.0);
    spinnerQuantity.setValue(1);
    productsUsedTableModel.setRowCount(0);
    availableProductsTableModel.setRowCount(0);
    datePicker.setDateToToday();
  }
}
