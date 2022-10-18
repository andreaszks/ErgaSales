package io.github.andreaszks.ergasales.app;

import io.github.andreaszks.ergasales.model.Product;
import io.github.andreaszks.ergasales.model.ProductRegistry;
import io.github.andreaszks.ergasales.model.Registry;
import io.github.andreaszks.ergasales.model.Sale;
import io.github.andreaszks.ergasales.tools.SwingTools;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

/** Products are managed in this panel. */
public class ProductsPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  private JTable productsTable;
  private JScrollPane panelProductsTable;
  private JPanel panelControls;
  private DefaultTableModel productsTableModel;
  private JButton btnSearchProduct;
  private JButton btnDeleteProduct;
  private JButton btnEditProduct;
  private JButton btnShowSales;
  private JTextField textName;
  private JTextField textType;
  private JLabel lblName;
  private JLabel lblType;
  private JButton btnCreateProduct;
  private JPanel panelCreateProduct;
  private JPanel panelSearchProduct;
  private JTextField txtSearchProduct;
  private JComboBox<String> comboBoxTypes;
  private DefaultComboBoxModel<String> comboBoxModelTypes;

  /** Create the panel. */
  public ProductsPanel() {

    panelProductsTable = new JScrollPane();

    panelControls = new JPanel();
    panelControls.setBorder(new LineBorder(new Color(0, 0, 0)));

    panelCreateProduct = new JPanel();
    panelCreateProduct.setBorder(new LineBorder(new Color(0, 0, 0)));

    panelSearchProduct = new JPanel();
    panelSearchProduct.setBorder(new LineBorder(new Color(0, 0, 0)));

    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
        groupLayout
            .createParallelGroup(Alignment.LEADING)
            .addComponent(panelControls, GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
            .addComponent(panelSearchProduct, GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
            .addComponent(
                panelCreateProduct,
                Alignment.TRAILING,
                GroupLayout.DEFAULT_SIZE,
                562,
                Short.MAX_VALUE)
            .addComponent(panelProductsTable, GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE));
    groupLayout.setVerticalGroup(
        groupLayout
            .createParallelGroup(Alignment.TRAILING)
            .addGroup(
                groupLayout
                    .createSequentialGroup()
                    .addComponent(
                        panelSearchProduct,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelCreateProduct,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelProductsTable, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelControls,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)));

    // 1ST PANEL - SEARCH PRODUCT
    txtSearchProduct = new JTextField();
    txtSearchProduct.setColumns(20);

    comboBoxTypes = new JComboBox<>();
    comboBoxModelTypes = new DefaultComboBoxModel<>(new String[] {""});
    comboBoxTypes.setModel(comboBoxModelTypes);

    btnSearchProduct = new JButton("Search Product");

    panelSearchProduct.add(txtSearchProduct);
    panelSearchProduct.add(comboBoxTypes);
    panelSearchProduct.add(btnSearchProduct);

    // 2ND PANEL - CREATE PRODUCT
    lblName = new JLabel("Name");
    textName = new JTextField();
    textName.setColumns(10);

    lblType = new JLabel("Type");
    textType = new JTextField();
    textType.setColumns(10);

    btnCreateProduct = new JButton("Create Product");

    panelCreateProduct.add(lblName);
    panelCreateProduct.add(textName);
    panelCreateProduct.add(lblType);
    panelCreateProduct.add(textType);
    panelCreateProduct.add(btnCreateProduct);

    // 3RD PANEL - PRODUCTS TABLE
    productsTableModel =
        new DefaultTableModel(
            null,
            new String[] {"ID", "Product Name", "Product Type", "Total Sales", "Total Quantity"}) {
          private static final long serialVersionUID = 1L;

          @Override
          public boolean isCellEditable(int row, int column) {
            // all cells false
            return false;
          }
        };

    productsTable = new JTable();
    productsTable.setModel(productsTableModel);
    productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    panelProductsTable.setViewportView(productsTable);

    // 4TH PANEL - CONTROLS
    btnShowSales = new JButton("Show Sales");
    btnEditProduct = new JButton("Edit Product");
    btnDeleteProduct = new JButton("Delete Product");
    panelControls.add(btnShowSales);
    panelControls.add(btnEditProduct);
    panelControls.add(btnDeleteProduct);

    setLayout(groupLayout);
    initListeners();
  }

  private void initListeners() {
    btnSearchProduct.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            String searchTerm = txtSearchProduct.getText();

            if (searchTerm != null) {

              List<Product> foundProducts = new ArrayList<Product>();

              if (comboBoxTypes.getSelectedIndex() == -1 || comboBoxTypes.getSelectedIndex() == 0) {
                foundProducts =
                    Registry.getInstance().getProductRegistry().getProductsByTerm(searchTerm);
              } else {
                foundProducts =
                    Registry.getInstance()
                        .getProductRegistry()
                        .getProductsByTermAndType(
                            searchTerm, comboBoxTypes.getSelectedItem().toString());
              }

              if (!foundProducts.isEmpty()) {
                updateModel(foundProducts);
              } else {
                JOptionPane.showMessageDialog(null, "No products found with this term");
              }
            }
          }
        });

    btnCreateProduct.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            String productName = textName.getText().trim();
            String productType = textType.getText().trim();

            if (ProductRegistry.isProductCorrectFormat(productName, productType)) {

              if (Registry.getInstance()
                  .getProductRegistry()
                  .createProduct(productName, productType)) {
                updateModel(Registry.getInstance().getProductRegistry().getProducts());
                JOptionPane.showMessageDialog(null, "New Product created successfully");
              } else {
                SwingTools.showErrorDialog("Product with this name already exists!");
              }

            } else {
              String message =
                  "Enter all fields!\nMax characters:\n"
                      + "Name: "
                      + ProductRegistry.MAX_PRODUCT_NAME_LENGTH
                      + ", "
                      + "Type: "
                      + ProductRegistry.MAX_PRODUCT_TYPE_LENGTH;
              SwingTools.showErrorDialog(message);
            }
          }
        });

    btnShowSales.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            int row = productsTable.getSelectedRow();
            if (productsTableModel.getRowCount() != 0 && row != -1) {
              String id = (String) productsTableModel.getValueAt(row, 0);

              List<Sale> salesByProduct =
                  Registry.getInstance().getSaleRegistry().getSalesByProductId(id);

              ApplicationFrame.showSalesCardByList(salesByProduct);
            }
          }
        });

    btnEditProduct.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            int row = productsTable.getSelectedRow();
            if (productsTableModel.getRowCount() != 0 && row != -1) {
              String id = (String) productsTableModel.getValueAt(row, 0);

              Product product = Registry.getInstance().getProductRegistry().getProductById(id);
              JTextField textNewName = new JTextField(product.getName());
              JTextField textNewType = new JTextField(product.getType());

              Object[] message = {
                "Product Name:", textNewName,
                "Product Type:", textNewType
              };
              int option =
                  JOptionPane.showConfirmDialog(
                      null, message, "Edit Product", JOptionPane.OK_CANCEL_OPTION);

              if (option == JOptionPane.OK_OPTION) {

                String newName = textNewName.getText().trim();
                String newType = textNewType.getText().trim();

                if (ProductRegistry.isProductCorrectFormat(newName, newType)) {

                  if (Registry.getInstance()
                      .getProductRegistry()
                      .updateProduct(id, newName, newType)) {
                    updateModel(Registry.getInstance().getProductRegistry().getProducts());
                    JOptionPane.showMessageDialog(null, "Product updated successfully!");
                  } else {
                    SwingTools.showErrorDialog("There is another product with this name!");
                  }

                } else {
                  SwingTools.showErrorDialog(
                      "Enter all fields!\nMax characters:\nName: "
                          + ProductRegistry.MAX_PRODUCT_NAME_LENGTH
                          + ", Type: "
                          + ProductRegistry.MAX_PRODUCT_TYPE_LENGTH);
                }
              }
            }
          }
        });

    btnDeleteProduct.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {

            int row = productsTable.getSelectedRow();
            if (productsTableModel.getRowCount() != 0 && row != -1) {
              int s =
                  JOptionPane.showConfirmDialog(
                      null,
                      "Are you sure you want to delete product?\nDeleting product deletes all\nsaless containing this product!",
                      "Delete Product",
                      JOptionPane.YES_NO_OPTION,
                      JOptionPane.WARNING_MESSAGE);
              if (s == 0) {
                String id = (String) productsTableModel.getValueAt(row, 0);

                Registry.getInstance().getProductRegistry().deleteProductById(id);
                ;
                updateModel(Registry.getInstance().getProductRegistry().getProducts());
                JOptionPane.showMessageDialog(null, "Product deleted successfully!");
              }
            }
          }
        });
  }

  /**
   * Fills the table with specified products.
   *
   * @param list
   */
  public void updateModel(List<Product> list) {

    textName.setText("");
    textType.setText("");
    productsTableModel.setRowCount(0);

    comboBoxModelTypes.removeAllElements();
    comboBoxModelTypes.addElement("-- All --");

    List<String> productTypes = Registry.getInstance().getProductRegistry().getTypes();
    Collections.sort(productTypes, (a, b) -> a.compareTo(b));

    for (String string : productTypes) {
      comboBoxModelTypes.addElement(string);
    }

    Collections.sort(list, (a, b) -> a.toString().compareTo(b.toString()));

    for (Product product : list) {

      productsTableModel.addRow(
          new Object[] {
            product.getId(),
            product.getName(),
            product.getType(),
            product.getTotalSales(),
            product.getTotalQuantity()
          });
    }
    productsTableModel.fireTableDataChanged();
    SwingTools.resizeColumns(productsTable);
  }
}
