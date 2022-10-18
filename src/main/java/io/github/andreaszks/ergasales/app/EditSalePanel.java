package io.github.andreaszks.ergasales.app;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import io.github.andreaszks.ergasales.model.Product;
import io.github.andreaszks.ergasales.model.ProductQuantityPair;
import io.github.andreaszks.ergasales.model.Registry;
import io.github.andreaszks.ergasales.model.Sale;
import io.github.andreaszks.ergasales.model.SaleRegistry;
import io.github.andreaszks.ergasales.tools.SwingTools;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
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

/** Sales are edited in this panel. */
public class EditSalePanel extends JPanel {

  private static final long serialVersionUID = 1L;
  private JPanel panelControls;
  private JButton btnUpdateSale;
  private JTextField textNotes;
  private JSpinner spinnerPrice;
  private DatePicker datePicker;
  private Sale saleToEdit;
  private JTable availableProductsTable;
  private JTable productsUsedTable;
  private JSpinner spinnerQuantity;
  private DefaultTableModel availableProductsTableModel;
  private DefaultTableModel productsUsedTableModel;
  private JButton btnAdd;
  private JButton btnRemove;
  private JPanel panelAvailableLabel;
  private JLabel lblAvailableProducts;
  private JPanel panelAddProduct;
  private JPanel panelDetails;
  private JScrollPane panelAvailableProductsTable;
  private JScrollPane panelProductsUsedTable;
  private JLabel lblDate;
  private JLabel lblNotes;
  private JLabel lblPrice;
  private JTextField textSearchProduct;
  private JButton btnSearchProduct;
  private JLabel lblProductsUsed;

