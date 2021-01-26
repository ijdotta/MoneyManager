package logic.data_io.data_exportation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import logic.transactions.Balance;
import logic.transactions.Participant;
import logic.transactions.Resumen;
import logic.transactions.Transaction;

public class SerializationDataExportationVisitor implements ExportationVisitor {

	@Override
	public void visit(Transaction transaction) {
		String filename = "t" + transaction.getId() + ".ser";
		serialize(transaction.makeSnapshot(), filename);
	}

	@Override
	public void visit(Participant participant) {
		String filename = "p" + participant.getId() + ".ser";
		serialize(participant, filename);
	}
	
	private void serialize(Object o, String filename) {
		try
        {    
            //Saving of object in a file 
            FileOutputStream file = new FileOutputStream(filename); 
            ObjectOutputStream out = new ObjectOutputStream(file); 
              
            // Method for serialization of object 
            out.writeObject(o); 
              
            out.close(); 
            file.close(); 
              
            System.out.println("Object has been serialized"); 
  
        } 
          
        catch(IOException ex) 
        { 
        	ex.printStackTrace();
            System.out.println("IOException is caught " + o.toString()); 
        } 
	}

	@Override
	public void visit(Balance balance) {
		//Does nothing
	}

	@Override
	public void visit(Resumen resumen) {
		//Does nothing
	}
	
}
