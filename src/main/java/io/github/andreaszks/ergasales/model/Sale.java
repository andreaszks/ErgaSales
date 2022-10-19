package io.github.andreaszks.ergasales.model;

import io.github.andreaszks.ergasales.tools.IDTools;
import io.github.threetenjaxb.core.LocalDateXmlAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * This class represents a Sale. Sales are managed through SaleRegistry
 *
 * <p>A sale is created only through {@link SaleRegistry#createSale(LocalDate, String, List, String,
 * float)} <br>
 * and is only updated through {@link SaleRegistry#updateSale(String, LocalDate, float, String,
 * List)}
 */
@XmlRootElement(name = "sale")
@XmlAccessorType(XmlAccessType.FIELD)
public class Sale implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  @XmlJavaTypeAdapter(value = LocalDateXmlAdapter.class)
  private LocalDate date;

  private String notes;
  private float price;

  @XmlIDREF private Customer customer;

  @XmlElement(name = "productQuantityPair")
  @XmlElementWrapper(name = "productsUsed")
  private ArrayList<ProductQuantityPair> productsUsed;

  Sale(
      LocalDate date,
      Customer customer,
      List<ProductQuantityPair> productsUsed,
      String notes,
      float price) {
    super();
    this.date = date;
    this.customer = customer;
    this.productsUsed = new ArrayList<>(productsUsed);
    this.notes = notes.trim();
    this.price = price;
  }

  Sale() {}

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(productsUsed.size());
    for (ProductQuantityPair pqp : productsUsed) {
      builder.append(pqp.getQuantity() + pqp.getProduct().toString());
    }
    String productsString = builder.toString();

    return date + customer.toString() + productsString + notes + price + id;
  }

  /**
   * Returns the ID of the sale.
   *
   * @return the ID of the sale.
   */
  public String getId() {
    return id;
  }
  /**
   * Sets a new ID if a sale from XML File doesn't have one. Does nothing if ID is already set.
   *
   * @return true if sale gets a new ID, false if there are no available IDs or ID already set.
   */
  public boolean setId() {
    if (this.id != null) return false;
    String newId = IDTools.generateUniqueId();
    if (newId == null) return false;
    this.id = newId;
    return true;
  }
  /**
   * Returns the date of the sale.
   *
   * @return
   */
  public LocalDate getDate() {
    return date;
  }
  /**
   * Returns the customer of the sale.
   *
   * @return the customer of the sale
   */
  public Customer getCustomer() {
    return customer;
  }
  /**
   * Returns the products that are used in the sale.
   *
   * @return the products that are used in the sale
   */
  public List<ProductQuantityPair> getProductsUsed() {
    return productsUsed;
  }
  /**
   * Returns the notes of the sale.
   *
   * @return the notes of the sale
   */
  public String getNotes() {
    return notes;
  }
  /**
   * Returns the price of the sale.
   *
   * @return the price of the sale
   */
  public float getPrice() {
    return price;
  }

  void setId(String string) {
    this.id = string;
  }

  void setDate(LocalDate date) {
    this.date = date;
  }

  void setProductsUsed(ArrayList<ProductQuantityPair> productsUsed) {
    this.productsUsed = productsUsed;
  }

  void setNotes(String notes) {
    this.notes = notes;
  }

  void setPrice(float price) {
    this.price = price;
  }
}
