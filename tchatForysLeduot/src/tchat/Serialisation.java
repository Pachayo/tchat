/**
 *
 * @author pachayo
 * Avec l'aide de Baudouin des Vaux & Kerian Noel
 */

package tchat;

import java.io.*;
import java.util.*;


public class Serialisation implements Serializable {
	
    // Declaration des variables
    private String treated_serialisation;
    private int result = 0;
    private boolean isDeserializable = false;

    public Serialisation(String serialisation) {
        serialisation = serialisation.replaceAll("\\s+", "");
        this.treated_serialisation = serialisation;
    }

    public String getString() {
        return this.treated_serialisation;
    }

    public int getResult() {
        return this.result;
    }

    public boolean isDeserializable() {
        return this.isDeserializable;
    }

    private void writeObject(ObjectOutputStream sortie) throws IOException, ClassNotFoundException {
        sortie.defaultWriteObject();
        sortie.writeInt(this.result);
        sortie.writeObject(this.treated_serialisation);
        this.isDeserializable = true;
        sortie.writeBoolean(this.isDeserializable);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        try {
            in.defaultReadObject();
            this.result = in.readInt();

            // On force le String
            this.treated_serialisation = (String)in.readObject();
            this.isDeserializable = in.readBoolean();

        } catch(Exception e) {
                System.out.println(e.getClass()+" / "+e.getMessage());
        }
    } 
}