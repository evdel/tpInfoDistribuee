package test;

import application.Client;
import application.Serveur;

/**
 * Programme de test.
 *
 * @author Jean-Michel Busca
 *
 */
public class Test {

  public static void main(String[] args) throws InterruptedException {

    // créer et lancer le/les serveurs du système réparti
    // et leur laisser le temps de s'initialiser
    Serveur s1 = new Serveur("S1");
    Thread.sleep(100);

    // créer et lancer le/les clients du système réparti
    // en leur demandant de communiquer avec le/les serveurs
    Client c1 = new Client("C1", "S1");

  }

}
