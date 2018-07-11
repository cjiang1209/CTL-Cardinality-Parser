package reduceparse;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



/**
 * @author Benjamin Smith bensmith@iastate.edu
 * @see <a href="http://orcid.org/0000-0003-2607-9338">My ORCID is: 0000-0003-2607-9338</a>
 *
 */
public class Utilities {
	static final int HASHBYTES = 20;
	static final int INDEXBYTES = 4;
	public static MessageDigest crypto;
	
	static final int SA_ITERATIONS = 5000;// number of simulated annealing iterations
	
	public Utilities() {
		try {
			crypto = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	
	public static byte [] getStringHash(String toHash) {
		byte [] byteString = toHash.getBytes();
		return crypto.digest(byteString);
	}
	
	
	
	
	public static String hexBytes(byte [] theBytes) {
		StringBuilder sb = new StringBuilder();
		String vals = "0123456789ABCDEF";
		
		for (byte b : theBytes) {
			int lower = b & 15;
			int upper = (b >> 4) & 15;
			sb.append(vals.substring(lower, lower + 1) + vals.substring(upper, upper + 1) + "-");
		}
		return sb.toString().substring(0, sb.length() - 1);
	}
	
	
	public static ArrayList<PetriPlace64> breadthFirst(PetriModel64 theModel) {
		ArrayList<PetriPlace64> sorted = new ArrayList<PetriPlace64>(theModel.thePlaces);
		Collections.sort(sorted);
		
		ArrayList<PetriPlace64> result = new ArrayList<PetriPlace64>();
		HashSet<String> used = new HashSet<String>();
		HashMap<String, PetriPlace64> namePlace = new HashMap<String, PetriPlace64>();
		
		for (PetriPlace64 p : theModel.thePlaces) {
			namePlace.put(p.name, p);
			
		}
		
		HashMap<String, HashSet<String>> connections = new HashMap<String, HashSet<String>>();
		for (PetriPlace64 p : theModel.thePlaces) connections.put(p.name, new HashSet<String>());
		for (PetriTransition pt : theModel.theTrans) connections.put(pt.id, new HashSet<String>());
		for (PetriArc64 pa : theModel.theArcs) connections.get(pa.source).add(pa.target);
		
		ArrayDeque<String> theQueue = new ArrayDeque<String>();
		
		
		for (PetriPlace64 p : sorted) {
			theQueue.clear();	// this outer loop exists because some place(s) might not be connected
			if (!used.contains(p.name)) {
				result.add(p);
				used.add(p.name);
				
				theQueue.addLast(p.name);
				
				while (!theQueue.isEmpty()) {
					String c = theQueue.removeFirst();
					
					for (String t : connections.get(c)) {
						for (String n : connections.get(t)) {
							if (!used.contains(n)) {
								result.add(namePlace.get(n));
								theQueue.addLast(n);
								used.add(n);
							}
						}
					}
				}
			}
		}
		
		return result;
	}
        
        
        public static ArrayList<PetriPlace64> cuthill(PetriModel64 theModel) {
			ArrayList<PetriPlace64> sorted = new ArrayList<PetriPlace64>(theModel.thePlaces);
			Collections.sort(sorted);
		
			ArrayList<PetriPlace64> result = new ArrayList<PetriPlace64>();
			HashSet<String> used = new HashSet<String>();
			HashMap<String, PetriPlace64> namePlace = new HashMap<String, PetriPlace64>();
			
			for (PetriPlace64 p : theModel.thePlaces) {
				namePlace.put(p.name, p);
			}
			
			HashMap<String, HashSet<String>> connections = new HashMap<String, HashSet<String>>();
			for (PetriPlace64 p : theModel.thePlaces) connections.put(p.name, new HashSet<String>());
			for (PetriTransition pt : theModel.theTrans) connections.put(pt.id, new HashSet<String>());
			for (PetriArc64 pa : theModel.theArcs) {
				connections.get(pa.source).add(pa.target);
				connections.get(pa.target).add(pa.source);
			}
			
			ArrayDeque<String> theQueue = new ArrayDeque<String>();
			ArrayList<PetriPlace64> toSort = new ArrayList<>();
			
			HashSet<String> transSet = new HashSet<>();
			HashSet<String> tpSet = new HashSet<>();
			HashSet<String> searching = new HashSet<>();
			int index = 0;
			PetriPlace64 current = sorted.get(index);
            while (result.size() < theModel.getNumPlaces()) {
				while (used.contains(current.name)) {
					current = sorted.get(++index);
				}
				toSort.clear();
				result.add(current);
				used.add(current.name);
				searching.add(current.name);
				while (!searching.isEmpty()) {
					transSet.clear();
					for (String s : searching) {
						transSet.addAll(connections.get(s));
					}
					for (String t : transSet) {
						for (String p : connections.get(t)) {
							if (!used.contains(p)) {
								toSort.add(namePlace.get(p));
								used.add(p);
							}
						}
					}

					Collections.sort(toSort);
					searching.clear();
					for (PetriPlace64 pp : toSort) {
						result.add(pp);
						searching.add(pp.name);
					}
					toSort.clear();
				}
			}
                
		Collections.reverse(result);
		return result;
	}
        
	
        public static void arcSetupDegreeAsc(PetriModel64 theModel) {
            HashMap<String, PetriPlace64> byName = new HashMap<>();
            for (PetriPlace64 pp: theModel.thePlaces) {
                pp.sortScoreA = 0.0;
                pp.sortScoreB = 0.0;
                byName.put(pp.name, pp);
            }
            for (PetriArc64 pa : theModel.theArcs) {
                if (theModel.placeSet.contains(pa.source)) {
                    // source is place
                    byName.get(pa.source).sortScoreA += -1.0;
                } else {
                    byName.get(pa.target).sortScoreA += -1.0;
                }
            }
        }
        
	public static void arcSetup(PetriModel64 theModel) {
		HashSet<String> placeSet = new HashSet<>();
		HashMap<String, Integer> levelByName = new HashMap<>();
		int lev = 1;
		HashMap<Integer, HashSet<ECPair>> uniques = new HashMap<>();
		for (PetriPlace64 pp : theModel.thePlaces) {
			placeSet.add(pp.name);
			levelByName.put(pp.name, lev);
			uniques.put(lev, new HashSet<ECPair>());
			lev++;
		}
		HashMap<String, TreeTransition> treeByName = new HashMap<>();
		int transCounter = 0;
		for (PetriTransition pt : theModel.theTrans) {
			TreeTransition temp = new TreeTransition(new TreeMap<Integer, ECPair>());
			temp.myNumber = transCounter;
			treeByName.put(pt.id, temp);
			transCounter++;
		}
		
		// Include all arcs
		for (PetriArc64 pa : theModel.theArcs) {
			if (placeSet.contains(pa.source)) {
				// source is place (constraint)
				lev = levelByName.get(pa.source);
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
				lev = levelByName.get(pa.target);
				TreeTransition tempTrans = treeByName.get(pa.source);
				ECPair tempPair = tempTrans.arcs.get(lev);
				if (tempPair == null) {
					tempPair = new ECPair(pa.cardinality, 0l);
				} else {
					tempPair = new ECPair(tempPair.effect + pa.cardinality, tempPair.constraint);
				}
				tempTrans.arcs.put(lev, tempPair);
			}
		}
		for (String s : treeByName.keySet()) {
			TreeTransition temp = treeByName.get(s);
			for (int i : temp.arcs.keySet()) {
				ECPair ecTemp = temp.arcs.get(i);
				if (ecTemp.effect == 0l) {
					// non-productive
					theModel.thePlaces.get(i - 1).sortScoreA += 1.0;
				} else {
					// productive
					if (!uniques.get(i).contains(ecTemp)) {
						// unique
						uniques.get(i).add(ecTemp);
					} else {
						theModel.thePlaces.get(i - 1).sortScoreB += 1.0;
					}
				}
			}
		}
		// finalize the "SCORE"s
		for (PetriPlace64 pp : theModel.thePlaces) {
			double toSort = pp.sortScoreA + pp.sortScoreB;
			if (toSort > 0.0) {
				pp.sortScoreA = pp.sortScoreA / toSort;
				pp.sortScoreB = pp.sortScoreB / toSort;
			} else {
				pp.sortScoreA = 0.0;
				pp.sortScoreB = 0.0;
			}
		}
		
	}
	
	
	
	public static void goodOrder(PetriModel64 theModel) {
		// this is an emergency solution to finding a good soups order for MCC
		Random arand = new Random(System.currentTimeMillis());
		
		double [] combo = new double[5];
		Arrays.fill(combo, 0.0);
			
						// Select here the preferred metric (default = 0)
		combo[0] = 1.0;// soups
		//combo[1] = 1.0;// sops
		//combo[2] = 1.0;// sous
		combo[3] = .001;// sos
		//combo[4] = 1.0;// sot
		
		ArrayList<PetriPlace64> given = new ArrayList<>(theModel.thePlaces);	// starting order
		HashSet<String> placeSet = new HashSet<>();
		for (PetriPlace64 pp : given) {
			placeSet.add(pp.name);
		}
		ArrayList<PetriPlace64> order = new ArrayList<>(given);
		Utilities.arcSetup(theModel);// experimental
		Utilities.arcSetupDegreeAsc(theModel);
		order = Utilities.cuthill(theModel);

		// To find an order, create the tree transitions
		HashMap<String, TreeTransition> treeByName = new HashMap<>();
		int transCounter = 0;
		for (PetriTransition pt : theModel.theTrans) {
			TreeTransition temp = new TreeTransition(new TreeMap<Integer, ECPair>());
			temp.myNumber = transCounter;
			treeByName.put(pt.id, temp);
			transCounter++;
			//System.out.println("TREEBYNAME " + temp);
		}
		HashMap<String, Integer> levelByName = new HashMap<>();
		int lev = 1;
		for (PetriPlace64 pp : order) {
			levelByName.put(pp.name, lev);
			lev++;
		}

		// Include all arcs
		for (PetriArc64 pa : theModel.theArcs) {
			if (placeSet.contains(pa.source)) {
				// source is place (constraint)
				lev = levelByName.get(pa.source);
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
				lev = levelByName.get(pa.target);
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
		
		for (String s : treeByName.keySet()) {
			treeByName.get(s).doSwap(0, 0);
			TreeTransition tt = treeByName.get(s);
			for (int i : tt.arcs.keySet()) {
				ECPair ec = tt.arcs.get(i);
				if (ec.effect != 0) {
					// add this to the location for place i
				}
			}
		}

		// Search for a better order
		TreeMap<Integer, PetriPlace64> placeTree = new TreeMap<>();
		lev = 1;
		for (PetriPlace64 pp : order) {
			placeTree.put(lev, pp);
			lev++;
		}
		
		TreeMap<Integer, PetriPlace64> bestOrder = new TreeMap<>(placeTree);
		double bestScore = Double.POSITIVE_INFINITY;//Integer.MAX_VALUE;
		double prevScore = bestScore;
		int bestSOT = Integer.MAX_VALUE;
		int bestSOS = Integer.MAX_VALUE;
		int bestSOUS = Integer.MAX_VALUE;
		int bestSOPS = Integer.MAX_VALUE;
		int bestSOUPS = Integer.MAX_VALUE;
		
		double SCORE = 0.0;
		
		int toughCount = 0;
		int maxiter = SA_ITERATIONS;	// 50000 iterations of Simulated annealing
		for (int iter = 0; iter < maxiter; iter++) {
			int sos = 0;
			int sops = 0;
			int sot = 0;
			
			
			ArrayList<TreeTransition> toUnique = new ArrayList<>(treeByName.values());
			Collections.sort(toUnique);
			int sous = toUnique.get(0).getSpan();
			sos = sous;
			sops = toUnique.get(0).getProductiveSpan();
			int soups = sops;
			sot = toUnique.get(0).top;
			int x = 1;
			for (x = 1; x < toUnique.size(); x++) {
				TreeTransition tt = toUnique.get(x);
				int unSpan = tt.getUniqueSpan(toUnique.get(x - 1));
				sous += unSpan;
				int prodSpan = tt.getProductiveSpan();
				
				
				sot += tt.top;
				sos += tt.getSpan();
				sops += prodSpan;
				soups += Math.min(unSpan, prodSpan);
				SCORE = soups * combo[0];
				SCORE += sops * combo[1];
				SCORE += sous * combo[2];
				SCORE += sos * combo[3];
				SCORE +=  sot * combo[4];
				if (SCORE > prevScore) break;
			}
			double enChange = (((double) (toUnique.size() - x)) / (double)toUnique.size());
			
			if (SCORE < bestScore) {//((soups < bestScore) || ((soups == bestScore) && (sos < bestSOS))) {// various tiebreakers possible
				bestOrder = new TreeMap(placeTree);
				bestSOS = sos;//(sops == bestScore) ? sos : bestSOS;
				bestSOPS = sops;
				bestSOUPS = soups;
				bestSOUS = sous;
				bestSOT = sot;
				SCORE = soups * combo[0];
				SCORE += sops * combo[1];
				SCORE += sous * combo[2];
				SCORE += sos * combo[3];
				SCORE +=  sot * combo[4];
				bestScore = SCORE;//soups;
				
//				System.out.println("AT ITER " + iter + " NEW BEST:: SOT " + sot + "  SOS " + sos + " HAS SOPS " + sops + "  HAS SOUS " + sous + " HAS SOUPS " + soups + " WITH SCORE " + SCORE);
				toughCount = 0;
			} else {
				toughCount++;
				double temperature = (double)(iter) / (double)(maxiter);
				
				if ((arand.nextDouble() < Math.exp(-enChange * temperature))) {//placeTree.size() )) {
					placeTree = new TreeMap(bestOrder);
					resetTreeTrans(bestOrder, treeByName, theModel);
					toughCount = 0;
					SCORE = bestScore;
				}
				
			}
			prevScore = SCORE;// * (1.0 / (1.0 - enChange));
			prevScore = Math.max(prevScore, bestScore);// preserve early breakout invariant
			
			int from = arand.nextInt(placeTree.size()) + 1;
			int to = arand.nextInt(placeTree.size()) + 1;
			
			PetriPlace64 tempPlace = placeTree.get(from);
			placeTree.put(from, placeTree.get(to));
			placeTree.put(to, tempPlace);
			for (TreeTransition tt : treeByName.values()) {
				tt.doSwap(from, to);
			}
		}
		// save the new order!!
		theModel.thePlaces.clear();
		for (int l : bestOrder.keySet()) {
			theModel.thePlaces.add(bestOrder.get(l));
		}
	}
	
	public static void resetTreeTrans(TreeMap<Integer, PetriPlace64> theOrder, HashMap<String, TreeTransition> treeByName, PetriModel64 theModel) {
		treeByName.clear();
		HashSet<String> placeSet = new HashSet<>();
		for (PetriTransition pt : theModel.theTrans) {
			treeByName.put(pt.id, new TreeTransition(new TreeMap<Integer, ECPair>()));
		}
		HashMap<String, Integer> levelByName = new HashMap<>();
		for (int lev = 1; lev <= theModel.getNumPlaces(); lev++) {
			levelByName.put(theOrder.get(lev).name, lev);
			placeSet.add(theOrder.get(lev).name);
		}
		
		// Include all arcs
		for (PetriArc64 pa : theModel.theArcs) {
			if (placeSet.contains(pa.source)) {
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
	}
}



