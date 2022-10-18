package io.github.andreaszks.ergasales.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

/** An immutable pair of Product and quantity that is used in a Sale. */
@XmlRootElement(name = "productQuantityPair")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductQuantityPair implements Serializable {

  private static final long serialVersionUID = 1L;

  @XmlElement(name = "product")
  @XmlIDREF
  private Product product;

  private int quantity;

  public ProductQuantityPair(Product product, int quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  public ProductQuantityPair() {}

  /**
   * Returns the product.
   *
   * @return the product.
   */
  public Product getProduct() {
    return product;
  }
  /**
   * Returns the quantity
   *
   * @return the quantity
   */
  public int getQuantity() {
    return quantity;
  }
}
