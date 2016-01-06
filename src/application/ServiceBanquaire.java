package application;

import java.util.HashMap;
import java.util.Map;

/**
 * Implémente un service de gestion de comptes bancaires. Les comptes sont
 * identifiés par le nom de leur propriétaire. Un compte peut être consulté pour
 * connaitre son solde ou crédité/débité d'un certain montant.
 * <p>
 * Pour simplifier les test de l'application, un service bancaire est créé avec
 * trois comptes pré-définis :
 * <ul>
 * <li>Dupont, solde -100.00 E
 * <li>Durand, solde 0.00 E
 * <li>Martin, solde 100.00 E
 * </ul>
 *
 * @author Jean-Michel Busca
 *
 */
public class ServiceBanquaire {

  //
  // ATTRIBUTS D'OBJET
  //
  private final Map<String, Float> comptes;

  //
  // CONSTRUCTEURS ET ACCESSEURS
  //
  /**
   * Crée un nouvel objet ServiceBanquaire, avec trois comptes pré-définis.
   *
   */
  public ServiceBanquaire() {
    comptes = new HashMap<String, Float>();
    comptes.put("Dupont", -100.0f);
    comptes.put("Durand", 0.0f);
    comptes.put("Martin", +100.0f);
  }

  //
  // METHODES METIER
  //
  /**
   * Retourne le solde du compte spécifié.
   *
   * @param c
   *          nom du propriétaire du compte
   * @return le solde du compte
   * @throws IllegalArgumentException
   *           si le compte spécifié n'existe pas
   */
  public float consulterSolde(String c) {
    Float s = comptes.get(c);
    if (s == null) {
      throw new IllegalArgumentException("c");
    }
    return s;
  }

  /**
   * Crédite ou débite le compte spécifié du montant spécifié.
   *
   * @param c
   *          nom du propriétaire du compte
   * @param m
   *          montant à créditer (si > 0) ou débiter (si < 0)
   * @return le nouveau solde du compte
   * @throws IllegalArgumentException
   *           si le compte spécifié n'existe pas
   * @throws IllegalStateException
   *           si un débit est demandé et le solde du compte est insuffisant
   */
  public float crediterDebiter(String c, float m) {
    Float s = comptes.get(c);
    if (s == null) {
      throw new IllegalArgumentException("c");
    }
    if (m < 0 && (s + m) < 0) {
      throw new IllegalStateException();
    }
    s += m;
    comptes.put(c, s);
    return s;
  }

}
