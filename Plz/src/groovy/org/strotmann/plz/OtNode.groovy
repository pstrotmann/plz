package org.strotmann.plz

import java.util.List;

class OtNode {
	String place
	String name
	String isIn
	BigDecimal lat
	BigDecimal lon
	
	String toString() {"${place},${name},${isIn},${lat},${lon}" }
	
	static OtNode nearestOtNode(List nodeList, List punkt) {
		OtNode nearest = nodeList.first()
		nodeList.each {
			if (dist(it,punkt) < dist(nearest,punkt))
				nearest = it
		}
		nearest
	}
	
	static BigDecimal dist (OtNode n, List p) {
		Math.sqrt((n.lat - p[0])**2 + (n.lon - p[1])**2)
	}
}
