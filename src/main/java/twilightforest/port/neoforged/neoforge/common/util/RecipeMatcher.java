package twilightforest.port.neoforged.neoforge.common.util;

import java.util.BitSet;
import java.util.List;
import java.util.function.Predicate;

public class RecipeMatcher {

	public static <T> int[] findMatches(List<T> inputs, List<? extends Predicate<T>> tests) {
		int elements = inputs.size();
		if (elements != tests.size()) {
			return null;
		}
		int[] matches = new int[elements];
		for (int i = 0; i < elements; i++) {
			matches[i] = -1;
		}
		BitSet used = new BitSet(elements);
		if (backtrack(inputs, tests, 0, matches, used)) {
			return matches;
		}
		return null;
	}

	private static <T> boolean backtrack(List<T> inputs, List<? extends Predicate<T>> tests, int testIndex, int[] matches, BitSet used) {
		if (testIndex >= tests.size()) {
			return true;
		}
		Predicate<T> test = tests.get(testIndex);
		for (int i = 0; i < inputs.size(); i++) {
			if (!used.get(i) && test.test(inputs.get(i))) {
				used.set(i);
				matches[i] = testIndex;
				if (backtrack(inputs, tests, testIndex + 1, matches, used)) {
					return true;
				}
				used.clear(i);
				matches[i] = -1;
			}
		}
		return false;
	}
}
