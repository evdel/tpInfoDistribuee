package infrastructure;

import java.io.Serializable;

/**
 * Message envoyé entre deux {@link Calculateur}s via le {@link Reseau}.
 *
 * @author Jean-Michel Busca
 *
 */
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;

  //
  // ATTRIBUTS D'OBJET
  //
  private String emetteur;
  private String destinataire;
  private final Serializable contenu;

  //
  // CONSTRUCTEURS ET ACCESSEURS
  //
  public Message(Serializable c) {
    contenu = c;
  }

  public String getEmetteur() {
    return emetteur;
  }

  public String getDestinataire() {
    return destinataire;
  }

  public Serializable getContenu() {
    return contenu;
  }

  void setEmetteur(String e) {
    this.emetteur = e;
  }

  void setDestinataire(String d) {
    this.destinataire = d;
  }

  @Override
  public String toString() {
    return "Message [e=" + emetteur + ", d=" + destinataire + ", c=" + contenu
            + "]";
  }

}
