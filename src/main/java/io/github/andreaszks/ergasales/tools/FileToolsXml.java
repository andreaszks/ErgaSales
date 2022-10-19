package io.github.andreaszks.ergasales.tools;

import io.github.andreaszks.ergasales.model.Customer;
import io.github.andreaszks.ergasales.model.CustomerRegistry;
import io.github.andreaszks.ergasales.model.Product;
import io.github.andreaszks.ergasales.model.ProductQuantityPair;
import io.github.andreaszks.ergasales.model.ProductRegistry;
import io.github.andreaszks.ergasales.model.Registry;
import io.github.andreaszks.ergasales.model.Sale;
import io.github.andreaszks.ergasales.model.SaleRegistry;
import java.io.File;
import java.io.StringWriter;
import java.util.HashSet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.text.StringEscapeUtils;

/**
 * Contains methods that handle the Import and Export of different aspects of Registry in XML
 * format.
 */
public abstract class FileToolsXml extends FileTools {

  // This class was firstly created to export the saved registry to other format so i can
  // refactor the class fields without losing the data from the serialized file due to
  // ClassNotFoundException. I could do it with SQL but XML is something that can be used by the end
  // user too, so i kept it as a feature.

  private static HashSet<String> ids = new HashSet<>();

  /** Creates a summary of Sales in XML format and exports it to selected file. */
  public static void exportSalesSummaryXml() {

    StringBuilder sb = new StringBuilder();
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
    sb.append("<sales>\n");

    for (Sale s : Registry.getInstance().getSaleRegistry().getSales()) {

      String customerName =
          StringEscapeUtils.escapeXml10(
              s.getCustomer().getLastName() + " " + s.getCustomer().getFirstName());
      String notes = StringEscapeUtils.escapeXml10(s.getNotes());

      sb.append("    <sale>\n");
      sb.append("        <date>" + s.getDate().toString() + "</date>\n");
      sb.append("        <customer>" + customerName + "</customer>\n");
      sb.append("        <notes>" + notes + "</notes>\n");
      sb.append("        <price>" + s.getPrice() + "</price>\n");
      sb.append("        <products>\n");

      for (ProductQuantityPair pqp : s.getProductsUsed()) {
        sb.append("            <pair>\n");
        String productName = StringEscapeUtils.escapeXml10(pqp.getProduct().getName());
        String productType = StringEscapeUtils.escapeXml10(pqp.getProduct().getType());
        sb.append("                <product>" + productName + " (" + productType + ")</product>\n");
        sb.append("                <quantity>" + pqp.getQuantity() + "</quantity>\n");
        sb.append("            </pair>\n");
      }

      sb.append("        </products>\n");
      sb.append("    </sale>\n");
    }

    sb.append("</sales>");

    File f = SwingTools.showExportFileChooser("xml");
    if (f != null) writeDataToFile(f, sb.toString());
  }

