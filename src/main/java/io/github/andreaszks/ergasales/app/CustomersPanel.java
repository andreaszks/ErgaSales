package io.github.andreaszks.ergasales.app;

import io.github.andreaszks.ergasales.model.Customer;
import io.github.andreaszks.ergasales.model.CustomerRegistry;
import io.github.andreaszks.ergasales.model.Registry;
import io.github.andreaszks.ergasales.model.Sale;
import io.github.andreaszks.ergasales.tools.SwingTools;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

/** Customers are managed in this panel. */
public class CustomersPanel extends JPanel {

  private static final long serialVersionUID = 1L;
  private JTable customersTable;
  private JScrollPane panelCustomersTable;
  private JPanel panelControls;
  private DefaultTableModel customersTableModel;
  private JButton btnSearchCustomer;
  private JButton btnDeleteCustomer;
  private JButton btnEditCustomer;
  private JButton btnShowSales;
  private JTextField textLastName;
  private JTextField textFirstName;
  private JTextField textNotes;
  private JPanel panelCreateCustomer;
  private JLabel lblLastName;
  private JLabel lblFirstName;
  private JLabel lblNotes;
  private JButton btnCreateCustomer;
  private JTextField txtSearchCustomer;
  private JPanel panelSearchCustomer;
  private JButton btnNewSale;
  private JTextField textPhone;
  private JLabel lblPhone;

  /** Create the panel. */
  public CustomersPanel() {

    panelCreateCustomer = new JPanel();
    panelCreateCustomer.setBorder(new LineBorder(new Color(0, 0, 0)));

    panelCustomersTable = new JScrollPane();

    panelControls = new JPanel();
    panelControls.setBorder(new LineBorder(new Color(0, 0, 0)));

    panelSearchCustomer = new JPanel();
    panelSearchCustomer.setBorder(new LineBorder(new Color(0, 0, 0)));

    GroupLayout groupLayout = new GroupLayout(this);
    groupLayout.setHorizontalGroup(
        groupLayout
            .createParallelGroup(Alignment.LEADING)
            .addComponent(panelControls, GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
            .addComponent(panelSearchCustomer, GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE)
            .addComponent(
                panelCreateCustomer,
                Alignment.TRAILING,
                GroupLayout.DEFAULT_SIZE,
                690,
                Short.MAX_VALUE)
            .addComponent(panelCustomersTable, GroupLayout.DEFAULT_SIZE, 690, Short.MAX_VALUE));
    groupLayout.setVerticalGroup(
        groupLayout
            .createParallelGroup(Alignment.TRAILING)
            .addGroup(
                groupLayout
                    .createSequentialGroup()
                    .addComponent(
                        panelSearchCustomer,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelCreateCustomer,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelCustomersTable, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(
                        panelControls,
                        GroupLayout.PREFERRED_SIZE,
                        GroupLayout.DEFAULT_SIZE,
                        GroupLayout.PREFERRED_SIZE)));

    // 1ST PANEL - SEARCH CUSTOMER
    txtSearchCustomer = new JTextField();
    txtSearchCustomer.setColumns(20);

    btnSearchCustomer = new JButton("Search Customer");

    panelSearchCustomer.add(txtSearchCustomer);
    panelSearchCustomer.add(btnSearchCustomer);

    // 2ND PANEL - CREATE CUSTOMER
    lblLastName = new JLabel("Last Name");
    textLastName = new JTextField();
    textLastName.setColumns(10);

    lblFirstName = new JLabel("First Name");
    textFirstName = new JTextField();
    textFirstName.setColumns(10);

    lblPhone = new JLabel("Phone");
    textPhone = new JTextField();
    textPhone.setColumns(10);

    lblNotes = new JLabel("Notes");
    textNotes = new JTextField();
    textNotes.setColumns(10);

    btnCreateCustomer = new JButton("Create Customer");

    panelCreateCustomer.add(lblLastName);
    panelCreateCustomer.add(textLastName);
    panelCreateCustomer.add(lblFirstName);
    panelCreateCustomer.add(textFirstName);
    panelCreateCustomer.add(lblPhone);
    panelCreateCustomer.add(textPhone);
    panelCreateCustomer.add(lblNotes);
    panelCreateCustomer.add(textNotes);
    panelCreateCustomer.add(btnCreateCustomer);

    // 3RD PANEL - CUSTOMERS TABLE
    customersTableModel =
        new DefaultTableModel(
            null,
            new String[] {
              "ID",
              "Last Name",
              "First Name",
              "Phone",
              "Notes",
              "First Sale",
              "Last Sale",
              "Revenue",
              "Sales"
            }) {
          private static final long serialVersionUID = 1L;

          @Override
          public boolean isCellEditable(int row, int column) {
            // all cells false
            return false;
          }
        };

    customersTable = new JTable();
    customersTable.setModel(customersTableModel);
    customersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    panelCustomersTable.setViewportView(customersTable);

    // 4TH PANEL - CONTROLS
    btnNewSale = new JButton("New Sale");
    btnShowSales = new JButton("Show Sales");
    btnEditCustomer = new JButton("Edit Customer");
    btnDeleteCustomer = new JButton("Delete Customer");

    panelControls.add(btnNewSale);
    panelControls.add(btnShowSales);
    panelControls.add(btnEditCustomer);
    panelControls.add(btnDeleteCustomer);

    setLayout(groupLayout);
    initListeners();
  }

  private void initListeners() {
    btnSearchCustomer.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            String searchTerm = txtSearchCustomer.getText();

            if (searchTerm != null) {

              List<Customer> foundCustomers =
                  Registry.getInstance().getCustomerRegistry().getCustomersByTerm(searchTerm);

              if (!foundCustomers.isEmpty()) {
                updateModel(foundCustomers);
              } else {
                JOptionPane.showMessageDialog(
                    null,
                    "No customers found with this term",
                    "Customer Search",
                    JOptionPane.WARNING_MESSAGE);
              }
            }

            txtSearchCustomer.setText("");
          }
        });

