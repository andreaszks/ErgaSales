package io.github.andreaszks.ergasales.app;

import io.github.andreaszks.ergasales.model.ProductQuantityPair;
import io.github.andreaszks.ergasales.model.Registry;
import io.github.andreaszks.ergasales.model.Sale;
import io.github.andreaszks.ergasales.tools.SwingTools;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

/** Sales are managed in this panel. */
public class SalesPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private JTable salesTable;
  private DefaultTableModel salesTableModel;
  private JLabel lblTotalRevenue;
  private JTextField textNotes;
  private JTable productsUsedTable;
  private DefaultTableModel productsUsedTableModel;
  private JPanel panelControls;
  private JButton btnEditSale;
  private JButton btnDeleteSale;
  private JPanel panelNotes;
  private JScrollPane panelProductsUsedTable;
  private JScrollPane panelSalesTable;
  private JLabel lblNotes;
  private JTextField txtSearchSale;
  private JButton btnSearchSale;
  private JPanel panelSearchSale;

  /** Create the panel. */
  public SalesPanel() {

    panelSalesTable = new JScrollPane();

    panelProductsUsedTable = new JScrollPane();

    panelNotes = new JPanel();
    panelNotes.setBorder(new LineBorder(new Color(0, 0, 0)));

    panelControls = new JPanel();
    panelControls.setBorder(new LineBorder(new Color(0, 0, 0)));

    panelSearchSale = new JPanel();
    panelSearchSale.setBorder(new LineBorder(new Color(0, 0, 0)));

    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
        groupLayout
            .createParallelGroup(Alignment.LEADING)
            .addComponent(panelControls, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
            .addComponent(panelNotes, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
            .addComponent(panelProductsUsedTable, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
            .addComponent(panelSearchSale, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
            .addComponent(
                panelSalesTable,
                Alignment.TRAILING,
                GroupLayout.DEFAULT_SIZE,
                450,
                Short.MAX_VALUE));
    groupLayout.setVerticalGroup(
        groupLayout
            .createParallelGroup(Alignment.TRAILING)
            .addGroup(
                groupLayout
                    .createSequentialGroup()
                    .addComponent(
                        panelSearchSale,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(panelSalesTable, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelProductsUsedTable,
                        GroupLayout.PREFERRED_SIZE,
                        92,
                        GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelNotes, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelControls,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)));

    // 1ST PANEL - SEARCH SALE
    txtSearchSale = new JTextField();
    txtSearchSale.setColumns(20);

    btnSearchSale = new JButton("Search Sale");

    panelSearchSale.add(txtSearchSale);
    panelSearchSale.add(btnSearchSale);

    // 2ND PANEL - SALES TABLE

    salesTableModel =
        new DefaultTableModel(null, new String[] {"Date", "ID", "Customer", "Price"}) {
          private static final long serialVersionUID = 1L;

          @Override
          public boolean isCellEditable(int row, int column) {
            // all cells false
            return false;
          }
        };

    salesTable = new JTable();
    salesTable.setModel(salesTableModel);
    salesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    panelSalesTable.setViewportView(salesTable);

    // 3RD PANEL - PRODUCTS USED TABLE
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

    // 4TH PANEL - NOTES PANEL
    lblNotes = new JLabel("Notes:");
    textNotes = new JTextField();
    textNotes.setEditable(false);
    textNotes.setColumns(50);

    panelNotes.add(lblNotes);
    panelNotes.add(textNotes);

    // 5TH PANEL - CONTROL
    btnEditSale = new JButton("Edit Sale");
    btnDeleteSale = new JButton("Delete Sale");
    lblTotalRevenue = new JLabel("Total Revenue: ");

    panelControls.add(btnEditSale);
    panelControls.add(btnDeleteSale);
    panelControls.add(lblTotalRevenue);

    setLayout(groupLayout);
    initListeners();
  }

  private void initListeners() {
    btnSearchSale.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            String searchTerm = txtSearchSale.getText();

            if (searchTerm != null) {

              List<Sale> foundSales =
                  Registry.getInstance().getSaleRegistry().getSalesByTerm(searchTerm);

              if (!foundSales.isEmpty()) {
                updateModel(foundSales);
              } else {
                JOptionPane.showMessageDialog(
                    null,
                    "No sales found with this term",
                    "Sales Search",
                    JOptionPane.WARNING_MESSAGE);
              }
            }

            txtSearchSale.setText("");
          }
        });

    salesTable.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseReleased(MouseEvent arg0) {

            int row = salesTable.getSelectedRow();
            if (salesTableModel.getRowCount() != 0 && row != -1) {
              String id = (String) salesTableModel.getValueAt(row, 1);
              String selectedSaleNotes =
                  Registry.getInstance().getSaleRegistry().getSaleById(id).getNotes();
              textNotes.setText(selectedSaleNotes);

              productsUsedTableModel.setRowCount(0);

              Sale sale = Registry.getInstance().getSaleRegistry().getSaleById(id);

              List<ProductQuantityPair> productsUsedInSale = sale.getProductsUsed();

              for (ProductQuantityPair pqp : productsUsedInSale) {
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
            }
          }
        });

    salesTable.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyReleased(KeyEvent arg0) {

            if (arg0.getExtendedKeyCode() == KeyEvent.VK_DOWN
                || arg0.getExtendedKeyCode() == KeyEvent.VK_UP) {
              int row = salesTable.getSelectedRow();
              if (salesTableModel.getRowCount() != 0 && row != -1) {
                String id = (String) salesTableModel.getValueAt(row, 1);
                String selectedSaleNotes =
                    Registry.getInstance().getSaleRegistry().getSaleById(id).getNotes();
                textNotes.setText(selectedSaleNotes);

                productsUsedTableModel.setRowCount(0);

                Sale sale = Registry.getInstance().getSaleRegistry().getSaleById(id);

                List<ProductQuantityPair> productsUsedInSale = sale.getProductsUsed();

                for (ProductQuantityPair pqp : productsUsedInSale) {
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
              }
            }
          }
        });

    btnEditSale.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            int row = salesTable.getSelectedRow();
            if (salesTableModel.getRowCount() != 0 && row != -1) {
              String id = (String) salesTableModel.getValueAt(row, 1);

              Sale saleToEdit = Registry.getInstance().getSaleRegistry().getSaleById(id);

              ApplicationFrame.showEditSaleCard(saleToEdit);
            }
            ApplicationFrame.checkUnsavedChanges();
          }
        });

    btnDeleteSale.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {

            int row = salesTable.getSelectedRow();
            if (salesTableModel.getRowCount() != 0 && row != -1) {

              int s =
                  JOptionPane.showConfirmDialog(
                      null,
                      "Are you sure you want to delete sale?",
                      "Delete Sale",
                      JOptionPane.YES_NO_OPTION,
                      JOptionPane.WARNING_MESSAGE);
              if (s == 0) {
                String id = (String) salesTableModel.getValueAt(row, 1);
                Registry.getInstance().getSaleRegistry().deleteSaleById(id);
                JOptionPane.showMessageDialog(null, "Sale deleted successfully!");
                ApplicationFrame.showSalesCard();
              }
            }
            ApplicationFrame.checkUnsavedChanges();
          }
        });
  }

  /** Fills table with specified sales. */
  public void updateModel(List<Sale> salesByCustomer) {

    salesTableModel.setRowCount(0);
    productsUsedTableModel.setRowCount(0);
    textNotes.setText("");

    ArrayList<Sale> sortedSales = new ArrayList<>();
    for (Sale sale : salesByCustomer) sortedSales.add(sale);
    Collections.sort(
        sortedSales, (a, b) -> a.getDate().toString().compareTo(b.getDate().toString()));

    float totalRevenue = 0;
    for (Sale sale : sortedSales) {

      salesTableModel.addRow(
          new Object[] {
            sale.getDate(),
            sale.getId(),
            sale.getCustomer().getLastName() + " " + sale.getCustomer().getFirstName(),
            String.format("%.1f", sale.getPrice())
          });
      totalRevenue += sale.getPrice();
    }
    lblTotalRevenue.setText("Total Revenue: " + String.format("%.1f", totalRevenue));
    salesTableModel.fireTableDataChanged();
    SwingTools.resizeColumns(salesTable);
  }
}
