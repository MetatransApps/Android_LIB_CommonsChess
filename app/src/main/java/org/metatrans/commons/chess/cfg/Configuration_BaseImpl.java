package org.metatrans.commons.chess.cfg;


import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.chess.cfg.pieces.IConfigurationPieces;
import org.metatrans.commons.chess.logic.BoardConstants;


public class Configuration_BaseImpl implements IConfiguration, BoardConstants {
	
	
	private IConfigurationColours coloursCfg;
	private IConfigurationPieces piecesCfg;
	
	
	public Configuration_BaseImpl(IConfigurationColours _coloursCfg, IConfigurationPieces _piecesCfg) {
		coloursCfg = _coloursCfg;
		piecesCfg = _piecesCfg;
	}
	
	
	@Override
	public IConfigurationColours getColoursConfiguration() {
		return coloursCfg;
	}
	
	
	@Override
	public IConfigurationPieces getPiecesConfiguration() {
		return piecesCfg;
	}
}
