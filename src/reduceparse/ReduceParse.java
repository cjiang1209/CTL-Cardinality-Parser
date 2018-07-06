
package reduceparse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * Petri Net Markup Language Reducing Parser Translation for SMART.
 *
 * <P>Output a SMART-style file for a Petri net in PNML format
 *
 * @author Benjamin Smith	bensmith@iastate.edu
 * @see <a href="http://orcid.org/0000-0003-2607-9338">My ORCID is: 0000-0003-2607-9338</a>
 *
 */
public class ReduceParse {

    /**
     * @param args Takes a list of file names (.pnml format) and creates translated versions.
     */
    public static void main(String[] args) {
        // Obtain filenames from arguments

        // just for testing
        if (args.length == 0) {
            System.out.println("No filename arguments were given.  ");
        }


        //for (String filename : args) {
		String filename = args[0];
		System.out.println("Filename " + filename);
            PetriModel64 theModel = getModel64(filename);

            boolean isPNML = filename.endsWith(".pnml");

            String outfile = filename.substring(0, filename.length() - 5) + ".sm";
            if (!isPNML) {
                // file was not a pnml file (try it anyway?)
                System.out.println("Filename without .pnml extension.  Nothing written.");
            } else {
                writeSmart64(theModel, filename, outfile);
            }
        //}
    }


    /**
     * Parse a PNML formatted file into a Petri Net Model.
     *
     * @param filename	Name of the PNML file to be translated
     * @return a PetriModel object, or null if failed
     */
    public static PetriModel64 getModel64(String filename) {
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
	
	
	public static PetriModel64 reduceModel(PetriModel64 theModel) {
		// Include all arcs
		HashMap<String, TreeTransition> treeByName = new HashMap<>();
		HashMap<String, Integer> levelByName = new HashMap<>();
		int level = 1;
		for (PetriPlace64 pp : theModel.thePlaces) {
			levelByName.put(pp.name, level);
			level++;
		}
		for (PetriTransition pt : theModel.theTrans) treeByName.put(pt.id, new TreeTransition(new TreeMap<Integer, ECPair>()));
		
		for (PetriArc64 pa : theModel.theArcs) {
			if (theModel.placeSet.contains(pa.source)) {
				// source is place (constraint)
				int lev = levelByName.get(pa.source);
				TreeTransition tempTrans = treeByName.get(pa.target);
				ECPair tempPair = tempTrans.arcs.get(lev);
				if (tempPair == null) {
					tempPair = new ECPair(-pa.cardinality, pa.cardinality);
				} else {
					tempPair = new ECPair(tempPair.effect - pa.cardinality, pa.cardinality);
				}
				tempTrans.arcs.put(lev, tempPair);
			} else {
				// source is transition (partial effect)
				int lev = levelByName.get(pa.target);
				TreeTransition tempTrans = treeByName.get(pa.source);
				ECPair tempPair = tempTrans.arcs.get(lev);
				if (tempPair == null) {
					tempPair = new ECPair(pa.cardinality, 0l);
				} else {
					tempPair = new ECPair(tempPair.effect + pa.cardinality, tempPair.constraint);
				}
				tempTrans.arcs.put(lev,  tempPair);
			}
		}
		
		do {
			// discover disconnected places
			
			// discover constant places
			
			// discover "no-effect" transitions
			
			// discover "never-enabled" transitions
			
			// remove "effect" arcs from above transitions
			
		} while (false);  // repeat until no more changes made
		
		
		return theModel;
	}


    /**
     * Given an already parsed Petri Net Model, output in Smart-style format.
     *
     * @param theModel	The Petri Net Model
     * @param infile	The input file name
     * @param outfile	The name for the Smart file to be created
     */
    public static void writeSmart64(PetriModel64 theModel, String infile, String outfile) {
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
		// reverse here??
        for (PetriPlace64 p : theModel.thePlaces) {
            sb.append(separator);
            sb.append(theModel.translateName(p.name));
            separator = ",";
        }
        sb.append(");\n");


        // add the transitions
        for (PetriTransition p : theModel.theTrans) {
            sb.append("\t" + p + "\n");
        }

        // add the arcs
        for (PetriArc64 p : theModel.theArcs) {
            sb.append("\t" + p + "\n");
        }
		
		sb.append("\tvoid var := run_for_MCC;\n");

        // add the smart file ending information here
        sb.append("};\n");

        sb.append("\t// Parameters and Stuff here\n");
		sb.append("\t# ProcessGeneration MEDDLY\n");
		sb.append("\t# MeddlyVariableStyle ON_THE_FLY\n");
		sb.append("\t# MeddlyProcessGeneration OTF_IMPLICIT_SATURATION\n");
		sb.append("\t# MeddlyRSSNodeDeletion OPTIMISTIC\n");
        sb.append("\t# MeddlyNSFNodeDeletion OPTIMISTIC\n");
		sb.append("\t# VariableOrdering FORCEPARAM\n\n");
		sb.append("\tautomodel.var;\n");

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
