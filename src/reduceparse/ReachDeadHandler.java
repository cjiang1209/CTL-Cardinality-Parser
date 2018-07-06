/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reduceparse;

import java.util.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author BenjaminSmith
 */
public class ReachDeadHandler extends DefaultHandler {
	String tmpValue;
	String propertyID;
	public ArrayList<String> formula; // collection of place names
	/* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("property")) {
			//String id = attributes.getValue("id");
			//System.out.println("Property found");
		} else if (qName.equalsIgnoreCase("place")) {
            //String id = attributes.getValue("id");
			//String placeName = id;
			//placeInit = 0L;
           // inPlace = true;
			//System.out.println("Place found");
        } else if (qName.equalsIgnoreCase("initialMarking")) {
            //placeMarked = true;
        } else if (qName.equalsIgnoreCase("transition")) {
            String id = attributes.getValue("id");
           // tranName = id;
           // trans.add(new PetriTransition(tranName));
        } else if (qName.equalsIgnoreCase("arc")) {
            String id = attributes.getValue("id");
            String source = attributes.getValue("source");
            String target = attributes.getValue("target");
           // arcID = id;
           // arcSource = source;
           // arcTarget = target;
           // arcCardinality = 1L;	// default value, might get changed later
           // inArc = true;
        } else if (true) {// (inArc && (qName.equalsIgnoreCase("inscription"))) {
           // arcCard = true;
        }
    }


    /* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("id")) {
			//System.out.println("ID: " + tmpValue);
			propertyID = tmpValue;
		} else if (qName.equalsIgnoreCase("place")) {
			//System.out.println("Place name: " + tmpValue);
			formula.add(tmpValue);
		} else if (qName.equalsIgnoreCase("formula")) {
			StringBuilder sb = new StringBuilder();
			sb.append("place-bound(");
			String delim = "";
			for (String s : formula) {
				sb.append(s + delim);
				delim = ",";
			}
			sb.append(") // " + propertyID);
			System.out.println("A FORMULA COMPLETE: " + sb.toString());
			
			formula.clear();
		}
		/*
        if (qName.equalsIgnoreCase("text")) {
            if (placeMarked) {
                long marking = Long.parseLong(tmpValue);
                placeInit = marking;
                hasMarking = true;

            } else if (arcCard) {
                long cardinality = Long.parseLong(tmpValue);
                arcCardinality = cardinality;

                arcCard = false;
            }
        } else if (qName.equalsIgnoreCase("place")) {
            if (inPlace) {
                if (hasMarking) {
                    places.add(new PetriPlace64(placeName, placeInit));
                } else {
                    places.add(new PetriPlace64(placeName));
                }
                inPlace = false;
                hasMarking = false;
            }
            placeMarked = false;
        } else if (qName.equalsIgnoreCase("arc")) {
            PetriArc64 temp = new PetriArc64(arcID, arcSource, arcTarget, arcCardinality);
            arcs.add(temp);

            inArc = false;
            arcCard = false;
        } else if (qName.equalsIgnoreCase("initialMarking")) {
            placeMarked = false;
        }
		*/

    }

    /* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
		tmpValue = new String(ch, start, length);
    }
}
