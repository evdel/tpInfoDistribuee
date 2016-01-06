package infrastructure;

import java.util.Random;

/**
 * La position d'un {@link Calculateur} dans le {@link Reseau}.
 *
 * @author Jean-Michel Busca
 *
 */
public final class Position {

  //
  // ATTRIBUTS DE CLASSE
  //
  private static final Random random = new Random(System.currentTimeMillis());

  //
  // ATTRIBUTS D'OBJET
  //
  private final float theta;
  private final float phi;

  //
  // CONSTRUCTEURS ET ACCESSEURS
  //
  public Position() {
    this.theta = (float) Math.asin(2.0 * random.nextFloat() - 1.0);
    this.phi = (float) (2.0 * Math.PI * random.nextFloat());
  }

  public float getTheta() {
    return theta;
  }

  public float getPhi() {
    return phi;
  }

  @Override
  public String toString() {
    return "Position [theta=" + theta + ", phi=" + phi + "]";
  }

  //
  // DISTANCE
  //
  public final float distance(Position p) {
    double d = Math.acos(Math.cos(phi - p.phi) * Math.cos(theta)
            * Math.cos(p.theta) + Math.sin(theta) * Math.sin(p.theta));
    return (float) d;
  }

}
