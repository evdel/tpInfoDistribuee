package infrastructure;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Un calculateur du simulateur de syst�me r�parti. Un calculateur peut
 * fonctionner selon deux modes :
 * <ul>
 * <li>asynchrone : la m�thode d'attente des messages est
 * {@link #recevoir(Message)} ; le thread ex�cutant le programme principal est
 * lanc� en mode daemon.
 * <li>synchrone : la m�thode d'attente des messages est {@link #attendre()} ;
 * le thread ex�cutant le programme principal est lanc� en mode defaut.
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
   * T�che envoyant en diff�r� un message � un calculateur.
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
   * Cr�e un calculateur ayant le nom sp�cifi�, et fonctionnant en mode
   * synchrone.
   *
   * @param n
   *          nom du calculateur � cr�er
   */
  public Calculateur(String n) {
    this(n, false);
  }

  /**
   * Cr�e un calculateur avec le nom et le mode de fonctionnement sp�cifi�s.
   *
   * @param n
   *          nom du calculateur � cr�er
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
   * Programme principal du calculateur, lanc� � sa cr�ation. Cette m�thode est
   * destin�e � �tre impl�ment� dans les sous-classes de Calculateur.
   * <p>
   * Pour simplifier la programmation, cette m�thode est autoris�e � lancer
   * l'exception InterruptedException (pas de try/catch � �crire).
   *
   * @throws InterruptedException
   */
  public abstract void programme() throws InterruptedException;

  //
  // ENVOI/RECEPTION DE MESSAGES
  //
  /**
   * Envoie un message � un calculateur destinataire.
   *
   * @param n
   *          nom du calculateur destinataire
   * @param m
   *          message � lui envoyer
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
   * Re�oit un message de fa�on asynchrone. Cette m�thode est appel�e par le
   * simulateur pour signaler la r�ception du message sp�cifi�. Son
   * impl�mentation doit �tre d�finie dans les sous-classes de Calculateur.
   * <p>
   * Les appels � cette m�thode sont synchronis�s par le simulateur : il ne peut
   * y avoir deux ex�cutions concurrentes de cette m�thode sur un m�me
   * calculateur.
   *
   * @param m
   *          message re�u, � traiter par le calculateur
   */
  public void recevoir(Message m) {
    throw new IllegalStateException("recevoir(Message) non red�finie");
  }

  /**
   * Attend de fa�on bloquante le prochain message � recevoir.
   *
   * @return le message re�u
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
   * Diffuse un message � un groupe destinataire.
   *
   * @param d
   *          nom du groupe de calculateurs destinataires
   * @param m
   *          message � lui envoyer
   */
  public final void diffuser(String d, Message m) {
    // � d�velopper
  }

  //
  // METHODES UTILITAIRES
  //
  /**
   * Affiche le message sp�cifi�, pr�c�d� de l'heure et du nom du calculateur.
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