    btnCreateCustomer.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            String lastname = textLastName.getText().trim();
            String firstname = textFirstName.getText().trim();
            String phone = textPhone.getText().trim();
            String notes = textNotes.getText().trim();

            if (CustomerRegistry.isCustomerCorrectFormat(lastname, firstname, phone, notes)) {

              if (Registry.getInstance()
                  .getCustomerRegistry()
                  .createCustomer(lastname, firstname, phone, notes)) {

                updateModel(Registry.getInstance().getCustomerRegistry().getCustomers());
                JOptionPane.showMessageDialog(null, "New customer created successfully");
              } else {
                SwingTools.showErrorDialog("Customer with this name already exists!");
              }

            } else {
              String errorMessage =
                  "Enter at least name fields!\nField lengths:\n"
                      + "Names: 1 - "
                      + CustomerRegistry.MAX_NAME_LENGTH
                      + ",\n"
                      + "Phone: 0, "
                      + CustomerRegistry.PHONE_LENGTH
                      + ",\n"
                      + "Notes: 0 -"
                      + CustomerRegistry.MAX_NOTES_LENGTH;
              SwingTools.showErrorDialog(errorMessage);
            }
            ApplicationFrame.checkUnsavedChanges();
          }
        });

    btnNewSale.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {

            int row = customersTable.getSelectedRow();
            if (customersTableModel.getRowCount() != 0 && row != -1) {
              String id = (String) customersTableModel.getValueAt(row, 0);

              Customer c = Registry.getInstance().getCustomerRegistry().getCustomerById(id);

              ApplicationFrame.showNewSaleTabByCustomer(c);
            }
          }
        });

    btnShowSales.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {

            int row = customersTable.getSelectedRow();
            if (customersTableModel.getRowCount() != 0 && row != -1) {
              String id = (String) customersTableModel.getValueAt(row, 0);

              List<Sale> salesByCustomer =
                  Registry.getInstance().getSaleRegistry().getSalesByCustomerId(id);

              ApplicationFrame.showSalesCardByList(salesByCustomer);
            }
          }
        });

    btnEditCustomer.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {

            int row = customersTable.getSelectedRow();
            if (customersTableModel.getRowCount() != 0 && row != -1) {
              String id = (String) customersTableModel.getValueAt(row, 0);

              Customer customerToEdit =
                  Registry.getInstance().getCustomerRegistry().getCustomerById(id);

              JTextField textNewLastName = new JTextField(customerToEdit.getLastName());
              JTextField textNewFirstName = new JTextField(customerToEdit.getFirstName());
              JTextField textNewPhone = new JTextField(customerToEdit.getPhone());
              JTextField textNewNotes = new JTextField(customerToEdit.getNotes());

              Object[] message = {
                "Last Name:", textNewLastName,
                "First Name:", textNewFirstName,
                "Phone: ", textNewPhone,
                "Notes: ", textNewNotes
              };

              int option =
                  JOptionPane.showConfirmDialog(
                      null, message, "Edit Customer", JOptionPane.OK_CANCEL_OPTION);

              if (option == JOptionPane.OK_OPTION) {

                String newLast = textNewLastName.getText().trim();
                String newFirst = textNewFirstName.getText().trim();
                String newPhone = textNewPhone.getText().trim();
                String newNotes = textNewNotes.getText().trim();

                if (CustomerRegistry.isCustomerCorrectFormat(
                    newLast, newFirst, newPhone, newNotes)) {
                  if (Registry.getInstance()
                      .getCustomerRegistry()
                      .updateCustomer(id, newLast, newFirst, newPhone, newNotes)) {
                    updateModel(Registry.getInstance().getCustomerRegistry().getCustomers());
                    JOptionPane.showMessageDialog(null, "Customer info updated successfully!");
                  } else {
                    SwingTools.showErrorDialog("There is another customer with this name!");
                  }
                } else {
                  String errorMessage =
                      "Enter at least name fields!\nMax characters:\n"
                          + "Names: "
                          + CustomerRegistry.MAX_NAME_LENGTH
                          + ", "
                          + "Notes: "
                          + CustomerRegistry.MAX_NOTES_LENGTH;
                  SwingTools.showErrorDialog(errorMessage);
                }
              }
            }
            ApplicationFrame.checkUnsavedChanges();
          }
        });

    btnDeleteCustomer.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {

            int row = customersTable.getSelectedRow();
            if (customersTableModel.getRowCount() != 0 && row != -1) {
              int s =
                  JOptionPane.showConfirmDialog(
                      null,
                      "Are you sure you want to delete customer?\nDeleting customer will delete all\nsales containing the customer!",
                      "Delete Customer",
                      JOptionPane.YES_NO_OPTION,
                      JOptionPane.WARNING_MESSAGE);
              if (s == 0) {
                String id = (String) customersTableModel.getValueAt(row, 0);

                Registry.getInstance().getCustomerRegistry().deleteCustomerByID(id);
                updateModel(Registry.getInstance().getCustomerRegistry().getCustomers());

                JOptionPane.showMessageDialog(null, "Customer deleted successfully!");
              }
            }
            ApplicationFrame.checkUnsavedChanges();
          }
        });
  }

  /**
   * Fills the table with specified customers.
   *
   * @param foundCustomers
   */
  public void updateModel(List<Customer> foundCustomers) {

    textLastName.setText("");
    textFirstName.setText("");
    textPhone.setText("");
    textNotes.setText("");
    customersTableModel.setRowCount(0);

    for (Customer customer : foundCustomers) {

      customersTableModel.addRow(
          new Object[] {
            customer.getId(),
            customer.getLastName(),
            customer.getFirstName(),
            customer.getPhone(),
            customer.getNotes(),
            customer.getFirstSaleDate(),
            customer.getLastSaleDate(),
            String.format("%.1f", customer.getTotalRevenue()),
            customer.getTotalSales()
          });
    }
    customersTableModel.fireTableDataChanged();
    SwingTools.resizeColumns(customersTable);
  }
}
