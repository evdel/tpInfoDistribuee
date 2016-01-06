package infrastructure;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Un calculateur du simulateur de système réparti. Un calculateur peut
 * fonctionner selon deux modes :
 * <ul>
 * <li>asynchrone : la méthode d'attente des messages est
 * {@link #recevoir(Message)} ; le thread exécutant le programme principal est
 * lancé en mode daemon.
 * <li>synchrone : la méthode d'attente des messages est {@link #attendre()} ;
 * le thread exécutant le programme principal est lancé en mode defaut.
 * </ul>
 *
 * @author Jean-Michel Busca
 *
 */
public abstract class Calculateur extends Thread {

  //
  // CLASSES INTERNES
  //
  /**
   * Tâche envoyant en différé un message à un calculateur.
   *
   * @author Busca
   *
   */
  private static class Envoi extends TimerTask {

    private final Message message;
    private final Calculateur destinataire;

    Envoi(Message m, Calculateur d) {
      message = m;
      destinataire = d;
    }

    @Override
    public void run() {
      synchronized (destinataire) {
        if (destinataire.estAsynchrone) {
          destinataire.afficher("reception " + message + " (asynchrone)");
          destinataire.recevoir(message);
        } else {
          destinataire.messages.add(message);
          destinataire.notifyAll();
        }
      }
    }
  }

  //
  // ATTRIBUTS DE CLASSE
  //
  private static final Map<String, Calculateur> instances = new HashMap<String, Calculateur>();
  private static final Timer ordonnanceur = new Timer(true);
  private static final long debut = System.currentTimeMillis();

  //
  // ATTRIBUTS D'OBJET
  //
  private final String nom;
  private final Position position;
  private final List<Message> messages;
  private final boolean estAsynchrone;

  //
  // CONSTRUCTEURS ET ACCESSEURS
  //
  /**
   * Crée un calculateur ayant le nom spécifié, et fonctionnant en mode
   * synchrone.
   *
   * @param n
   *          nom du calculateur à créer
   */
  public Calculateur(String n) {
    this(n, false);
  }

  /**
   * Crée un calculateur avec le nom et le mode de fonctionnement spécifiés.
   *
   * @param n
   *          nom du calculateur à créer
   * @param a
   *          vrai si le calculateur doit fonctionner en mode asynchrone
   */
  public Calculateur(String n, boolean a) {
    nom = n;
    if (instances.get(nom) != null) {
      throw new IllegalArgumentException("n");
    }
    instances.put(nom, this);
    position = Reseau.newPosition();
    messages = new ArrayList<Message>();
    estAsynchrone = a;
    setDaemon(a);
    start();
  }

  /**
   * Retourne le nom de ce calculateur.
   *
   * @return le nom de ce calculateur
   */

  public String getNom() {
    return nom;
  }

  @Override
  public String toString() {
    return nom;
  }

  //
  // PROGRAMME DU CALCULATEUR
  //
  /**
   * Programme principal du calculateur, lancé à sa création. Cette méthode est
   * destinée à être implémenté dans les sous-classes de Calculateur.
   * <p>
   * Pour simplifier la programmation, cette méthode est autorisée à lancer
   * l'exception InterruptedException (pas de try/catch à écrire).
   *
   * @throws InterruptedException
   */
  public abstract void programme() throws InterruptedException;

  //
  // ENVOI/RECEPTION DE MESSAGES
  //
  /**
   * Envoie un message à un calculateur destinataire.
   *
   * @param n
   *          nom du calculateur destinataire
   * @param m
   *          message à lui envoyer
   */
  public final void envoyer(String n, Message m) {
    Calculateur d = toCalculateur(n);
    m.setEmetteur(nom);
    m.setDestinataire(n);
    long l = Reseau.latence(this.position, d.position);
    afficher("envoi     " + m + " (latence=" + l + "ms)");
    m = (Message) new Serialisation(m).copie();
    ordonnanceur.schedule(new Envoi(m, d), l);
  }

  /**
   * Reçoit un message de façon asynchrone. Cette méthode est appelée par le
   * simulateur pour signaler la réception du message spécifié. Son
   * implémentation doit être définie dans les sous-classes de Calculateur.
   * <p>
   * Les appels à cette méthode sont synchronisés par le simulateur : il ne peut
   * y avoir deux exécutions concurrentes de cette méthode sur un même
   * calculateur.
   *
   * @param m
   *          message reçu, à traiter par le calculateur
   */
  public void recevoir(Message m) {
    throw new IllegalStateException("recevoir(Message) non redéfinie");
  }

  /**
   * Attend de façon bloquante le prochain message à recevoir.
   *
   * @return le message reçu
   *
   * @throws InterruptedException
   *           si ce calculateur est interrompu
   */
  public final synchronized Message attendre() throws InterruptedException {
    while (messages.isEmpty()) {
      wait();
    }
    Message r = messages.remove(0);
    afficher("reception " + r + " (synchrone)");
    notifyAll();
    return r;
  }

  /**
   * Diffuse un message à un groupe destinataire.
   *
   * @param d
   *          nom du groupe de calculateurs destinataires
   * @param m
   *          message à lui envoyer
   */
  public final void diffuser(String d, Message m) {
    // à développer
  }

  //
  // METHODES UTILITAIRES
  //
  /**
   * Affiche le message spécifié, précédé de l'heure et du nom du calculateur.
   *
   * @param message
   */
  public final void afficher(String message) {
    System.out.println(date() + " - " + this + " : " + message);
  }

  //
  // METHODES INTERNES
  //
  @Override
  public final void run() {
    try {
      programme();
    } catch (InterruptedException e) {
      afficher("interrompu");
    }
  }

  private Calculateur toCalculateur(String n) {
    Calculateur d = instances.get(n);
    if (instances.get(n) == null) {
      throw new IllegalArgumentException("n=" + n);
    }
    return d;
  }

  private static String date() {
    long d = System.currentTimeMillis() - debut;
    return new Formatter().format("%02d.%03d", d / 1000, d % 1000).toString();
  }

}
