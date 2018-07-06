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
 * @author Ben
 */
public class CTLHandler extends DefaultHandler {
	String tmpValue;
	String propertyID;
	public ArrayList<String> formula; // collection of place names (for token counts)
	public ArrayList<String> firable; // collection of transition names
	public ArrayList<Property> props; // collection for all properties parsed
	
	ArrayDeque<BooleanFormula> stack;
        public  PetriModel64 theModel;
	//BooleanFormula left;
	//BooleanFormula right;
	//boolean goRight;
	
	/* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("property")) {
			// a new property, so reset things
			stack = new ArrayDeque<>();
		} else if (qName.equalsIgnoreCase("formula")) {
			//goRight = false;
		} else if (qName.equalsIgnoreCase("tokens-count")) {
			formula = new ArrayList<>();
		} else if (qName.equalsIgnoreCase("place-bound")) {
			formula = new ArrayList<>();
		} else if (qName.equalsIgnoreCase("is-fireable")) {
			firable = new ArrayList<>();
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
			propertyID = tmpValue;
		} else if (qName.equalsIgnoreCase("place")) {
			//System.out.println("Place name: " + tmpValue);
			formula.add(tmpValue);
		} else if (qName.equalsIgnoreCase("transition")) {
			firable.add(tmpValue);
		} else if (qName.equalsIgnoreCase("tokens-count")) {
			BooleanFormula tc = new TokenCountExpression(formula,theModel);
			stack.push(tc);
		} else if (qName.equalsIgnoreCase("place-bound")) {
			BooleanFormula tc = new TokenCountExpression(formula,theModel);
			stack.push(tc);
		} else if (qName.equalsIgnoreCase("is-fireable")) {
                        BooleanFormula tc = new FireableFormula(firable,theModel);
			stack.push(tc);
		} else if (qName.equalsIgnoreCase("integer-le")) {
			BooleanFormula right = stack.pop();
			BooleanFormula left = stack.pop();
			BooleanFormula le = LTEFormula.makeFormula(left, right);
			stack.push(le);
		} else if (qName.equalsIgnoreCase("conjunction")) {
			BooleanFormula right = stack.pop();
			BooleanFormula left = stack.pop();
			BooleanFormula and = AndFormula.makeFormula(left, right);
			stack.push(and);
		} else if (qName.equalsIgnoreCase("disjunction")) {
			BooleanFormula right = stack.pop();
			BooleanFormula left = stack.pop();
			BooleanFormula or = OrFormula.makeFormula(left, right);
			stack.push(or);
		} else if (qName.equalsIgnoreCase("until")) {
			BooleanFormula right = stack.pop();
			BooleanFormula left = stack.pop();
			BooleanFormula u = UFormula.makeFormula(left, right);
			stack.push(u);
		} else if (qName.equalsIgnoreCase("integer-sum")) {
			BooleanFormula right = stack.pop();
			BooleanFormula left = stack.pop();
			BooleanFormula u = PlusExpression.makeFormula(left, right);
			stack.push(u);
		} else if (qName.equalsIgnoreCase("integer-difference")) {
			BooleanFormula right = stack.pop();
			BooleanFormula left = stack.pop();
			BooleanFormula u = MinusExpression.makeFormula(left, right);
			stack.push(u);
		} else if (qName.equalsIgnoreCase("integer-constant")) {
			long num = Long.parseLong(tmpValue);
			BooleanFormula ic = new ConstantExpression(num);
			stack.push(ic);
		} else if (qName.equalsIgnoreCase("before")) {
			// do nothing?  strange name
		} else if (qName.equalsIgnoreCase("reach")) {
			// do nothing?  strange name
		} else if (qName.equalsIgnoreCase("negation")) {
			BooleanFormula neg = new NotFormula(stack.pop());
			stack.push(neg);
		} else if (qName.equalsIgnoreCase("all-paths")) {
			BooleanFormula all = new AFormula(stack.pop());
			stack.push(all);
		} else if (qName.equalsIgnoreCase("exists-path")) {
			BooleanFormula exist = new EFormula(stack.pop());
			stack.push(exist);
		} else if (qName.equalsIgnoreCase("globally")) {
			BooleanFormula globe = new GFormula(stack.pop());
			stack.push(globe);
		} else if (qName.equalsIgnoreCase("finally")) {
			BooleanFormula fin = new FFormula(stack.pop());
			stack.push(fin);
		} else if (qName.equalsIgnoreCase("next")) {
			BooleanFormula next = new XFormula(stack.pop());
			stack.push(next);
		} else if (qName.equalsIgnoreCase("property")) {
                    	Property aProp = new Property(propertyID, stack.pop());
			props.add(aProp);
			System.out.println(aProp);
			/*
			StringBuilder sb = new StringBuilder();
			sb.append("place-bound(");
			String delim = "";
			for (String s : formula) {
				sb.append(delim + s);
				delim = ",";
			}
			sb.append(") // " + propertyID);
			System.out.println("A FORMULA COMPLETE: " + props.size());
			*/
			//formula.clear();
		}
	

    }

    /* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
		tmpValue = new String(ch, start, length);
    }
    
    
}
