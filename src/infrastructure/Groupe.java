package infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Un groupe de {@link Calculateur}s. Chaque groupe est identifié par un nom. Au
 * sein d'un groupe, les calculateurs sont identifiés par leur nom.
 *
 * @author Jean-Michel Busca
 *
 */
public class Groupe {

  //
  // ATTRIBUTS DE CLASSE
  //
  private static final Map<String, Groupe> instances = new HashMap<String, Groupe>();

  //
  // ATTRIBUTS D'OBJET
  //
  private final String nom;
  private final List<String> membres;

  //
  // CONSTRUCTEURS ET ACCESSEURS
  //
  public Groupe(String n) {
    nom = n;
    membres = new ArrayList<String>();
    if (instances.get(nom) != null) {
      throw new IllegalArgumentException("n");
    }
    instances.put(nom, this);
  }

  public String getNom() {
    return nom;
  }

  public List<String> getMembres() {
    return membres;
  }

  public int getNombre() {
    return membres.size();
  }

  @Override
  public String toString() {
    return "Groupe [nom=" + nom + ", membres=" + membres + "]";
  }

  //
  // CONVERSION NOM DE GROUPE - GROUPE
  //
  public static Groupe toGroupe(String n) {
    Groupe g = instances.get(n);
    if (instances.get(n) == null) {
      throw new IllegalArgumentException("n=" + n);
    }
    return g;
  }

  //
  // METHODES PUBLIQUES
  //
  public void ajouter(String n) {
    if (membres.contains(n)) {
      throw new IllegalArgumentException("n=" + n);
    }
    membres.add(n);
  }

}
