package com.gdx.orion.gamemodel.engines;

/**
 * The highest level super advanced freaky engine
 * 
 * @author jau
 */
public class HiggsInhibitorEngine extends Engine {

	@Override
	public String getEngineName() {
		return "Higgs Inhibitor";
	}

	@Override
	public float maxThrust() {
		// TODO: default value should be pulled from a resource file
		return 500;
	}

}
