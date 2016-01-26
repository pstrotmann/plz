package org.strotmann.plz

class PlzNode {
	Integer plz
	String ort
	BigDecimal lat
	BigDecimal lon
	
	String toString() {"${ort},${plz},${lat},${lon}" }
	
	static PlzNode nearestPlzNode(List nodeList, List punkt) {
		PlzNode nearest = nodeList.first()
		nodeList.each {
			if (dist(it,punkt) < dist(nearest,punkt)) 
				nearest = it
		}
		nearest
	}
	
	static BigDecimal dist (PlzNode n, List p) {
		Math.sqrt((n.lat - p[0])**2 + (n.lon - p[1])**2)
	}
}
