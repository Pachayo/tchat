package calculette;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class Calculus implements Serializable {
	
	private ArrayList<String> treated_calculus;
	private int result = 0;
	private boolean isDeserializable = false;
	
	public Calculus(String calculus) {
		calculus = calculus.replaceAll("\\s+", "");
		this.treated_calculus = new ArrayList<String>(Arrays.asList(calculus.split("")));
	}
	
	public ArrayList<String> getArray() {
		return this.treated_calculus;
	}
	
	public int getResult() {
		return this.result;
	}
	
	public boolean isDeserializable() {
		return this.isDeserializable;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException, ClassNotFoundException {
		out.defaultWriteObject();
		out.writeInt(this.result);
		out.writeObject(this.treated_calculus);
		this.isDeserializable = true;
		out.writeBoolean(this.isDeserializable);
	}
	
	private void readObject(ObjectInputStream in) throws IOException {
		try {
			in.defaultReadObject();
			this.result = in.readInt();
			this.treated_calculus = (ArrayList<String>) in.readObject();
			this.isDeserializable = in.readBoolean();
			
		} catch(Exception e) {
			System.out.println(e.getClass()+" / "+e.getMessage());
		}
	} 
}