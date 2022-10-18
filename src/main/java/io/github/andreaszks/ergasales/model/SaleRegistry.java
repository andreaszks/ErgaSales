package io.github.andreaszks.ergasales.model;

import io.github.andreaszks.ergasales.tools.IDTools;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/** Creates and manages sales in the Registry. */
@XmlRootElement(name = "sales")
@XmlAccessorType(XmlAccessType.FIELD)
public class SaleRegistry implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final int MAX_NOTES_LENGTH = 300;

  @XmlElement(name = "sale")
  private ArrayList<Sale> sales = new ArrayList<>();

  SaleRegistry() {
    sales = new ArrayList<>();
  }

  /**
   * Returns a list of all sales in the Registry.
   *
   * @return a list of all sales in the Registry
   */
  public List<Sale> getSales() {
    // Applies to  getCustomers, getProducts, getSales: Return a clone that has the original
    // objects to prevent modification of the original list.
    // Example: if you return the original list, one can execute remove(), add() etc without
    // updating the required objects and fields.
    // Scenario it solves: by removing a sale outside SaleRegistry#removeSaleById(), customer
    // and products will not update quantities, dates etc.
    ArrayList<Sale> salesClone = new ArrayList<>();
    salesClone.addAll(sales);
    return salesClone;
  }

  /**
   * Creates a sale and adds it to the Registry.
   *
   * <p>The sale is created iff the fields are in correct format and there is available ID to
   * assign.
   *
   * <p>The customer of the sale cannot be changed once the sale is added to the Registry.
   *
   * @see SaleRegistry#isSaleCorrectFormat(LocalDate, String, float, List)
   * @param date
   * @param customerId
   * @param productsUsed
   * @param notes
   * @param price
   * @return true if sale is created and added to the Registry, false if not
   */
  public boolean createSale(
      LocalDate date,
      String customerId,
      List<ProductQuantityPair> productsUsed,
      String notes,
      float price) {

    Customer customer = Registry.getInstance().getCustomerRegistry().getCustomerById(customerId);
    if (customer == null) return false;
    if (isSaleCorrectFormat(date, notes, price, productsUsed)) {
      String id = IDTools.generateUniqueId();
      if (id != null) {
        Sale s = new Sale(date, customer, productsUsed, notes.trim(), price);
        s.setId(id);
        sales.add(s);

        customer.setTotalRevenue(customer.getTotalRevenue() + price);
        customer.setTotalSales(customer.getTotalSales() + 1);

        for (ProductQuantityPair pqp : productsUsed) {
          pqp.getProduct().setTotalSales(pqp.getProduct().getTotalSales() + 1);
          pqp.getProduct()
              .setTotalQuantity(pqp.getProduct().getTotalQuantity() + pqp.getQuantity());
        }

        updateCustomerDates(customer);
      } else return false;
    } else return false;

    return true;
  }

  /**
   * Updates the sale to the new values.
   *
   * <p>The sale is updated iff there is a sale with this ID and the new values are in correct
   * format.
   *
   * @see SaleRegistry#isSaleCorrectFormat(LocalDate, String, float, List)
   * @param id
   * @param newDate
   * @param newPrice
   * @param newNotes
   * @param newProductsUsed
   * @return true if sale is updated, false if no sale has this ID or fields are incorrect
   */
  public boolean updateSale(
      String id,
      LocalDate newDate,
      float newPrice,
      String newNotes,
      List<ProductQuantityPair> newProductsUsed) {

    Sale sale = getSaleById(id);
    if (sale == null) return false;
    if (!isSaleCorrectFormat(newDate, newNotes, newPrice, newProductsUsed)) return false;

    sale.setNotes(newNotes);
    sale.setDate(newDate);
    updateCustomerDates(sale.getCustomer());

    sale.getCustomer()
        .setTotalRevenue(sale.getCustomer().getTotalRevenue() + (newPrice - sale.getPrice()));

    sale.setPrice(newPrice);

    // update products (total sales and quantities)
    for (ProductQuantityPair opqp : sale.getProductsUsed()) {
      opqp.getProduct().setTotalSales(opqp.getProduct().getTotalSales() - 1);
      opqp.getProduct().setTotalQuantity(opqp.getProduct().getTotalQuantity() - opqp.getQuantity());
    }
    sale.getProductsUsed().clear();

    for (ProductQuantityPair npqp : newProductsUsed) {
      sale.getProductsUsed().add(npqp);
      npqp.getProduct().setTotalSales(npqp.getProduct().getTotalSales() + 1);
      npqp.getProduct().setTotalQuantity(npqp.getProduct().getTotalQuantity() + npqp.getQuantity());
    }
    return true;
  }

  /**
   * Deletes sale with specified ID.
   *
   * <p>Deleting a sale also updates Customer and Product statistics such as:<br>
   * Customer dates, total sales and revenue and Product total sales and quantities.
   *
   * @param id
   * @return true if sale is deleted, false if no sale is found with this ID
   */
  public boolean deleteSaleById(String id) {
    if (id == null) return false;
    Sale sale = getSaleById(id);
    if (sale == null) return false;

    Customer customer = sale.getCustomer();

    for (ProductQuantityPair pqp : sale.getProductsUsed()) {
      Product p = pqp.getProduct();
      int quantity = pqp.getQuantity();

      p.setTotalSales(p.getTotalSales() - 1);
      p.setTotalQuantity(p.getTotalQuantity() - quantity);
    }

    customer.setTotalSales(customer.getTotalSales() - 1);
    customer.setTotalRevenue(customer.getTotalRevenue() - sale.getPrice());

    sales.remove(sale);

    updateCustomerDates(customer);

    return true;
  }

  void deleteSalesByCustomerId(String custId) {

    ArrayList<Sale> salesToRemove = new ArrayList<>();

    for (Sale sale : sales) {
      if (sale.getCustomer().getId().contentEquals(custId)) {
        salesToRemove.add(sale);
      }
    }

    for (Sale sale : salesToRemove) {
      deleteSaleById(sale.getId());
    }
  }

  void deleteSalesByProductId(String prodId) {

    ArrayList<Sale> salesToRemove = new ArrayList<>();

    for (Sale sale : sales) {
      for (ProductQuantityPair pqp : sale.getProductsUsed()) {
        if (pqp.getProduct().getId().contentEquals(prodId) && !salesToRemove.contains(sale)) {
          salesToRemove.add(sale);
        }
      }
    }

    for (Sale sale : salesToRemove) {
      deleteSaleById(sale.getId());
    }
  }

  private void updateCustomerDates(Customer customer) {

    LocalDate first = LocalDate.MAX;
    LocalDate last = LocalDate.MIN;

    if (customer.getTotalSales() != 0) {

      for (Sale sale : sales) {
        if (sale.getCustomer().getId().equals(customer.getId())) {
          if (sale.getDate().isBefore(first)) first = sale.getDate();
          if (sale.getDate().isAfter(last)) last = sale.getDate();
        }
      }

      customer.setFirstSaleDate(first);
      customer.setLastSaleDate(last);

    } else {
      customer.setFirstSaleDate(null);
      customer.setLastSaleDate(null);
    }
  }

  /**
   * Returns the sale with specified ID.
   *
   * @param id
   * @return the sale with specified ID or null if no sale is found
   */
  public Sale getSaleById(String id) {
    if (id == null) return null;
    for (Sale sale : sales) {
      if (sale.getId().contentEquals(id)) {
        return sale;
      }
    }
    return null;
  }

  /**
   * Returns a list of sales that contain the Product of specified ID.
   *
   * @param id
   * @return the sales that contain the Product of specified ID or an empty list if no sales are
   *     found
   */
  public List<Sale> getSalesByProductId(String id) {
    ArrayList<Sale> salesByProduct = new ArrayList<>();
    if (id == null) return salesByProduct;

    for (Sale sale : sales) {
      for (int j = 0; j < sale.getProductsUsed().size(); j++) {
        if (sale.getProductsUsed().get(j).getProduct().getId().contentEquals(id)) {
          salesByProduct.add(sale);
        }
      }
    }
    return salesByProduct;
  }

  /**
   * Returns a list of sales that contain the Customer of specified ID.
   *
   * @param id
   * @return the sales that contain the Customer of specified ID or an empty list if no sales are
   *     found
   */
  public List<Sale> getSalesByCustomerId(String id) {
    ArrayList<Sale> salesByCustomer = new ArrayList<>();
    if (id == null) return salesByCustomer;

    for (Sale sale : sales) {
      if (sale.getCustomer().getId().contentEquals(id)) salesByCustomer.add(sale);
    }
    return salesByCustomer;
  }

  /**
   * Returns a list of sales that contain the specified term.
   *
   * <p>The term may be everything related to the sale such as:<br>
   * customer info (id, last/first name, phone or notes),<br>
   * product info (id, name or type),<br>
   * sale info (date, notes, price, id)
   *
   * @param term
   * @return a list of sales that contain the specified term or an empty list if none are found
   */
  public List<Sale> getSalesByTerm(String term) {
    ArrayList<Sale> salesByTerm = new ArrayList<>();
    if (term == null) return salesByTerm;

    String[] splittedTerm = term.split("\\s+");

    for (Sale s : sales) {
      for (int i = 0; i < splittedTerm.length; i++) {
        if (s.toString().toUpperCase().contains(splittedTerm[i].toUpperCase())
            && !salesByTerm.contains(s)) {
          salesByTerm.add(s);
        }
      }
    }

    return salesByTerm;
  }

  /**
   * Returns the revenue made in specified date
   *
   * @param date
   * @return the revenue made in specified date
   */
  public float getRevenueOfDate(LocalDate date) {
    if (date == null) return 0;
    float totalRevenue = 0;
    for (Sale sale : sales) {
      if (sale.getDate().toString().contentEquals(date.toString())) totalRevenue += sale.getPrice();
    }
    return totalRevenue;
  }

  /**
   * Returns the revenue made up until specified date.<br>
   * Includes the specified date.
   *
   * @param date
   * @return
   */
  public float getRevenueToDate(LocalDate date) {
    if (date == null) return 0;
    float totalRevenue = 0;
    for (Sale sale : sales) {
      if (sale.getDate().isBefore(date) || sale.getDate().isEqual(date))
        totalRevenue += sale.getPrice();
    }
    return totalRevenue;
  }

  /**
   * Checks if Sale fields are in correct format.
   *
   * <p>For the fields to be correct,<br>
   * 1)they should not be null,<br>
   * 2)notes must be within correct length and without leading or trailing whitespace,<br>
   * 3)price should be zero or positive,<br>
   * 4)there should be at least 1 product and all quantities must be positive
   *
   * @see #MAX_NOTES_LENGTH
   * @param date
   * @param notes
   * @param price
   * @param productsUsed
   * @return true is sale is in correct format
   */
  boolean isSaleCorrectFormat(
      LocalDate date, String notes, float price, List<ProductQuantityPair> productsUsed) {

    if (date == null
        || notes == null
        || notes.length() > MAX_NOTES_LENGTH
        || price < 0
        || productsUsed == null) return false;
    // whitespace is trimmed when taking input through UI but there may be in XML file.
    if (!notes.equals(notes.trim())) return false;
    if (productsUsed.isEmpty()) return false;
    for (ProductQuantityPair pqp : productsUsed) {
      if (pqp.getProduct() == null) return false;
      if (Registry.getInstance().getProductRegistry().getProductById(pqp.getProduct().getId())
          == null) return false; // product doesn't exist in Registry
      if (pqp.getQuantity() < 1) return false;
    }
    return true;
  }

  /** Loops through sales and update Customer and Product statistics (total sales, revenue...) */
  public boolean updateStatistics() {

    // The 2 FORs just in case it is called in other place other than XML import.
    // This method would better seat in FileToolsXml class because it is called only from there
    // at this point, but being here
    // it provides a layer of security because methods such as Customer#setTotalSales are
    // package private and are meant to be called
    // only when you call a Registry method such as SaleRegistry#add, so all other necessary
    // fields are changed simultaneously
    for (Customer c : Registry.getInstance().getCustomerRegistry().getCustomers()) {
      c.setFirstSaleDate(null);
      c.setLastSaleDate(null);
      c.setTotalRevenue(0);
      c.setTotalSales(0);
    }
    for (Product p : Registry.getInstance().getProductRegistry().getProducts()) {
      p.setTotalQuantity(0);
      p.setTotalSales(0);
    }

    try {

      for (Sale sale : Registry.getInstance().getSaleRegistry().getSales()) {
        sale.getCustomer().setTotalSales(sale.getCustomer().getTotalSales() + 1);
        sale.getCustomer().setTotalRevenue(sale.getCustomer().getTotalRevenue() + sale.getPrice());

        for (ProductQuantityPair pqp : sale.getProductsUsed()) {
          if (pqp.getQuantity() < 1) return false;
          pqp.getProduct().setTotalSales(pqp.getProduct().getTotalSales() + 1);
          pqp.getProduct()
              .setTotalQuantity(pqp.getProduct().getTotalQuantity() + pqp.getQuantity());
        }
      }

      for (Customer c : Registry.getInstance().getCustomerRegistry().getCustomers()) {
        updateCustomerDates(c);
      }

      return true;

    } catch (Exception e) {
      // most likely a NullPointer (value missing on XML Registry file)
      return false;
    }
  }

  void clearRegistry() {
    sales.clear();
  }
}
