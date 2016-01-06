package application;

import java.io.Serializable;

/**
 * Une réponse du {@link ServiceBancaire}.
 *
 * @author Jean-Michel Busca
 *
 */
public class Reponse implements Serializable {

  private static final long serialVersionUID = 1L;

  //
  // ATTRIBUTS D'OBJET
  //
  private final float solde;
  private final Exception exception;

  //
  // CONSTRUCTEURS ET ACCESSEURS
  //
  public Reponse(float s) {
    this.solde = s;
    this.exception = null;
  }

  public Reponse(Exception e) {
    this.solde = -1.0f;
    this.exception = e;
  }

  public float getSolde() {
    return solde;
  }

  public Exception getException() {
    return exception;
  }

  @Override
  public String toString() {
    if (exception == null) {
      return "Reponse [solde=" + solde + "]";
    } else {
      return "Reponse [exception=" + exception + "]";
    }
  }

}
