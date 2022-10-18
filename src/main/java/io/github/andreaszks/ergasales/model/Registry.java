package io.github.andreaszks.ergasales.model;

import io.github.andreaszks.ergasales.tools.IDTools;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Wrapper class that stores the instances of registries.
 *
 * <p>The instance of this Registry wrapper class is the object that is marshaled in XML file or
 * serialized in SER file.<br>
 * This is the class that is used to access the different registries (CustomerRegistry,
 * ProductRegistry, SaleRegistry).
 */
@XmlRootElement(name = "registry")
@XmlAccessorType(XmlAccessType.FIELD)
public class Registry implements Serializable {

  private static final long serialVersionUID = 1L;

  @XmlTransient private static Registry registryInstance = new Registry();

  @XmlElement(name = "customers")
  private CustomerRegistry customerRegistry;

  @XmlElement(name = "products")
  private ProductRegistry productRegistry;

  @XmlElement(name = "sales")
  private SaleRegistry saleRegistry;

  private Registry() {
    this.customerRegistry = new CustomerRegistry();
    this.productRegistry = new ProductRegistry();
    this.saleRegistry = new SaleRegistry();
  }

  /**
   * Returns the registry wrapper that contain the Product, Customer and Sale registries.
   *
   * @return the registry wrapper.
   */
  public static Registry getInstance() {
    return registryInstance;
  }

  /** Clears all registries of the application. */
  public static void clearInstance() {
    registryInstance.customerRegistry.clearRegistry();
    registryInstance.productRegistry.clearRegistry();
    registryInstance.saleRegistry.clearRegistry();
    IDTools.resetIds();
  }

  /**
   * Sets the registry to a new registry that came from reading files.
   *
   * @param registry
   */
  public static void setInstance(Registry registry) {
    registryInstance = registry;
  }

  /**
   * Returns the customer registry.
   *
   * @return the registry that contains and manages customers.
   */
  public CustomerRegistry getCustomerRegistry() {
    return customerRegistry;
  }

  /**
   * Returns the product registry.
   *
   * @return the registry that contains and manages products.
   */
  public ProductRegistry getProductRegistry() {
    return productRegistry;
  }

  /**
   * Returns the sale registry.
   *
   * @return the registry that contains and manages sales.
   */
  public SaleRegistry getSaleRegistry() {
    return saleRegistry;
  }
}
