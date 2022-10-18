package io.github.andreaszks.ergasales.app;

import io.github.andreaszks.ergasales.model.Customer;
import io.github.andreaszks.ergasales.model.CustomerRegistry;
import io.github.andreaszks.ergasales.model.ProductRegistry;
import io.github.andreaszks.ergasales.model.Registry;
import io.github.andreaszks.ergasales.model.Sale;
import io.github.andreaszks.ergasales.model.SaleRegistry;
import io.github.andreaszks.ergasales.tools.FileToolsSer;
import io.github.andreaszks.ergasales.tools.FileToolsTxt;
import io.github.andreaszks.ergasales.tools.FileToolsXml;
import io.github.andreaszks.ergasales.tools.SwingTools;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** Main window of the application. This is where all panels are contained. */
public class ApplicationFrame extends JFrame {
  private static final long serialVersionUID = 1L;

  private JPanel contentPane;
  private static JTabbedPane panelTabs;
  private static CustomersPanel customersPanel;
  private static ProductsPanel productsPanel;
  private static JPanel panelCards;
  private static CardLayout clCards;
  private static SalesPanel salesPanel;
  private static EditSalePanel editSalePanel;
  private static NewSalePanel newSalePanel;

  private JMenuBar menuBar;
  private JMenu menuFile;
  private JMenuItem itemFileNewInstance;
  private JMenu menuFileOpen;
  private JMenuItem itemFileOpenXml;
  private JMenuItem itemFileSave;
  private JMenu menuFileSaveAs;
  private JMenuItem itemFileSaveAsXml;
  private JMenuItem itemFileExit;
  private JMenu menuHelp;
  private JMenuItem itemHelpAbout;
  private JMenu menuFileExport;
  private JMenu menuFileExportSalesSummary;
  private JMenuItem itemFileExportSalesSummaryXml;
  private JMenuItem itemFileExportSalesSummaryTxt;
  private JMenuItem itemHelpFields;

  private static boolean comingFromTab;

  /** Launch the application. */
  public static void main(String[] args) {
    EventQueue.invokeLater(
        new Runnable() {
          public void run() {
            try {
              UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
              FileToolsSer.importRegistryFromFile();
              comingFromTab = true;
              ApplicationFrame frame = new ApplicationFrame();
              frame.setVisible(true);
            } catch (Exception e) {
              e.printStackTrace();
              System.exit(1);
            }
          }
        });
  }

  /** Create the frame. */
  public ApplicationFrame() {

    setMinimumSize(new Dimension(1020, 600));
    setTitle("ErgaSales");

    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    setBounds(100, 100, 529, 320);

    // Menu bar
    initMenuComponents();

    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    setContentPane(contentPane);

    panelTabs = new JTabbedPane();

    panelCards = new JPanel();
    panelCards.setLayout(new CardLayout(0, 0));
    clCards = (CardLayout) (panelCards.getLayout());

    customersPanel = new CustomersPanel();
    productsPanel = new ProductsPanel();
    salesPanel = new SalesPanel();
    editSalePanel = new EditSalePanel();
    newSalePanel = new NewSalePanel();

    initListeners();

    panelCards.add(salesPanel, "sales");
    panelCards.add(editSalePanel, "editSale");

    panelTabs.add(customersPanel, "Customers");
    panelTabs.add(productsPanel, "Products");
    panelTabs.add(panelCards, "Sales");
    panelTabs.add(newSalePanel, "New Sale");

    contentPane.add(panelTabs);
  }

