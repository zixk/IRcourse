package uk.ac.gla.dcs.dsms;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.terrier.indexing.IndexTestUtils;
import org.terrier.structures.Index;
import org.terrier.structures.postings.BlockPosting;
import org.terrier.structures.postings.IterablePosting;
import org.terrier.tests.ApplicationSetupBasedTest;
import org.terrier.utility.ApplicationSetup;

public class DominikBladekTestMinDistFeatureDSM extends ApplicationSetupBasedTest
{
	@Test public void testOneDocTwoTerms() throws Exception {

		//make an index with a single sample document
		ApplicationSetup.setProperty("termpipelines", "");
		Index index = IndexTestUtils.makeIndexBlocks(
				new String[]{"doc1"}, 
				new String[]{"The quick brown fox jumps over the lazy dog"});

		//get posting iterators for two terms 'fox' and 'jumps'
		IterablePosting[] ips = new IterablePosting[2];
		ips[0] = index.getInvertedIndex().getPostings(index.getLexicon().getLexiconEntry("fox"));
		ips[1] = index.getInvertedIndex().getPostings(index.getLexicon().getLexiconEntry("jumps"));
		ips[0].next();
		ips[1].next();
		assertEquals(0, ips[0].getId());
		assertEquals(0, ips[1].getId());
		System.out.println("Positions of term 'fox'="+ Arrays.toString( ((BlockPosting)ips[0]).getPositions()));
		System.out.println("Positions of term 'jumps'="+ Arrays.toString( ((BlockPosting)ips[1]).getPositions()));

		DominikBladekMinDistFeatureDSM sample = new DominikBladekMinDistFeatureDSM();
		double score = sample.calculateDependence(
            ips, //posting lists
            new boolean[]{true,true},  //is this posting list on the correct document?
            new double[]{1d,1d}, false//doesnt matter
		);
		System.out.println(score);
		//TODO: make your assertion about what the score should be
		assertEquals(1.0d, score, 0.0d);

		Index index2 = IndexTestUtils.makeIndexBlocks(
				new String[]{"doc1"}, 
				new String[]{"The quick brown fox jumps over the lazy dog"});

		//get posting iterators for two terms 'quick' and 'dog'
		IterablePosting[] ips2 = new IterablePosting[2];
		ips2[0] = index2.getInvertedIndex().getPostings(index2.getLexicon().getLexiconEntry("quick"));
		ips2[1] = index2.getInvertedIndex().getPostings(index2.getLexicon().getLexiconEntry("dog"));
		ips2[0].next();
		ips2[1].next();
		assertEquals(0, ips2[0].getId());
		assertEquals(0, ips2[1].getId());
		System.out.println("Positions of term 'quick'="+ Arrays.toString( ((BlockPosting)ips2[0]).getPositions()));
		System.out.println("Positions of term 'dog'="+ Arrays.toString( ((BlockPosting)ips2[1]).getPositions()));
		
		DominikBladekMinDistFeatureDSM sample2 = new DominikBladekMinDistFeatureDSM();
		double score2 = sample2.calculateDependence(
            ips2, //posting lists
            new boolean[]{true,true},  //is this posting list on the correct document?
            new double[]{1d,1d}, false//doesnt matter
		);
		System.out.println(score2);
		//TODO: make your assertion about what the score should be
		assertEquals(7.0d, score2, 0.0d);
	}
}
