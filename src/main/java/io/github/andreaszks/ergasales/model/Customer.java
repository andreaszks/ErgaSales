package io.github.andreaszks.ergasales.model;

import java.io.Serializable;
import java.time.LocalDate;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This class represents a Customer. Customers are managed through CustomerRegistry.
 *
 * <p>A customer is created only through {@link CustomerRegistry#createCustomer(String, String,
 * String, String)} <br>
 * and is only updated through {@link CustomerRegistry#updateCustomer(String, String, String,
 * String, String)}
 */
@XmlRootElement(name = "customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer implements Serializable {

  private static final long serialVersionUID = 1L;

  @XmlID private String id;
  private String lastName;
  private String firstName;
  private String phone;
  private String notes;

  @XmlTransient private LocalDate firstSaleDate;
  @XmlTransient private LocalDate lastSaleDate;
  @XmlTransient private float totalRevenue;
  @XmlTransient private int totalSales;

  Customer(String lastName, String firstName, String phone, String notes) {
    super();
    this.lastName = lastName.trim();
    this.firstName = firstName.trim();
    this.phone = phone;
    this.notes = notes.trim();
    this.firstSaleDate = null;
    this.lastSaleDate = null;
  }

  Customer() {}

  @Override
  public String toString() {
    return lastName
        + firstName
        + id
        + phone
        + notes
        + firstSaleDate
        + lastSaleDate
        + totalRevenue
        + totalSales;
  }

  /**
   * Returns the ID of the customer
   *
   * @return the ID of the customer
   */
  public String getId() {
    return id;
  }
  /**
   * Returns the last name of the customer
   *
   * @return the last name of the customer
   */
  public String getLastName() {
    return lastName;
  }
  /**
   * Returns the first name of the customer
   *
   * @return the first name of the customer
   */
  public String getFirstName() {
    return firstName;
  }
  /**
   * Returns the phone of the customer
   *
   * @return the phone of the customer
   */
  public String getPhone() {
    return phone;
  }
  /**
   * Returns the notes of the customer
   *
   * @return the notes of the customer
   */
  public String getNotes() {
    return notes;
  }
  /**
   * Returns the date of the first sale made by the customer.
   *
   * @return the date of the first sale made by the customer, null if the customer hasn't made any
   *     sale
   */
  public LocalDate getFirstSaleDate() {
    return firstSaleDate;
  }
  /**
   * Returns the date of the last sale made by the customer.
   *
   * @return the date of the last sale made by the customer, null if the customer hasn't made any
   *     sale
   */
  public LocalDate getLastSaleDate() {
    return lastSaleDate;
  }
  /**
   * Returns the revenue made by the customer across all sales the customer appears into.
   *
   * @return the revenue made by the customer
   */
  public float getTotalRevenue() {
    return totalRevenue;
  }
  /**
   * Returns the number of sales this customer appears into
   *
   * @return the number of sales this customer appears into
   */
  public int getTotalSales() {
    return totalSales;
  }

  void setId(String id) {
    this.id = id;
  }

  void setLastName(String lastName) {
    this.lastName = lastName.trim();
  }

  void setFirstName(String firstName) {
    this.firstName = firstName.trim();
  }

  void setPhone(String phone) {
    this.phone = phone;
  }

  void setNotes(String notes) {
    this.notes = notes.trim();
  }

  void setFirstSaleDate(LocalDate firstSaleDate) {
    this.firstSaleDate = firstSaleDate;
  }

  void setLastSaleDate(LocalDate lastSaleDate) {
    this.lastSaleDate = lastSaleDate;
  }

  void setTotalRevenue(float totalRevenue) {
    if (totalRevenue < 0) totalRevenue = 0;
    this.totalRevenue = totalRevenue;
  }

  void setTotalSales(int totalSales) {
    if (totalSales < 0) totalSales = 0;
    this.totalSales = totalSales;
  }
}