  /** Create the panel. */
  public EditSalePanel() {

    panelDetails = new JPanel();
    panelDetails.setBorder(new LineBorder(new Color(0, 0, 0)));

    panelAvailableLabel = new JPanel();
    panelAvailableLabel.setBorder(new LineBorder(new Color(0, 0, 0)));

    panelAvailableProductsTable = new JScrollPane();

    panelAddProduct = new JPanel();
    panelAddProduct.setBorder(new LineBorder(new Color(0, 0, 0)));

    panelProductsUsedTable = new JScrollPane();

    panelControls = new JPanel();
    panelControls.setBorder(new LineBorder(new Color(0, 0, 0)));

    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
        groupLayout
            .createParallelGroup(Alignment.LEADING)
            .addComponent(panelControls, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
            .addComponent(panelDetails, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
            .addComponent(panelAvailableLabel, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
            .addComponent(panelProductsUsedTable, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
            .addComponent(
                panelAddProduct, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
            .addComponent(
                panelAvailableProductsTable, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));
    groupLayout.setVerticalGroup(
        groupLayout
            .createParallelGroup(Alignment.TRAILING)
            .addGroup(
                groupLayout
                    .createSequentialGroup()
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
                        panelAvailableProductsTable, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelAddProduct,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelProductsUsedTable,
                        GroupLayout.PREFERRED_SIZE,
                        127,
                        GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelControls,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)));

    // 1ST PANEL - DETAILS
    lblDate = new JLabel("Date");

    DatePickerSettings dateSettings = new DatePickerSettings();
    dateSettings.setAllowEmptyDates(false);
    datePicker = new DatePicker(dateSettings);
    datePicker.getComponentDateTextField().setEditable(false);

    lblNotes = new JLabel("Notes");

    textNotes = new JTextField();
    textNotes.setColumns(25);

    lblPrice = new JLabel("Price");

    spinnerPrice = new JSpinner();
    spinnerPrice.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.0));
    ((DefaultEditor) spinnerPrice.getEditor()).getTextField().setColumns(4);
    ((JSpinner.NumberEditor) spinnerPrice.getEditor()).getFormat().setMaximumFractionDigits(2);

    panelDetails.add(lblDate);
    panelDetails.add(datePicker);
    panelDetails.add(lblNotes);
    panelDetails.add(textNotes);
    panelDetails.add(lblPrice);
    panelDetails.add(spinnerPrice);

    // 2ND PANEL - AVAILABLE PRODUCTS LABEL
    lblAvailableProducts = new JLabel("Available Products / Search");

    textSearchProduct = new JTextField();
    textSearchProduct.setColumns(15);

    btnSearchProduct = new JButton("Search");

    panelAvailableLabel.add(lblAvailableProducts);
    panelAvailableLabel.add(textSearchProduct);
    panelAvailableLabel.add(btnSearchProduct);

    // 3RD PANEL - AVAILABLE PRODUCTS TABLE
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

    // 4TH PANEL - ADD PRODUCT
    lblProductsUsed = new JLabel("Products Used");

    spinnerQuantity = new JSpinner();
    spinnerQuantity.setModel(new SpinnerNumberModel(1, 1, null, 1));
    ((DefaultEditor) spinnerPrice.getEditor()).getTextField().setColumns(2);

    btnAdd = new JButton("Add");

    btnRemove = new JButton("Remove");

    panelAddProduct.add(lblProductsUsed);
    panelAddProduct.add(spinnerQuantity);
    panelAddProduct.add(btnAdd);
    panelAddProduct.add(btnRemove);

    // 5TH PANEL - PRODUCTS USED TABLE
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
    productsUsedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    panelProductsUsedTable.setViewportView(productsUsedTable);

    // 6TH PANEL - CONTROLS
    btnUpdateSale = new JButton("Update Sale");

    panelControls.add(btnUpdateSale);
    setLayout(groupLayout);
    initListeners();
  }

  private void initListeners() {
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
              btnUpdateSale.setEnabled(true);
            }
          }
        });

    btnRemove.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

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

              if (productsUsedTable.getRowCount() == 0) btnUpdateSale.setEnabled(false);
            }
          }
        });

    btnUpdateSale.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {

            LocalDate date = datePicker.getDate();
            String notes = textNotes.getText();
            float price = Float.parseFloat(spinnerPrice.getValue().toString());

            ArrayList<ProductQuantityPair> productsUsed = new ArrayList<>();

            for (int i = 0; i < productsUsedTable.getRowCount(); i++) {

              Product product =
                  Registry.getInstance()
                      .getProductRegistry()
                      .getProductById((String) productsUsedTable.getValueAt(i, 0));
              int quantity = Integer.parseInt(String.valueOf(productsUsedTable.getValueAt(i, 3)));

              productsUsed.add(new ProductQuantityPair(product, quantity));
            }

            if (notes.length() > SaleRegistry.MAX_NOTES_LENGTH) {
              SwingTools.showErrorDialog(
                  "Notes exceeding character limit (Maximum characters: "
                      + SaleRegistry.MAX_NOTES_LENGTH
                      + ")");
            } else {
              Registry.getInstance()
                  .getSaleRegistry()
                  .updateSale(saleToEdit.getId(), date, price, notes, productsUsed);

              JOptionPane.showMessageDialog(null, "Sale updated successfully!");
              ApplicationFrame.showSalesCard();
            }
          }
        });
  }

  /**
   * Sets the fields with the details of specified sale
   *
   * @param saleToEdit
   */
  public void setSaleToEdit(Sale saleToEdit) {
    this.saleToEdit = saleToEdit;
    datePicker.setDate(saleToEdit.getDate());
    spinnerPrice.setValue(saleToEdit.getPrice());
    textNotes.setText(saleToEdit.getNotes());
    textSearchProduct.setText("");
    spinnerQuantity.setValue(1);
    productsUsedTableModel.setRowCount(0);
    availableProductsTableModel.setRowCount(0);
    btnUpdateSale.setEnabled(true);

    updateModelUsed();
  }

  private void updateModelUsed() {

    List<ProductQuantityPair> originalProductPairs = saleToEdit.getProductsUsed();
    ArrayList<Product> originalProducts = new ArrayList<>();

    // getProductsByTerm() is used instead of getProducts() because when removing original
    // products from available
    // if getProducts() is used, it will delete the products from the ArrayList of
    // Registry/CustomerList
    List<Product> availableProducts = Registry.getInstance().getProductRegistry().getProducts();

    for (ProductQuantityPair pqp : originalProductPairs) {
      originalProducts.add(pqp.getProduct());
    }
    availableProducts.removeAll(originalProducts);

    for (ProductQuantityPair pqp : originalProductPairs) {
      productsUsedTableModel.addRow(
          new Object[] {
            pqp.getProduct().getId(),
            pqp.getProduct().getName(),
            pqp.getProduct().getType(),
            pqp.getQuantity()
          });
    }

    productsUsedTableModel.fireTableDataChanged();
    SwingTools.resizeColumns(productsUsedTable);

    updateModelAvailable(availableProducts);
  }

  private void updateModelAvailable(List<Product> foundProducts) {

    availableProductsTableModel.setRowCount(0);

    Collections.sort(foundProducts, (a, b) -> a.toString().compareTo(b.toString()));

    for (Product product : foundProducts) {
      availableProductsTableModel.addRow(
          new Object[] {product.getId(), product.getName(), product.getType(), product.getType()});
    }

    availableProductsTableModel.fireTableDataChanged();
    SwingTools.resizeColumns(availableProductsTable);
    if (productsUsedTable.getRowCount() > 0) btnUpdateSale.setEnabled(true);
  }
}
