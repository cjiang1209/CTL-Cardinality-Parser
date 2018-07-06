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
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
/**
 *
 * @author BenjaminSmith
 */
public class BoundParse {
	 public static void main(String[] args) {
        // Obtain filenames from arguments (first is model, second is bounds)

        // just for testing
        if (args.length == 0) {
            System.out.println("No filename arguments were given.  ");
        }


        String filename = args[0];
		System.out.println("PNML FILE: " + filename);
            PetriModel64 theModel = getModel64x(filename);

            boolean isPNML = filename.endsWith(".pnml");

            String outfile = filename.substring(0, filename.length() - 5) + ".sm";
           
        String boundsFile = args[1];
		System.out.println("Bounds file is: " + boundsFile);
		ArrayList<TokenCountExpression> thoseBounds = getBounds(boundsFile);
		for (TokenCountExpression tk : thoseBounds) {
			System.out.println(tk.smartOut(theModel) );
		}
		
		 if (!isPNML) {
                // file was not a pnml file (try it anyway?)
                System.out.println("Filename without .pnml extension.  Nothing written.");
            } else {
                writeSmart64(theModel, thoseBounds, filename, outfile);
            }
    }


    /**
	 * Parse a PNML formatted file into a Petri Net Model.
	 *
	 * @param filename	Name of the PNML file to be translated
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
			//Logger.getLogger(FastPNML64.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			//Logger.getLogger(FastPNML64.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	public static ArrayList<TokenCountExpression> getBounds(String filename) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		UpperHandler handler = null;
		try {
			SAXParser saxParser = saxParserFactory.newSAXParser();
			handler = new UpperHandler();
			handler.formula = new ArrayList<>();
			handler.varNum = 1;
			handler.bounds = new ArrayList<>();
			// handler.trans = new ArrayList<PetriTransition>();
			//handler.arcs = new ArrayList<>();

			saxParser.parse(new File(filename), handler);
			
			//return new PetriModel64(handler.places, handler.trans, handler.arcs);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (org.xml.sax.SAXException ex) {
			//Logger.getLogger(FastPNML64.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			//Logger.getLogger(FastPNML64.class.getName()).log(Level.SEVERE, null, ex);
		}
		return handler.bounds;
	}
	
	
	/**
     * Given an already parsed Petri Net Model, output in Smart-style format.
     *
     * @param theModel	The Petri Net Model
     * @param infile	The input file name
     * @param outfile	The name for the Smart file to be created
     */
    public static void writeSmart64(PetriModel64 theModel, ArrayList<TokenCountExpression> theBounds, String infile, String outfile) {
        // Create the Smart output
        StringBuilder sb = new StringBuilder();
        sb.append("// Smart file automatically generated from source PNML file\n");
        sb.append("// " + infile + "\n");
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
		for (TokenCountExpression tke : theBounds) {
			sb.append(tke.smartOut(theModel) + "\n");
		}

        // add the smart file ending information here
        sb.append("};\n");

        sb.append("\t// Parameters and Stuff here\n");
        sb.append("\t# ProcessGeneration MEDDLY\n");
		sb.append("\t# MeddlyVariableStyle ON_THE_FLY\n");
		sb.append("\t# MeddlyProcessGeneration OTF_IMPLICIT_SATURATION\n");
		sb.append("\t# MeddlyRSSNodeDeletion OPTIMISTIC\n");
		sb.append("\t# MeddlyNSFNodeDeletion OPTIMISTIC\n");
		sb.append("\t# VariableOrdering FORCEPARAM\n\n");

		// add the upper bounds outputs
		// add upperbounds lines
		for (TokenCountExpression tke : theBounds) {
			sb.append(tke.smartOutBottom() + "\n");
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
}
