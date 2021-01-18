package logic.data_load;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public abstract class DataDeserialization {

	public void load(String path_to_dir) {
		File dir = new File(path_to_dir);

		if (dir.exists() && dir.isDirectory()) {

			for (File file : dir.listFiles()) {
				Object item = deserialize(file.getPath());
				loadItem(item);
			}

		}
	}

	protected abstract void loadItem(Object item);

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
		}

		catch (IOException ex) {
			System.out.println("IOException is caught");
		}

		catch (ClassNotFoundException ex) {
			System.out.println("ClassNotFoundException is caught");
		}
		
		return item;

	}

}
