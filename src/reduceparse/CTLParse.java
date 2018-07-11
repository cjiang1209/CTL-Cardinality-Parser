/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reduceparse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author Ben
 */
public class CTLParse {
	public static void main(String[] args) {
		// Obtain filenames from arguments (first is model, second is bounds)

		// just for testing
		if (args.length == 0) {
			System.out.println("No filename arguments were given.  ");
		}

		String filename = args[0];
		PetriModel64 theModel = getModel64x(filename);
		Utilities.goodOrder(theModel);
		if (!filename.endsWith(".pnml")) {
			// file was not a pnml file (try it anyway?)
			System.out.println("Filename without .pnml extension.  Nothing written.");
			return;
		}

		String RCPropertyFile = args[1];
		System.out.println("CTL Cardinality file is: " + RCPropertyFile);
		System.out.println();
		ArrayList<Property> thoseProperties = getCTL(RCPropertyFile, theModel);

		for (Property p : thoseProperties) {
			p.evaluate();
			p.normalize();
		}

		System.out.println();
		System.out.println(String.format("%-50s %7s %7s %7s %7s %7s %7s %7s",
				"Property", "Const", "Tempor", "Nested", "Linear", "ATCL", "ECTL", "Select"));
		for (Property p : thoseProperties) {
			System.out
					.println(String.format(
							"%-50s %7s %7s %7s %7s %7s %7s %7s",
							p.propID,
							p.isConstant(),
							p.isTemporal(),
							p.isNested(),
							p.hasLinearTemplate(),
							p.isACTL(),
							p.isECTL(),
							p.isTemporal() && p.isNested()
									&& !p.hasLinearTemplate() ? "***" : ""));
		}

		System.out.println();
		for (Property p : thoseProperties) {
			if (p.isACTL()) {
				p.negate();
			}
			if (p.isTemporal() && p.isNested() && !p.hasLinearTemplate()) {
				System.out.println(p);
			}
		}

//		String outfile = filename.substring(0, filename.length() - 5) + ".sm";
//		writeSmart64(theModel, thoseProperties, filename, outfile);
		
		for (Property p : thoseProperties) {
			if (p.isTemporal() && p.isNested() && !p.hasLinearTemplate()) {
				String outfile = p.propID + ".sm";
				writeSmart64SingleProperty(theModel, p, filename, outfile);
			}
		}
	}

	/**
	 * Parse a PNML formatted file into a Petri Net Model.
	 *
	 * @param filename
	 *            Name of the PNML file to be translated
	 * @return a PetriModel object, or null if failed
	 */
	public static PetriModel64 getModel64x(String filename) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			PNMLHandler64 handler = new PNMLHandler64();
			handler.places = new ArrayList<PetriPlace64>();
			handler.trans = new ArrayList<PetriTransition>();
			handler.arcs = new ArrayList<>();

			saxParser.parse(new File(filename), handler);
			return new PetriModel64(handler.places, handler.trans, handler.arcs);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (org.xml.sax.SAXException ex) {
			// Logger.getLogger(FastPNML64.class.getName()).log(Level.SEVERE,
			// null, ex);
		} catch (IOException ex) {
			// Logger.getLogger(FastPNML64.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
		return null;
	}

