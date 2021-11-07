package org.metatrans.commons.chess.utils;


import android.app.Activity;
import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Set;

import org.metatrans.commons.chess.model.FieldSelection;


public class StorageUtils_BoardSelections {
	
	
	private static final String SELECTIONS_FILE_NAME = "selections";
	

	public static Set<FieldSelection>[][] readStorage(Context context) {
		
		ObjectInputStream input = null;
		
		try {
			
			File file = new File(context.getFilesDir(), SELECTIONS_FILE_NAME);

			if (file.exists()) {

				InputStream is = context.openFileInput(SELECTIONS_FILE_NAME);
				InputStream buffer = new BufferedInputStream(is);
				input = new ObjectInputStream(buffer);

				Set<FieldSelection>[][] selections = (Set<FieldSelection>[][]) input.readObject();

				return selections;
			}
			
		} catch (Throwable t) {

			t.printStackTrace();

		} finally {

			if (input != null) {

				try {
					input.close();

				} catch (IOException e) {}
			}
		}
		
		return null;
	}
	
	
	public static void writeStore(Context context, Set<FieldSelection>[][] selections) {

		ObjectOutput output = null;
		
		try {

			OutputStream os = context.openFileOutput(SELECTIONS_FILE_NAME, Activity.MODE_PRIVATE);
			OutputStream buffer = new BufferedOutputStream(os);
			output = new ObjectOutputStream(buffer);

			output.writeObject(selections);
			
			output.flush();
			
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {}
			}
		}
	}
}
