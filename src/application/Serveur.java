package application;

import infrastructure.Calculateur;
import infrastructure.Message;

/**
 * Un {@link Calculateur} serveur de l'application, impl�mentant un
 * {@link ServiceBanquaire}. Il re�oit de fa�on asynchrone les {@link Requete}s
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
   * Construit un calculateur serveur ex�cutant le service
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

    // delivrer la requ�te au service bancaire
    delivrer(e, rq);
  }

  /**
   * D�livre la requ�te sp�cifi�e au service bancaire.
   *
   * @param e
   *          emetteur de la requ�te
   * @param rq
   *          requ�te de service bancaire extraite du message re�u
   */
  public void delivrer(String e, Requete rq) {

    // d�coder et traiter la requete
    Reponse rp = null;
    String c = rq.getCompte();

    // les m�thodes envoient des exception en cas d'erreur
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
      afficher("compte " + c + " non approvisionn�");
      rp = new Reponse(ex);
    }

    // envoyer la r�ponse
    envoyer(e, new Message(rp));
  }

}
