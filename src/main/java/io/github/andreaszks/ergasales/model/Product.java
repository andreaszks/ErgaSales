package io.github.andreaszks.ergasales.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * This class represents a Sale. Sales are managed through ProductRegistry
 *
 * <p>A sale is created only through {@link ProductRegistry#createProduct(String, String)} <br>
 * and is only updated through {@link ProductRegistry#updateProduct(String, String, String)}
 */
@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class Product implements Serializable {

  private static final long serialVersionUID = 1L;

  @XmlID private String id;

  private String name;
  private String type;

  @XmlTransient private int totalSales;
  @XmlTransient private int totalQuantity;

  Product(String name, String type) {
    super();
    this.name = name.trim();
    this.type = type.trim();
  }

  Product() {}

  @Override
  public String toString() {
    return name + type + id + totalSales + totalQuantity;
  }

  /**
   * Returns the ID of the product.
   *
   * @return the ID of the product
   */
  public String getId() {
    return id;
  }
  /**
   * Returns the name of the product.
   *
   * @return the name of the product
   */
  public String getName() {
    return name;
  }
  /**
   * Returns the type of the product.
   *
   * @return the type of the product
   */
  public String getType() {
    return type;
  }
  /**
   * Returns the number of sales that use this product.
   *
   * @return the number of sales that use this product
   */
  public int getTotalSales() {
    return totalSales;
  }
  /**
   * Returns the number of times this product has been used across all sales.
   *
   * @return the number of times this product has been used across all sales
   */
  public int getTotalQuantity() {
    return totalQuantity;
  }

  void setId(String id) {
    this.id = id;
  }

  void setName(String name) {
    this.name = name;
  }

  void setType(String type) {
    this.type = type;
  }

  void setTotalSales(int totalSales) {
    this.totalSales = totalSales;
  }

  void setTotalQuantity(int totalQuantity) {
    this.totalQuantity = totalQuantity;
  }
}
