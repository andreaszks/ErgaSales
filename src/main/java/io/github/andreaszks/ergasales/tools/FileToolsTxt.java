package io.github.andreaszks.ergasales.tools;

import io.github.andreaszks.ergasales.model.ProductQuantityPair;
import io.github.andreaszks.ergasales.model.Registry;
import io.github.andreaszks.ergasales.model.Sale;
import java.io.File;

/** Contains methods that export different aspects of the Registry in TXT format. */
public abstract class FileToolsTxt extends FileTools {

  /** Creates a summary of Sales in TXT format and exports it to selected file. */
  public static void exportSalesSummaryTxt() {

    StringBuilder sb = new StringBuilder();

    for (Sale s : Registry.getInstance().getSaleRegistry().getSales()) {
      sb.append(s.getDate().toString() + "\n");
      sb.append(
          "    customer: "
              + s.getCustomer().getLastName()
              + " "
              + s.getCustomer().getFirstName()
              + "\n");
      sb.append("    products:\n");
      for (ProductQuantityPair pqp : s.getProductsUsed()) {
        sb.append(
            "        "
                + pqp.getQuantity()
                + "x "
                + pqp.getProduct().getName()
                + " ("
                + pqp.getProduct().getType()
                + ")\n");
      }
      sb.append("    notes: " + s.getNotes() + "\n");
      sb.append("    price: " + s.getPrice() + "\n");
    }

    String output = sb.toString();

    File f = SwingTools.showExportFileChooser("txt");
    if (f != null) writeDataToFile(f, output);
  }
}