  private void initMenuComponents() {
    menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    // MENU ITEMS FILE
    menuFile = new JMenu("File");

    itemFileNewInstance = new JMenuItem("New Instance");
    menuFileOpen = new JMenu("Open");
    itemFileOpenXml = new JMenuItem("XML");
    // Separator
    itemFileSave = new JMenuItem("Save");
    menuFileSaveAs = new JMenu("Save As");
    itemFileSaveAsXml = new JMenuItem("XML");
    // Separator
    menuFileExport = new JMenu("Export");
    menuFileExportSalesSummary = new JMenu("Sales Summary");
    itemFileExportSalesSummaryXml = new JMenuItem("XML");
    itemFileExportSalesSummaryTxt = new JMenuItem("TXT");
    // Separator
    itemFileExit = new JMenuItem("Exit");

    menuFile.add(itemFileNewInstance);
    menuFile.add(menuFileOpen);
    menuFileOpen.add(itemFileOpenXml);
    menuFile.addSeparator();
    menuFile.add(itemFileSave);
    menuFile.add(menuFileSaveAs);
    menuFileSaveAs.add(itemFileSaveAsXml);
    menuFile.addSeparator();
    menuFile.add(menuFileExport);
    menuFileExport.add(menuFileExportSalesSummary);
    menuFileExportSalesSummary.add(itemFileExportSalesSummaryXml);
    menuFileExportSalesSummary.add(itemFileExportSalesSummaryTxt);
    menuFile.addSeparator();
    menuFile.add(itemFileExit);

    // MENU ITEMS HELP
    menuHelp = new JMenu("Help");

    itemHelpFields = new JMenuItem("Fields");
    // Separator
    itemHelpAbout = new JMenuItem("About");
    KeyStroke ctrlH =
        KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    itemHelpAbout.setAccelerator(ctrlH);

    menuHelp.add(itemHelpFields);
    menuHelp.addSeparator();
    menuHelp.add(itemHelpAbout);

    menuBar.add(menuFile);
    menuBar.add(menuHelp);
  }

