package com.gdx.orion.gamemodel.engines;

/**
 * The neutrino space engine (1st available upgrade)
 * 
 * @author jau
 */
public class NeutrinoEngine extends Engine {

	@Override
	public String getEngineName() {
		return "Neutrino";
	}

	@Override
	public float maxThrust() {
		// TODO: default value should be pulled from a resource file
		return 250;
	}
}
