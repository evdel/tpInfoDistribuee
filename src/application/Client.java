package application;

import infrastructure.Calculateur;
import infrastructure.Message;

import java.util.Random;

/**
 * Un {@link Calculateur} client de l'application. Il envoie des {@link Requete}
 * s aux {@link Serveur}s, et attend de fa�on synchrone leurs {@link Reponse}.
 *
 * @author Jean-Michel Busca
 *
 */
public class Client extends Calculateur {

  //
  // ATTRIBUTS DE CLASSE
  //
  private static final Random random = new Random(System.currentTimeMillis());

  //
  // ATTRIBUTS D'OBJET
  //
  private final String destinataire; // serveur ou groupe de serveurs

  //
  // CONSTRUCTEURS ET ACCESSEURS
  //
  /**
   * Construit un calculateur client s'adressant � un destinataire donn�, qu'il
   * soit serveur simple ou groupe de serveurs.
   *
   * @param n
   *          nom du client
   * @param d
   *          nom du serveur ou groupe de serveurs � contacter
   */
  public Client(String n, String d) {
    super(n, false);
    destinataire = d;
  }

  //
  // METHODES PUBLIQUES
  //
  @Override
  public void programme() throws InterruptedException {

    // d�but du programme du client
    afficher("demarrage");

    // envoyer 5 requ�tes au serveur
    for (int i = 0; i < 5; i++) {

      // requ�te de cr�dit/d�bit :
      // - compte Durand
      // - montant al�atoire dans [-50, +50[
      float v = random.nextInt(100) - 50;
      Requete rq = new Requete("Durand", v);
      envoyer(destinataire, new Message(rq));

      // attendre et afficher la r�ponse
      Message m = attendre();
      Reponse rp = (Reponse) m.getContenu();
      afficher(rp + "");

      // attendre entre deux requ�tes, si n�cessaire
      Thread.sleep(500);

    }
    afficher("fin emission");

    // attendre que les serveurs terminent leurs traitements
    Thread.sleep(3000);

    // fin du programme client
    afficher("arret");
  }

}