  /** Exports the Registry in XML format on selected file. */
  public static void exportRegistryToXml(File file) {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Registry.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      StringWriter sw = new StringWriter();
      jaxbMarshaller.marshal(Registry.getInstance(), sw);

      if (file != null) writeDataToFile(file, sw.toString());

    } catch (JAXBException e) {
      e.printStackTrace();
    }
  }

  /**
   * Imports a Registry saved in XML file using exportRegistryToXml() method and sets it as the
   * Instance.
   *
   * <p>If registry fails to import, the Registry instance resets to the registry from the
   * serialized file.
   *
   * @see #exportRegistryToXml(File)
   * @param file
   * @return true if registry from XML file is imported, false if not
   */
  public static boolean importRegistryFromXml(File file) {
    if (file == null) return false;
    if (!readRegistryFromXml(file)) {
      FileToolsSer.importRegistryFromFile(); // if XML registry is incorrect make the Registry
      // instance to the registry from serialized file.
      return false;
    }
    return true;
  }

  private static boolean readRegistryFromXml(File file) {

    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Registry.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      Registry reg = (Registry) jaxbUnmarshaller.unmarshal(file);

      Registry.setInstance(reg);

      ids.clear();

      if (!checkCustomerFieldsFromXmlFile(reg)) return false;
      if (!checkProductFieldsFromXmlFile(reg)) return false;

      // Set the IDs to instance, if sale doesn't have an id, it will be created in the
      // instance.
      // If XML file has incorrect fields in sales, the IDs of the instance will reset to
      // those of the serialized file.
      IDTools.setIds(ids);

      return checkSaleFieldsFromXmlFile(reg);

    } catch (UnmarshalException e) {
      SwingTools.showXmlErrorDialog(
          "Something went wrong!\nPossible causes:\nField name corrupted");
      e.printStackTrace();
      return false;
    } catch (JAXBException e) {
      SwingTools.showXmlErrorDialog("Something went wrong reading XML file!");
      e.printStackTrace();
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      SwingTools.showXmlErrorDialog("Incorrect format, fields may be missing!");
      return false;
    }
  }

  private static boolean checkCustomerFieldsFromXmlFile(Registry reg) {
    for (Customer c : reg.getCustomerRegistry().getCustomers()) {
      if (!IDTools.isIdCorrectFormat(c.getId())) {
        if (c.getId() == null) {
          SwingTools.showErrorDialog("Customer is missing ID field");
          return false;
        }
        SwingTools.showXmlErrorDialog("Customer ID incorrect format!\nIncorrect ID: " + c.getId());
        return false;
      }
      if (!CustomerRegistry.isCustomerCorrectFormat(
          c.getLastName(), c.getFirstName(), c.getPhone(), c.getNotes())) {
        SwingTools.showXmlErrorDialog(
            "Customer "
                + c.getId()
                + " incorrect values!\nPossible causes: names/phone/notes missing or incorrect length");
        return false;
      }
      if (ids.contains(c.getId())) {
        SwingTools.showXmlErrorDialog(
            "Customers can't have the same ID!\nConflicting ID: " + c.getId());
        return false;
      }
      ids.add(c.getId());
    }
    return true;
  }

  private static boolean checkProductFieldsFromXmlFile(Registry reg) {
    for (Product p : reg.getProductRegistry().getProducts()) {
      if (!IDTools.isIdCorrectFormat(p.getId())) {
        if (p.getId() == null) {
          SwingTools.showXmlErrorDialog("Product is missing ID field");
          return false;
        }
        SwingTools.showXmlErrorDialog("Product ID incorrect format!\nIncorrect ID: " + p.getId());
        return false;
      }
      if (!ProductRegistry.isProductCorrectFormat(p.getName(), p.getType())) {
        SwingTools.showXmlErrorDialog(
            "Product "
                + p.getId()
                + " has incorrect values!\nPossible causes: "
                + "name/type missing,\nname/type exceeding character limit\"");
        return false;
      }
      if (ids.contains(p.getId())) {
        SwingTools.showXmlErrorDialog(
            "IDs must be unique across Customers, Products and Sales\nConflicting Product ID:"
                + p.getId());
        return false;
      }
      ids.add(p.getId());
    }
    return true;
  }

  private static boolean checkSaleFieldsFromXmlFile(Registry reg) {
    // Sales do not necessarily have IDs in XML files so it is easier to add more without
    // checking conflicting IDs
    for (Sale s : reg.getSaleRegistry().getSales()) {

      if (ids.contains(s.getId())) {
        SwingTools.showXmlErrorDialog(
            "IDs must be unique across Customers, Products and Sales\nConflicting Sale ID:"
                + s.getId());
        return false;
      }

      if (IDTools.isIdCorrectFormat(s.getId())) ids.add(s.getId());
      else {
        if (!s.setId()) {
          SwingTools.showXmlErrorDialog(
              "Cannot create ID for a sale that is missing an ID\nPossible cause: no more available IDs");
          return false;
        }
      }

      if (s.getCustomer() == null) {
        SwingTools.showXmlErrorDialog("Unknown Customer in sale " + s.getId());
        return false;
      }
      if (s.getPrice() < 0) {
        SwingTools.showXmlErrorDialog("Sale cannot have negative price!\nSale: " + s.getId());
        return false;
      }
      if (s.getNotes() == null) {
        SwingTools.showXmlErrorDialog("Sale " + s.getId() + " is missing notes field");
        return false;
      }
      if (s.getNotes().length() > SaleRegistry.MAX_NOTES_LENGTH) {
        SwingTools.showXmlErrorDialog(
            "Sale "
                + s.getId()
                + " exceeding notes character limit!\n"
                + "(Maximum characters: "
                + SaleRegistry.MAX_NOTES_LENGTH
                + ")");
        return false;
      }
      if (s.getDate() == null) {
        SwingTools.showXmlErrorDialog("Date incorrect in sale '" + s.getId() + "'");
        return false;
      }
    }

    // updateStatistics method loops through sales and update Customer and Product statistics
    // (total sales, revenue...)
    if (!reg.getSaleRegistry().updateStatistics()) {
      SwingTools.showXmlErrorDialog(
          "Sales fields wrong\nPossible causes:\n"
              + "Product doesn't exist,\nProduct Quantity incorrect");
      return false;
    }
    return true;
  }
}
