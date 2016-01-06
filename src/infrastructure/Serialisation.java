package infrastructure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Classe utilitaire permettant de serialiser et déserialiser un objet.
 *
 * @author Jean-Michel Busca
 *
 */
class Serialisation {

  // object field
  private byte[] representation;

  // constructor
  Serialisation(Object object) {
    try {
      ByteArrayOutputStream array = new ByteArrayOutputStream();
      ObjectOutputStream stream = new ObjectOutputStream(array);
      stream.writeObject(object);
      stream.close();
      representation = array.toByteArray();
    } catch (Exception exception) {
      throw new RuntimeException("could not serialize " + object + ": "
              + exception);
    }
  }

  // methods
  public Object copie() {
    try {
      ByteArrayInputStream array = new ByteArrayInputStream(representation);
      ObjectInputStream stream = new ObjectInputStream(array);
      return stream.readObject();
    } catch (Exception exception) {
      throw new RuntimeException("could not deserialize object: " + exception);
    }
  }

  public byte[] getRepresentation() {
    return representation;
  }

  public int getTaille() {
    return representation.length;
  }

}
