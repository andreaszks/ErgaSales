package io.github.andreaszks.ergasales.tools;

import io.github.andreaszks.ergasales.model.Customer;
import io.github.andreaszks.ergasales.model.Product;
import io.github.andreaszks.ergasales.model.Registry;
import io.github.andreaszks.ergasales.model.Sale;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/** Contains methods that handle the Import and Export of Registry in serialized format. */
public abstract class FileToolsSer extends FileTools {

  private static Registry registryFromFile;

  private static final byte[] KEY = "ErgaSalesErgaSalesErgaSalesSales".getBytes();
  private static final String KEY_ALG = "AES";
  // AES/ECB/PKCS5Padding is NOT a secure mode, i just wanted to hide some of the plaintext.
  private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
  private static final SecretKeySpec SKS = new SecretKeySpec(KEY, KEY_ALG);

  private static final File REGISTRY_FILE = new File("registry.ser");

  /** Exports Registry object in encrypted serialized format. */
  public static void exportRegistryToFile() {

    try {

      Cipher cipher = Cipher.getInstance(ALGORITHM);
      cipher.init(Cipher.ENCRYPT_MODE, SKS);

      CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(REGISTRY_FILE), cipher);
      ObjectOutputStream oos = new ObjectOutputStream(cos);

      oos.writeObject(Registry.getInstance());
      oos.close();
      cos.close();
    } catch (NoSuchAlgorithmException
        | NoSuchPaddingException
        | InvalidKeyException
        | IOException e1) {
      e1.printStackTrace();
    }
  }

  /**
   * Imports Registry that has been exported by exportRegistryToFile() method.
   *
   * <p>Called only when starting the application. All other checks once the application has already
   * started are performed through {@link #readRegistryFromFile()}.<br>
   * If it fails to import the Registry from the file for whichever reason, it clears the Instance.
   */
  public static void importRegistryFromFile() {

    if (REGISTRY_FILE.length() == 0) {
      Registry.getInstance();
    } else {

      try {

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, SKS);

        CipherInputStream cis = new CipherInputStream(new FileInputStream(REGISTRY_FILE), cipher);
        ObjectInputStream ois = new ObjectInputStream(cis);

        Registry.setInstance((Registry) ois.readObject());
        IDTools.resetIds();

        ois.close();
        cis.close();

      } catch (IOException
          | ClassNotFoundException
          | NoSuchAlgorithmException
          | NoSuchPaddingException
          | InvalidKeyException e) {
        Registry.clearInstance();
        e.printStackTrace();
      }
    }
  }

  private static boolean readRegistryFromFile() {

    if (!REGISTRY_FILE.exists() || REGISTRY_FILE.length() == 0) {
      exportRegistryToFile();
      return false;
    } else {

      try {

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, SKS);

        CipherInputStream cis = new CipherInputStream(new FileInputStream(REGISTRY_FILE), cipher);
        ObjectInputStream ois = new ObjectInputStream(cis);

        registryFromFile = (Registry) ois.readObject();
        ois.close();
        cis.close();
        return true;

      } catch (IOException
          | NoSuchAlgorithmException
          | NoSuchPaddingException
          | InvalidKeyException
          | ClassNotFoundException e) {
        // exportToFile();
        e.printStackTrace();
        return false;
      }
    }
  }

  /**
   * Checks if the Registry in the serialized file is any different than the running state.
   *
   * @return true if the Registry on file is different than the Registry on running state
   */
  public static boolean registryHasUnsavedChanges() {
    if (!readRegistryFromFile()) return false;

    List<Customer> appCustomers = Registry.getInstance().getCustomerRegistry().getCustomers();
    List<Customer> fileCustomers = registryFromFile.getCustomerRegistry().getCustomers();

    List<Product> appProducts = Registry.getInstance().getProductRegistry().getProducts();
    List<Product> fileProducts = registryFromFile.getProductRegistry().getProducts();

    List<Sale> appSales = Registry.getInstance().getSaleRegistry().getSales();
    List<Sale> fileSales = registryFromFile.getSaleRegistry().getSales();

    if (appCustomers.size() != fileCustomers.size()
        || appProducts.size() != fileProducts.size()
        || appSales.size() != fileSales.size()) return true;

    for (int i = 0; i < appCustomers.size(); i++) {
      if (!appCustomers.get(i).toString().contentEquals(fileCustomers.get(i).toString()))
        return true;
    }
    for (int i = 0; i < appProducts.size(); i++) {
      if (!appProducts.get(i).toString().contentEquals(fileProducts.get(i).toString())) return true;
    }
    for (int i = 0; i < appSales.size(); i++) {
      if (!appSales.get(i).toString().contentEquals(fileSales.get(i).toString())) return true;
    }

    return false;
  }
}
