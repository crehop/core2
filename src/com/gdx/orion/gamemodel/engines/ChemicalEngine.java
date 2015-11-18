package com.gdx.orion.gamemodel.engines;

/**
 * The stock low power chemical engine
 * 
 * @author jau
 *
 */
public class ChemicalEngine extends Engine {

	@Override
	public String getEngineName() {
		return "Chemical";
	}

	@Override
	public float maxThrust() {
		// TODO: default value should be pulled from a resource file
		return 100;
	}

}