  private void initListeners() {

    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent arg0) {

            if (FileToolsSer.registryHasUnsavedChanges()) {

              int dialogResult =
                  JOptionPane.showConfirmDialog(
                      null,
                      "Do you want to save changes?",
                      "ErgaSales",
                      JOptionPane.YES_NO_CANCEL_OPTION);

              if (dialogResult == JOptionPane.YES_OPTION) {
                FileToolsSer.exportRegistryToFile();
                System.exit(0);
              } else if (dialogResult == JOptionPane.NO_OPTION) {
                System.exit(0);
              }

            } else System.exit(0);
          }
        });

    itemFileNewInstance.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            int s =
                JOptionPane.showConfirmDialog(
                    null,
                    "Erase ALL data from the running instance.\nAre you sure you want to erase all data?",
                    "New Instance",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (s == 0) {
              Registry.clearInstance();
              panelTabs.setSelectedIndex(1);
              panelTabs.setSelectedIndex(0);
            }
          }
        });

    itemFileOpenXml.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            if (FileToolsSer.registryHasUnsavedChanges()) {
              int dialogResult =
                  JOptionPane.showConfirmDialog(
                      null,
                      "Registry has unsaved changed!\nDo you want to save before importing Registry from XML File?",
                      "ErgaSales XML Import",
                      JOptionPane.YES_NO_CANCEL_OPTION);

              if (dialogResult == JOptionPane.YES_OPTION) {
                FileToolsSer.exportRegistryToFile();
              }
            }
            File file = SwingTools.showImportFileChooser("xml");

            if (FileToolsXml.importRegistryFromXml(file)) {
              panelTabs.setSelectedIndex(1);
              panelTabs.setSelectedIndex(0);
            }
          }
        });

    itemFileSave.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (FileToolsSer.registryHasUnsavedChanges()) {
              FileToolsSer.exportRegistryToFile();
            }
          }
        });

    itemFileSaveAsXml.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            File file = SwingTools.showExportFileChooser("xml");
            if (file != null) FileToolsXml.exportRegistryToXml(file);
          }
        });

    itemFileExportSalesSummaryXml.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            FileToolsXml.exportSalesSummaryXml();
          }
        });

    itemFileExportSalesSummaryTxt.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            FileToolsTxt.exportSalesSummaryTxt();
          }
        });

    itemFileExit.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            ApplicationFrame.this.dispatchEvent(
                new WindowEvent(ApplicationFrame.this, WindowEvent.WINDOW_CLOSING));
          }
        });

    itemHelpFields.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {

            String s =
                "Field lengths:\n"
                    + "Customer:\n    Names: 1-%s\n    Phone: 0,%s\n    Notes: 0-%s\n"
                    + "Product:\n    Name: 1-%s\n    Type: 1-%s\n"
                    + "Sale:\n    Notes: 0-%s";

            s =
                String.format(
                    s,
                    CustomerRegistry.MAX_NAME_LENGTH,
                    CustomerRegistry.PHONE_LENGTH,
                    CustomerRegistry.MAX_NOTES_LENGTH,
                    ProductRegistry.MAX_PRODUCT_NAME_LENGTH,
                    ProductRegistry.MAX_PRODUCT_TYPE_LENGTH,
                    SaleRegistry.MAX_NOTES_LENGTH);

            JOptionPane.showMessageDialog(null, s, "Fields", JOptionPane.INFORMATION_MESSAGE);
          }
        });

    itemHelpAbout.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {

            JLabel link1 = new JLabel("<html><a href=''>andreaszks</a></html>");
            JLabel link2 = new JLabel("<html><a href=''>ErgaSales</a></html>");

            MouseAdapter ma =
                new MouseAdapter() {
                  @Override
                  public void mouseClicked(MouseEvent e) {
                    String[] links = {
                      "https://github.com/andreaszks", "https://github.com/andreaszks/ergasales"
                    };

                    try {
                      if (e.getSource().equals(link1))
                        Desktop.getDesktop().browse(new URI(links[0]));
                      else if (e.getSource().equals(link2))
                        Desktop.getDesktop().browse(new URI(links[1]));

                    } catch (IOException | URISyntaxException e1) {
                      e1.printStackTrace();
                    }
                  }
                };

            link1.addMouseListener(ma);
            link2.addMouseListener(ma);

            Object[] message = {"Project by", link1, "Project Repository (GitHub):", link2};
            JOptionPane.showMessageDialog(null, message);
          }
        });

    panelTabs.addChangeListener(
        new ChangeListener() {
          public void stateChanged(ChangeEvent arg0) {
            // Tab Index
            // 0 Customers
            // 1 Products
            // 2 Sales (salesPanel,editSalePanel) (panelCards)
            // 3 New Sale

            // comingFromTab is a flag to differentiate changing tabs from tab menu or
            // through a method
            // If coming from tab menu, execute the default action (example: when
            // changing to newSalePanel, load all customers from registry)
            // If coming from a method, deactivate comingFromTab flag so when changing
            // the tab it won't execute the default action, example:
            // showNewSaleTabByCustomer method will load newSalePanel with a specific
            // customer instead of the default all customers

            if (panelTabs.getSelectedIndex() == 0) {
              customersPanel.updateModel(
                  Registry.getInstance().getCustomerRegistry().getCustomers());
            } else if (panelTabs.getSelectedIndex() == 1) {
              productsPanel.updateModel(Registry.getInstance().getProductRegistry().getProducts());
            } else if (panelTabs.getSelectedIndex() == 2 && comingFromTab) {
              salesPanel.updateModel(Registry.getInstance().getSaleRegistry().getSales());
              clCards.show(panelCards, "sales");
            } else if (panelTabs.getSelectedIndex() == 3 && comingFromTab) {
              newSalePanel.clearFields();
              newSalePanel.updateCustomersModel(
                  Registry.getInstance().getCustomerRegistry().getCustomers());
            }
          }
        });
  }

  /** Shows the Sales card with all sales from Registry. */
  public static void showSalesCard() {
    panelTabs.setSelectedIndex(1); // switch to other tab so stateChanged listener activates
    panelTabs.setSelectedIndex(2);
  }

  /**
   * Shows the Sales card with only specified sales being shown.
   *
   * @param salesByCustomer
   */
  public static void showSalesCardByList(List<Sale> salesByCustomer) {
    comingFromTab = false;
    salesPanel.updateModel(salesByCustomer);
    clCards.show(panelCards, "sales");
    panelTabs.setSelectedIndex(2);
    comingFromTab = true;
  }

  /**
   * Shows the Edit Sale card with the sale to edit.
   *
   * @param sale
   */
  public static void showEditSaleCard(Sale sale) {
    editSalePanel.setSaleToEdit(sale);
    clCards.show(panelCards, "editSale");
  }

  /**
   * Switch to New Sale tab with specified customer selected as default customer in the comboBox.
   *
   * @param c
   */
  public static void showNewSaleTabByCustomer(Customer c) {
    comingFromTab = false;
    newSalePanel.clearFields();
    newSalePanel.updateCustomersModel(c);
    panelTabs.setSelectedIndex(3);
    comingFromTab = true;
  }
}
