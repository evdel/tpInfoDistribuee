package application;

import java.io.Serializable;

/**
 * Une requête au {@link ServiceBancaire}.
 *
 * @author Jean-Michel Busca
 *
 */
public class Requete implements Serializable {

  private static final long serialVersionUID = 1L;

  //
  // ATTRIBUTS D'OBJET
  //
  private final boolean estConsultation;
  private final String compte;
  private final float montant;

  //
  // CONSTRUCTEURS ET ACCESSEURS
  //
  public Requete(String c) {
    this(true, c, 0.0f);
  }

  public Requete(String c, float m) {
    this(false, c, m);
  }

  private Requete(boolean ec, String c, float m) {
    this.estConsultation = ec;
    this.compte = c;
    this.montant = m;
  }

  public boolean estConsultation() {
    return estConsultation;
  }

  public String getCompte() {
    return compte;
  }

  public float getMontant() {
    return montant;
  }

  @Override
  public String toString() {
    if (estConsultation) {
      return "Requete [consultation compte=" + compte + "]";
    } else {
      return "Requete [credit/debit compte=" + compte + ", montant=" + montant
              + "]";
    }
  }

}
