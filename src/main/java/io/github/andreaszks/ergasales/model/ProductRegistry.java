package io.github.andreaszks.ergasales.model;

import io.github.andreaszks.ergasales.tools.IDTools;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/** Creates and manages products in the Registry. */
@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductRegistry implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final int MAX_PRODUCT_NAME_LENGTH = 100;
  public static final int MAX_PRODUCT_TYPE_LENGTH = 40;

  @XmlElement(name = "product")
  private ArrayList<Product> products = new ArrayList<>();

  ProductRegistry() {
    products = new ArrayList<>();
  }

  /**
   * Returns a list of all Products in the Registry
   *
   * @return a list of all Products in the Registry
   */
  public List<Product> getProducts() {
    // Applies to  getCustomers, getProducts, getSales: Return a clone that has the original
    // objects to prevent modification of the original list.
    // Example: if you return the original list, one can execute remove(), add() etc without
    // updating the required objects and fields.
    // Scenario it solves: by removing a sale outside SaleRegistry#removeSaleById(), the
    // customer and products will not update quantities, dates etc.
    ArrayList<Product> productsClone = new ArrayList<>();
    productsClone.addAll(products);
    return productsClone;
  }

  /**
   * Creates a Product and adds it to the Registry.
   *
   * <p>The Product is created iff the values are in correct format, there is available ID to assign
   * and there is no other Product with the same name.
   *
   * @see ProductRegistry#isProductCorrectFormat(String, String)
   * @param pname
   * @param ptype
   * @return true if the Product is created and added to Registry, false if not
   */
  public boolean createProduct(String pname, String ptype) {
    if (isProductCorrectFormat(pname, ptype)) {
      for (Product product : products) {
        if (product.getName().equalsIgnoreCase(pname)) return false;
      }
      String id = IDTools.generateUniqueId();
      if (id != null) {
        Product p = new Product(pname, ptype);
        p.setId(id);
        products.add(p);
      } else return false;
    } else return false;

    return true;
  }

  /**
   * Updates the Product to the new values
   *
   * <p>The Product is update iff the new values are in correct format and there is no other Product
   * with the same name.
   *
   * @see #isProductCorrectFormat(String, String)
   * @param id
   * @param newName
   * @param newType
   * @return true if Product is updated, false if not
   */
  public boolean updateProduct(String id, String newName, String newType) {
    Product p = getProductById(id);
    if (p == null) return false;
    if (isProductCorrectFormat(newName, newType)) {
      for (Product product : products) {
        if (product != p && product.getName().equalsIgnoreCase(newName)) return false;
      }
      p.setName(newName);
      p.setType(newType);
      return true;
    } else return false;
  }

  /**
   * Deletes the Product with specified ID and all objects that are related to it.
   *
   * <p>Deleting a Product removes all sales that include this Product.
   *
   * @param id
   */
  public boolean deleteProductById(String id) {
    if (id == null) return false;
    for (int i = 0; i < products.size(); i++) {
      if (products.get(i).getId().contentEquals(id)) {
        Registry.getInstance().getSaleRegistry().deleteSalesByProductId(id);
        products.remove(i);
        IDTools.resetIds();
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the Product with specified ID.
   *
   * @param id
   * @return the Product with specified ID or null if no product is found
   */
  public Product getProductById(String id) {
    if (id == null) return null;
    for (Product product : products) {
      if (product.getId().contentEquals(id)) return product;
    }
    return null;
  }

  /**
   * Returns a list of Products that match the term.
   *
   * @param term
   * @return a list of Products that match the term or empty list if no matches are found
   */
  public List<Product> getProductsByTerm(String term) {
    ArrayList<Product> foundProducts = new ArrayList<>();
    if (term == null) return foundProducts;

    for (Product product : products) {
      if (product.getName().toUpperCase().contains(term.toUpperCase())) foundProducts.add(product);
    }

    return foundProducts;
  }

  /**
   * Returns the different types across Products
   *
   * @return the different types across Products
   */
  public List<String> getTypes() {

    ArrayList<String> types = new ArrayList<>();

    for (Product product : products) {
      if (!types.contains(product.getType())) {
        types.add(product.getType());
      }
    }

    return types;
  }

  /**
   * Returns a list of Products that match the type.
   *
   * @param type
   * @return a list of Products that match the type or empty list if no matches are found
   */
  public List<Product> getProductsByType(String type) {
    ArrayList<Product> productsWithType = new ArrayList<>();
    if (type == null) return productsWithType;

    for (Product product : products) {
      if (product.getType().contentEquals(type)) {
        productsWithType.add(product);
      }
    }

    return productsWithType;
  }

  /**
   * Returns a list of Products that match both term and type.
   *
   * <p>Searches for matching term only in Products that have the specified type.
   *
   * @param term
   * @param type
   * @return a list of Products that match both term and type or empty list if no matches are found
   */
  public List<Product> getProductsByTermAndType(String term, String type) {
    ArrayList<Product> productsWithTermAndType = new ArrayList<>();
    if (term == null || type == null) return productsWithTermAndType;

    productsWithTermAndType.addAll(getProductsByTerm(term));
    productsWithTermAndType.retainAll(getProductsByType(type));

    return productsWithTermAndType;
  }

  /**
   * Checks if Product fields are in correct format.
   *
   * <p>For the fields to be correct they have to:<br>
   * 1)not be empty or null,<br>
   * 2)be smaller than MAX,<br>
   * 3)not have leading or trailing whitespace
   *
   * @see #MAX_PRODUCT_NAME_LENGTH
   * @see #MAX_PRODUCT_TYPE_LENGTH
   * @param productName
   * @param productType
   * @return true if the fields are in correct format
   */
  public static boolean isProductCorrectFormat(String productName, String productType) {
    if (productName == null || productType == null) return false;
    // whitespace is not an issue when creating product through UI but it is when checking
    // product from XML file.
    if (!productName.equals(productName.trim())) return false;
    if (!productType.equals(productType.trim())) return false;
    if (productName.length() == 0 || productName.length() > MAX_PRODUCT_NAME_LENGTH) return false;
    if (productType.length() == 0 || productType.length() > MAX_PRODUCT_TYPE_LENGTH) return false;

    return true;
  }

  void clearRegistry() {
    products.clear();
  }
}
