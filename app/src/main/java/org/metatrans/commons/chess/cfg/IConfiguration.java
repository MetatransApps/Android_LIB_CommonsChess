package org.metatrans.commons.chess.cfg;


import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;


public interface IConfiguration {
	
	public IConfigurationColours getColoursConfiguration();
	
	public IConfigurationPieces getPiecesConfiguration();
}
