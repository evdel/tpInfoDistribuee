package application;

import infrastructure.Calculateur;
import infrastructure.Message;

/**
 * Un {@link Calculateur} serveur de l'application, implémentant un
 * {@link ServiceBanquaire}. Il reçoit de façon asynchrone les {@link Requete}s
 * venant des {@link Client}s, et leur renvoie des {@link Reponse}.
 *
 * @author Jean-Michel Busca
 *
 */
public class Serveur extends Calculateur {

  //
  // ATTRIBUTS D'OBJET
  //
  private final ServiceBanquaire service;

  //
  // CONSTRUCTEURS ET ACCESSEURS
  //
  /**
   * Construit un calculateur serveur exécutant le service
   * {@link ServiceBancaire} et fonctionnant en mode asynchrone.
   *
   * @param n
   *          nom du serveur
   */
  public Serveur(String n) {
    super(n, true);
    service = new ServiceBanquaire();
  }

  //
  // METHODES PUBLIQUES
  //
  @Override
  public void programme() throws InterruptedException {
    afficher("demarrage");
  }

  @Override
  public void recevoir(Message m) {

    // extraire la requete du message
    String e = m.getEmetteur();
    Requete rq = (Requete) m.getContenu();

    // delivrer la requête au service bancaire
    delivrer(e, rq);
  }

  /**
   * Délivre la requête spécifiée au service bancaire.
   *
   * @param e
   *          emetteur de la requête
   * @param rq
   *          requête de service bancaire extraite du message reçu
   */
  public void delivrer(String e, Requete rq) {

    // décoder et traiter la requete
    Reponse rp = null;
    String c = rq.getCompte();

    // les méthodes envoient des exception en cas d'erreur
    try {

      float s;
      if (rq.estConsultation()) {
        s = service.consulterSolde(c);
      } else {
        s = service.crediterDebiter(c, rq.getMontant());
      }

      afficher("compte : " + c + ", nouveau solde = " + s);
      rp = new Reponse(s);

    } catch (IllegalArgumentException ex) {
      afficher("compte " + c + " inexistant");
      rp = new Reponse(ex);
    } catch (IllegalStateException ex) {
      afficher("compte " + c + " non approvisionné");
      rp = new Reponse(ex);
    }

    // envoyer la réponse
    envoyer(e, new Message(rp));
  }

}
