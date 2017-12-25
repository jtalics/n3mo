package com.jtalics.n3mo;

import java.io.File;
import java.io.PrintStream;

public class N3mo {
	static boolean NOCONSOLE = false;
	static PrintStream outPrintStream;

	public static void main(String[] args) throws Exception {

		if (System.console() == null) {
			NOCONSOLE = true; // probably running in IDE or debugger
			// throw new Exception("Cannot open a console");
		}
		System.out.println("Current directory is " + System.getProperty("user.dir"));
		N3mo n3mo = new N3mo();
		String FileName = null;

		System.out.println(Constants.VersionStr);

		Satellite satellite = new Satellite(n3mo,new File("kepler.dat"));
		Site site = new Site();
		Ephemeris ephemeris = new Ephemeris(site, satellite);

		if (!NOCONSOLE) {
			FileName = System.console().readLine("Output file (RETURN for TTY): ");
		}
		if (FileName == null || FileName.length() > 0) {
			File file = new File(FileName, satellite.SatName + ".eph");
			outPrintStream = new PrintStream(file);
		} else
			outPrintStream = System.out;

		outPrintStream.println(satellite.SatName + " Element Set " + satellite.ElementSet);

		outPrintStream.println(site.SiteName);

		outPrintStream.println("Doppler calculated for freq = " + satellite.BeaconFreq);

		ephemeris.calc();
		
		outPrintStream.close();
	}
}