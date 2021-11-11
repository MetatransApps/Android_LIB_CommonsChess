package org.metatrans.commons.chess.model;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.cfg.difficulty.IConfigurationDifficulty;
import org.metatrans.commons.chess.GlobalConstants;
import org.metatrans.commons.chess.cfg.animation.Config_Animation_Base;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;
import org.metatrans.commons.chess.cfg.rules.IConfigurationRule;
import org.metatrans.commons.model.UserSettings_Base;


public class UserSettings extends UserSettings_Base implements GlobalConstants, Serializable {
	
	
	private static final long serialVersionUID = -6559817027983771505L;
	

	public int boardManagerID 	= IConfigurationRule.BOARD_MANAGER_ID_ALL_RULES;
	public int computerModeID 	= IConfigurationDifficulty.MODE_COMPUTER_ENGINE_1SEC;

	public int playerTypeWhite 	= PLAYER_TYPE_HUMAN;
	public int playerTypeBlack 	= PLAYER_TYPE_COMPUTER;

	public int uiPiecesID 		= IConfigurationPieces.CFG_PIECES_CUSTOM_2;

	public boolean infoEnabled 	= true;
	public boolean rotatedboard	= false;

	public boolean auto_player_enabled_white = false;
	public boolean auto_player_enabled_black = true;


	public int moveAnimationID = Config_Animation_Base.ID_NORMAL;
	
	
	public UserSettings() {

		fixFields("constructor");
	}
	
	
	private void writeObject(ObjectOutputStream oos) throws IOException {
		
	    fixFields("writeObject");
	    
	    // default serialization 
	    oos.defaultWriteObject();
	}
	
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
	    
	    // default deserialization
	    ois.defaultReadObject();
	    
	    fixFields("readObject");
	}
	
	
	@Override
	public String toString() {
		return "boardManagerID=" + boardManagerID + ", computerModeID=" + computerModeID
				+ ", playerTypeWhite=" + playerTypeWhite + ", playerTypeBlack=" + playerTypeBlack + ", uiColoursID=" + uiColoursID + ", uiPiecesID=" + uiPiecesID;
	}
	
	
	protected void fixFields(String op) {

		if (uiColoursID == 0) {

			uiColoursID 		= IConfigurationColours.CFG_COLOUR_GRAY;

	    	System.out.println("UserSettings: " + op + " - updating colour id");
	    }

		if (moveAnimationID == 0) {

			moveAnimationID = Config_Animation_Base.ID_NORMAL;
		}
	}
}
