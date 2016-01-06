package application;

import java.util.HashMap;
import java.util.Map;

/**
 * Impl�mente un service de gestion de comptes bancaires. Les comptes sont
 * identifi�s par le nom de leur propri�taire. Un compte peut �tre consult� pour
 * connaitre son solde ou cr�dit�/d�bit� d'un certain montant.
 * <p>
 * Pour simplifier les test de l'application, un service bancaire est cr�� avec
 * trois comptes pr�-d�finis :
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
   * Cr�e un nouvel objet ServiceBanquaire, avec trois comptes pr�-d�finis.
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
   * Retourne le solde du compte sp�cifi�.
   *
   * @param c
   *          nom du propri�taire du compte
   * @return le solde du compte
   * @throws IllegalArgumentException
   *           si le compte sp�cifi� n'existe pas
   */
  public float consulterSolde(String c) {
    Float s = comptes.get(c);
    if (s == null) {
      throw new IllegalArgumentException("c");
    }
    return s;
  }

  /**
   * Cr�dite ou d�bite le compte sp�cifi� du montant sp�cifi�.
   *
   * @param c
   *          nom du propri�taire du compte
   * @param m
   *          montant � cr�diter (si > 0) ou d�biter (si < 0)
   * @return le nouveau solde du compte
   * @throws IllegalArgumentException
   *           si le compte sp�cifi� n'existe pas
   * @throws IllegalStateException
   *           si un d�bit est demand� et le solde du compte est insuffisant
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
