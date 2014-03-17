package com.ngzhian.dose;

import java.util.Comparator;

public class DosageComparator implements Comparator<Dosage> {

	@Override
	public int compare(Dosage a, Dosage b) {
		return (int) (a.getTime() - b.getTime());
	}

}
