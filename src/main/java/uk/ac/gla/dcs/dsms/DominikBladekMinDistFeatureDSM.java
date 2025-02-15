package uk.ac.gla.dcs.dsms;

import org.terrier.structures.postings.Posting;
import org.terrier.structures.postings.BlockPosting;
import org.terrier.matching.dsms.DependenceScoreModifier;
import java.util.ArrayList;

/**
 * You should use this sample class to implement a proximity feature in Exercise 2.
 * TODO: Describe the function that your class implements
 * <p>
 * You can add your feature into a learned model by appending DSM:uk.ac.gla.IRcourse.DominikBladekMinDistFeatureDSM to the features.list file.
 * @author Dominik Bladek - 2144751b
 */
public class DominikBladekMinDistFeatureDSM extends DependenceScoreModifier {


	/** This class is passed the postings of the current document,
	 * and should return a score to represent that document.
	 */
	@Override
	protected double calculateDependence(
			Posting[] ips, //postings for this document (these are actually IterablePosting[])
			boolean[] okToUse,  //is each posting on the correct document?
			double[] phraseTermWeights, boolean SD //not needed
		)
	{

		final int numberOfQueryTerms = okToUse.length;

		/** New list containing only those postings that are ok to use */
		ArrayList<Posting> okToUseIps = new ArrayList<>();

		for (int i=0; i < okToUse.length; i++) {
			if (okToUse[i]) {
				okToUseIps.add(ips[i]);
			}
		}

		/** New list to make a BlockPostings of all Postings */
		ArrayList<BlockPosting> bps = new ArrayList<>();
		for (Posting ip : okToUseIps) {
			bps.add((BlockPosting) ip);
		}

		/** Calculates the minimum distances and stores them in a list */
		ArrayList<Integer> distances = new ArrayList<>();
		for (int i=0; i < okToUseIps.size(); i++) {
			for (int j=i+1; j < okToUseIps.size(); j++) {
				distances.add(min_dist(bps.get(i), bps.get(j)));
			}
		}
		/** Gets the mean of minimum distances */
		int sum = 0;
		for (int d : distances) sum += d;

		double average = (double)sum / (double)distances.size();

		return average;
	}

	protected int min_dist(BlockPosting term1, BlockPosting term2) {
		int[] positions1 = term1.getPositions();
		int[] positions2 = term2.getPositions();

		int min_distance = Math.abs(positions1[0] - positions2[0]);

		for (int pos1_index = 0; pos1_index < positions1.length; pos1_index++) {
			for (int pos2_index = 0; pos2_index < positions2.length; pos2_index++) {

				int current_distance = Math.abs(positions1[pos1_index] - positions2[pos2_index]);

				if (current_distance < min_distance) {
					min_distance = current_distance;
				}
			}
		}

		return min_distance;
	}

	/** You do NOT need to implement this method */
	@Override
	protected double scoreFDSD(int matchingNGrams, int docLength) {
		throw new UnsupportedOperationException();
	}


	@Override
	public String getName() {
		return "ProxFeatureDSM_MYFUNCTION";
	}

}
