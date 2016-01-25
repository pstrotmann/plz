package org.strotmann.plz

class PlzNode {
	Integer plz
	String ort
	BigDecimal lat
	BigDecimal lon
	
	String toString() {"${ort},${plz},${lat},${lon}" }
}