	public static ArrayList<Property> getCTL(String filename,
			PetriModel64 theModel) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		CTLHandler handler = null;
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			handler = new CTLHandler();
			handler.formula = new ArrayList<>();
			handler.props = new ArrayList<Property>();
			handler.theModel = theModel;
			// handler.trans = new ArrayList<PetriTransition>();
			// handler.arcs = new ArrayList<>();
			saxParser.parse(new File(filename), handler);
			// return new PetriModel64(handler.places, handler.trans,
			// handler.arcs);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (org.xml.sax.SAXException ex) {
			// Logger.getLogger(FastPNML64.class.getName()).log(Level.SEVERE,
			// null, ex);
		} catch (IOException ex) {
			// Logger.getLogger(FastPNML64.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
		return handler.props;
	}

	/**
	 * Given an already parsed Petri Net Model, output in Smart-style format.
	 *
	 * @param theModel
	 *            The Petri Net Model
	 * @param infile
	 *            The input file name
	 * @param outfile
	 *            The name for the Smart file to be created
	 */
	public static void writeSmart64(PetriModel64 theModel,
			ArrayList<Property> theProperties, String infile, String outfile) {
		// Create the Smart output
		StringBuilder sb = new StringBuilder();
		sb.append("// Smart file automatically generated from source PNML file\n");
		sb.append("// " + infile + "\n");

		sb.append("\n");
		sb.append("// Parameters and Stuff here\n");
		sb.append("# ProcessGeneration MEDDLY\n");
		sb.append("# MeddlyVariableStyle ON_THE_FLY\n");
		sb.append("# MeddlyProcessGeneration OTF_IMPLICIT_SATURATION\n");
		sb.append("# MeddlyRSSNodeDeletion OPTIMISTIC\n");
		sb.append("# MeddlyNSFNodeDeletion OPTIMISTIC\n");
		sb.append("# VariableOrdering FORCEPARAM\n");
		sb.append("# ParseTemporalOperators true\n");
		sb.append("# CompactBMCEncoding false\n");
		sb.append("\n");

		sb.append("pn automodel := {\n");

		// add the places
		for (PetriPlace64 p : theModel.thePlaces) {
			sb.append("\t" + p + "\n");
		}

		// add the order partition line
		sb.append("\npartition(");
		String separator = "";
		// reverse here (smart partition is ordered top to bottom)
		Collections.reverse(theModel.thePlaces);

		for (PetriPlace64 p : theModel.thePlaces) {
			sb.append(separator);
			sb.append(theModel.translateName(p.name));
			separator = ",";
		}
		sb.append(");\n");
		Collections.reverse(theModel.thePlaces);

		// add the transitions
		for (PetriTransition p : theModel.theTrans) {
			sb.append("\t" + p + "\n");
		}

		// add the arcs
		for (PetriArc64 p : theModel.theArcs) {
			sb.append("\t" + p + "\n");
		}

		// add upperbounds lines
		for (Property p : theProperties) {
			if (p.isTemporal() && p.isNested() && !p.hasLinearTemplate()) {
				sb.append(p.smartOut(theModel) + "\n");
			}
		}

		// add the smart file ending information here
		sb.append("};\n");

		// add the upper bounds outputs
		// add upperbounds lines
		for (Property p : theProperties) {
			if (p.isTemporal() && p.isNested() && !p.hasLinearTemplate()) {
				sb.append(p.smartOutBottom() + "\n");
			}
		}

		// Store the Smart file output
		PrintWriter pw;
		try {
			pw = new PrintWriter(outfile);

			pw.write(sb.toString());
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Given an already parsed Petri Net Model, output in Smart-style format.
	 * The file contains only one property to be checked.
	 *
	 * @param theModel
	 *            The Petri Net Model
	 * @param theProperty
	 *            The property to be contained in the Smart file
	 * @param infile
	 *            The input file name
	 * @param outfile
	 *            The name for the Smart file to be created
	 */
	public static void writeSmart64SingleProperty(PetriModel64 theModel,
			Property theProperty, String infile, String outfile) {
		// Create the Smart output
		StringBuilder sb = new StringBuilder();
		sb.append("// Smart file automatically generated from source PNML file\n");
		sb.append("// " + infile + "\n");

		sb.append("\n");
		sb.append("// Parameters and Stuff here\n");
		sb.append("# ProcessGeneration MEDDLY\n");
		sb.append("# MeddlyVariableStyle ON_THE_FLY\n");
		sb.append("# MeddlyProcessGeneration OTF_IMPLICIT_SATURATION\n");
		sb.append("# MeddlyRSSNodeDeletion OPTIMISTIC\n");
		sb.append("# MeddlyNSFNodeDeletion OPTIMISTIC\n");
		sb.append("# VariableOrdering FORCEPARAM\n");
		sb.append("# ParseTemporalOperators true\n");
		sb.append("# CompactBMCEncoding false\n");
		sb.append("\n");

		sb.append("pn automodel := {\n");

		// add the places
		for (PetriPlace64 p : theModel.thePlaces) {
			sb.append("\t" + p + "\n");
		}

		// add the order partition line
		sb.append("\npartition(");
		String separator = "";
		// reverse here (smart partition is ordered top to bottom)
		Collections.reverse(theModel.thePlaces);

		for (PetriPlace64 p : theModel.thePlaces) {
			sb.append(separator);
			sb.append(theModel.translateName(p.name));
			separator = ",";
		}
		sb.append(");\n");
		Collections.reverse(theModel.thePlaces);

		// add the transitions
		for (PetriTransition p : theModel.theTrans) {
			sb.append("\t" + p + "\n");
		}

		// add the arcs
		for (PetriArc64 p : theModel.theArcs) {
			sb.append("\t" + p + "\n");
		}

		sb.append(theProperty.smartOut(theModel) + "\n");

		// add the smart file ending information here
		sb.append("};\n");

		// add the upper bounds outputs
		// add upperbounds lines
		sb.append(theProperty.smartOutBottom() + "\n");

		// Store the Smart file output
		PrintWriter pw;
		try {
			pw = new PrintWriter(outfile);

			pw.write(sb.toString());
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
