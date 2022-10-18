package io.github.andreaszks.ergasales.model;

import io.github.andreaszks.ergasales.tools.IDTools;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/** Creates and manages customers in the Registry. */
@XmlRootElement(name = "customers")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerRegistry implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final int MAX_NAME_LENGTH = 22;
  public static final int PHONE_LENGTH = 10;
  public static final int MAX_NOTES_LENGTH = 150;

  @XmlElement(name = "customer")
  private ArrayList<Customer> customers = new ArrayList<>();

  CustomerRegistry() {
    customers = new ArrayList<>();
  }

  /**
   * Returns a list of all customers in Registry.
   *
   * @return a list of all customers in Registry.
   */
  public List<Customer> getCustomers() {
    // Applies to  getCustomers, getProducts, getSales: Return a clone that has the original
    // objects to prevent modification of the original list.
    // Example: if you return the original list, one can execute remove(), add() etc without
    // updating the required objects and fields.
    // Scenario it solves: by removing a sale outside SaleRegistry#removeSaleById(), the
    // customer and products will not update quantities, dates etc.
    ArrayList<Customer> customersClone = new ArrayList<>();
    customersClone.addAll(customers);
    return customersClone;
  }

  /**
   * Creates a customer and adds it to the Registry.
   *
   * <p>If a customer with the same name and phone already exists, do not add and return false.
   *
   * @see #isCustomerCorrectFormat(String, String, String, String)
   * @param lastname
   * @param firstname
   * @param phone
   * @param notes
   * @return true if fields are not conflicting with other customer and customer is added.
   */
  public boolean createCustomer(String lastname, String firstname, String phone, String notes) {
    if (isCustomerCorrectFormat(lastname, firstname, phone, notes)) {
      for (Customer customer : customers) {
        if (customer.getLastName().equalsIgnoreCase(lastname)
            && customer.getFirstName().equalsIgnoreCase(firstname)
            && customer.getPhone().equals(phone)) {
          return false;
        }
      }
      String id = IDTools.generateUniqueId();
      if (id != null) {
        Customer c = new Customer(lastname, firstname, phone, notes);
        c.setId(id);
        customers.add(c);
      } else return false;
    } else return false;

    return true;
  }

  /**
   * Updates customer with the new info.
   *
   * <p>If the new values conflicts with existing customer, do not update and return false.
   *
   * @see #isCustomerCorrectFormat(String, String, String, String)
   * @param id
   * @param last
   * @param first
   * @param notes
   * @return true if fields are correct and not conflicting with other customers.
   */
  public boolean updateCustomer(String id, String last, String first, String phone, String notes) {
    Customer c = getCustomerById(id);
    if (c == null) return false;
    if (isCustomerCorrectFormat(last, first, phone, notes)) {
      for (Customer customer : customers) {
        if (customer != c
            && customer.getLastName().equalsIgnoreCase(last)
            && customer.getFirstName().equalsIgnoreCase(first)
            && customer.getPhone().equals(phone)) {
          return false;
        }
      }
      c.setLastName(last);
      c.setFirstName(first);
      c.setNotes(notes);
      return true;
    } else return false;
  }

  /**
   * Removes customer from the registry.
   *
   * <p>All sales from this customer are also removed.
   *
   * @param id
   * @return true if Customer is deleted from the Registry, false if no customer is found with this
   *     ID
   */
  public boolean deleteCustomerByID(String id) {
    if (id == null) return false;
    for (int i = 0; i < customers.size(); i++) {
      if (customers.get(i).getId().contentEquals(id)) {
        Registry.getInstance().getSaleRegistry().deleteSalesByCustomerId(id);
        customers.remove(i);
        IDTools.resetIds();
        return true;
      }
    }
    return false;
  }

  /**
   * Returns customers that match term.
   *
   * <p>The term may be either a name, phone or a term in notes.
   *
   * @param term
   * @return a list of customers that match term or an empty list if none are found.
   */
  public List<Customer> getCustomersByTerm(String term) {
    ArrayList<Customer> foundCustomers = new ArrayList<>();
    if (term == null) return foundCustomers;

    for (Customer customer : customers) {
      if (customer.toString().toUpperCase().contains(term.toUpperCase()))
        foundCustomers.add(customer);
    }

    return foundCustomers;
  }

  /**
   * Returns customer that has the specified ID.
   *
   * @param id
   * @return customer with specified ID or null if no customer is found
   */
  public Customer getCustomerById(String id) {
    if (id == null) return null;
    for (Customer customer : customers) {
      if (customer.getId().contentEquals(id)) return customer;
    }
    return null;
  }

  /**
   * Checks if Customer fields are in correct format.
   *
   * <p>For the fields to be correct they have to:<br>
   * 1)not be null,<br>
   * 2)be correct length(names not empty, phone empty or correct length, notes below max length),
   * <br>
   * 3)not have leading or trailing whitespace.
   *
   * @see #MAX_NAME_LENGTH
   * @see #PHONE_LENGTH
   * @see #MAX_NOTES_LENGTH
   * @param lastname
   * @param firstname
   * @param phone
   * @param notes
   * @return true if the fields in correct format
   */
  public static boolean isCustomerCorrectFormat(
      String lastname, String firstname, String phone, String notes) {
    if (lastname == null || firstname == null || phone == null || notes == null) return false;
    // whitespace is not an issue when creating customer through UI but it is when checking
    // customer from XML file.
    if (!lastname.equals(lastname.trim())) return false;
    if (!firstname.equals(firstname.trim())) return false;
    if (!phone.equals(phone.trim())) return false;
    if (!notes.equals(notes.trim())) return false;
    if (lastname.length() == 0 || lastname.length() > MAX_NAME_LENGTH) return false;
    if (firstname.length() == 0 || firstname.length() > MAX_NAME_LENGTH) return false;
    if (phone.length() != 0 && phone.length() != PHONE_LENGTH) return false;
    if (notes.length() > MAX_NOTES_LENGTH) return false;

    for (int i = 0; i < phone.length(); i++)
      if ("0123456789".indexOf(phone.charAt(i)) == -1) return false;

    return true;
  }

  void clearRegistry() {
    customers.clear();
  }
}
