package logic.data_io.data_load;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import logic.transactions.exceptions.ParticipantNotFoundException;

public abstract class DataDeserialization {

	protected void load(String path_to_dir, String prefix) throws ParticipantNotFoundException {
		File dir = new File(path_to_dir);
		String[] files;
		Object deserialized;

		if (dir.exists() && dir.isDirectory()) {

			files = dir.list();

			for (String filename : files) {
				if (filename.startsWith(prefix) && filename.endsWith(".ser")) {
					deserialized = deserialize(filename);
					loadItem(deserialized);
				}
			}

		}
	}

	public abstract void load(String path_to_dir) throws ParticipantNotFoundException;

	protected abstract void loadItem(Object item) throws ParticipantNotFoundException;

	public Object deserialize(String filename) {

		Object item = null;

		try {
			// Reading the object from a file
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);

			// Method for deserialization of object
			item = in.readObject();

			in.close();
			file.close();

			System.out.println("Object has been deserialized ");

			// Rename to .old -> permite conservar la última versión
			File toDelete = new File(filename);
			
			if (toDelete.isFile()) {
				File rename = new File(filename + ".old");
				if (rename.exists()) {
					rename.delete();
					rename = new File(filename + ".old");
				}
				toDelete.renameTo(rename);
				toDelete.delete();
			}
			
		}

		catch (IOException ex) {
			System.err.println("IOException is caught");
		}

		catch (ClassNotFoundException ex) {
			System.err.println("ClassNotFoundException is caught");
		}

		return item;

	}

}
