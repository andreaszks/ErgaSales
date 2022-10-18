package io.github.andreaszks.ergasales.tools;

import io.github.andreaszks.ergasales.model.Customer;
import io.github.andreaszks.ergasales.model.Product;
import io.github.andreaszks.ergasales.model.Registry;
import io.github.andreaszks.ergasales.model.Sale;
import java.security.SecureRandom;
import java.util.HashSet;

/**
 * Contains methods that create, validate and manage IDs across Registries (Customers, Products,
 * Sales).
 */
public abstract class IDTools {

  public static final int ID_LENGTH = 5;

  private static int minId = (int) Math.pow(10, ID_LENGTH - 1d) + 1;
  private static int maxCombinations = (int) Math.pow(10, ID_LENGTH) - minId;

  private static SecureRandom rnd = new SecureRandom();
  private static HashSet<String> ids = new HashSet<>();

  /**
   * Creates a unique random numeric ID with a length of ID_LENGTH.
   *
   * <p>If 90% of possible IDs are created, stop creating more IDs and return null.<br>
   * There are (10^ID_LENGTH - 10^(ID_LENGTH-1)) -1 possible combinations
   *
   * <p>Example: for ID_LENGTH of 5 there are 90k available IDs ranging from 10000 to 99999
   *
   * @see #ID_LENGTH
   * @return a numerical ID of ID_LENGTH length or null if there are no available IDs.
   */
  public static String generateUniqueId() {
    if (ids.size() > maxCombinations / 1.11) return null; // 1.11=10/9

    String id;
    int tries = 0;
    do {
      id = String.valueOf(minId + rnd.nextInt(maxCombinations));
      tries++;
    } while (ids.contains(id)
        && tries < 4); // Try 3 more times if you find a hit. This is for when ID's set size
    // is close to max allowed combinations.
    // The first 40-60% is unlikely to have a hit so it will run 1 time. By trying 2 more times
    // when set is more than 75% full,
    // your chances to return an actual ID instead of null are increased by 30%. 4 tries are
    // good for accuracy without spending time.
    // 1000 tries success rates benchmark: Tries: success rate => 1: 70.2%, 2: 84.3%, 3: 91.3%,
    // 4: 95.3%, 5: 98.0%, 6: 99.8%

    if (ids.contains(id))
      return null; // if however 4 times aren't enough and you still hit, return null
    ids.add(id);
    return id;
  }

  /**
   * Checks if the ID is in correct format.
   *
   * <p>For the ID to be in correct format the represented number must be:<br>
   * larger than 10^(ID_LENGTH-1) and<br>
   * smaller than (10^ID_LENGTH)-1.
   *
   * @see #generateUniqueId()
   * @param id
   * @return true if the ID is in correct format
   */
  public static boolean isIdCorrectFormat(String id) {
    if (id == null || id.length() != ID_LENGTH || id.charAt(0) == '0') return false;

    for (int i = 0; i < id.length(); i++) {
      if ("0123456789".indexOf(id.charAt(i)) == -1) {
        return false;
      }
    }
    return true;
  }

  static void setIds(HashSet<String> ids) {
    // only accessed from FileToolsSer#importRegistryFromFile() and
    // FileToolsXml#importRegistryFromXml();
    IDTools.ids = ids;
  }

  /** Binds the IDs imported from the serialized file. */
  public static void resetIds() {
    // Gets called when launching the application and reading from serialized file,
    // if XML import is failed to import registry,
    // when you delete a customer, product or sale.
    ids.clear();
    for (Customer c : Registry.getInstance().getCustomerRegistry().getCustomers())
      ids.add(c.getId());
    for (Product p : Registry.getInstance().getProductRegistry().getProducts()) ids.add(p.getId());
    for (Sale s : Registry.getInstance().getSaleRegistry().getSales()) ids.add(s.getId());
  }
}
