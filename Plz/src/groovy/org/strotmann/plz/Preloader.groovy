package org.strotmann.plz

import org.hibernate.SessionFactory;

class Preloader {
	
	def hibSession 
	Map nodeMap
	List ortsteilList
	FileInputStream osmStream
	
	void ini() {
		if (hibSession)
			println "Hibernate Session ok"
	}
	
	void aufbauNodeMap () {
		def BigDecimal minlat, maxlat, minlon, maxlon
		
		//nodeMap + ortsteilMap aufbauen
		osmStream = new FileInputStream ("/vol/mapWaltrop");
		InputStreamReader osmReader = new InputStreamReader(osmStream, "UTF-8")
		BufferedReader osm = new BufferedReader (osmReader)
		nodeMap = [:]
		ortsteilList = []
		Integer cntNodeMap = 0
		Integer cntOtMap = 0
		List otTags = []
		String nAct = ""
		osm.eachLine {String it ->
			if (it.trim().startsWith("<bounds")) {
				minlat = tagVal(it, "minlat").toBigDecimal()
				maxlat = tagVal(it, "maxlat").toBigDecimal()
				minlon = tagVal(it, "minlon").toBigDecimal()
				maxlon = tagVal(it, "maxlon").toBigDecimal()
				println it
			}
			if (it.trim().startsWith("<node")) {
				cntNodeMap++
				
				BigInteger nodeId = tagVal(it,'id').toBigInteger()
				nodeMap[nodeId] = [tagVal(it,'lat').toBigDecimal(),tagVal(it,'lon').toBigDecimal()]
				
				otTags = [null,null,null]
				nAct = it
			}
			
			if (it.trim().startsWith("</node") && otTags[0] && otTags[1] && otTags[2]) {
				cntOtMap++
				OtNode otNode = new OtNode(place:otTags[0],name:otTags[1],isIn:otTags[2],
											lat:tagVal(nAct, "lat").toBigDecimal(),lon:tagVal(nAct, "lon").toBigDecimal())
				nAct = ""
				ortsteilList << otNode
			}
			
			if (it.contains("<tag") && tagVal(it,'k') == "place" && tagVal(it,'v') in ["city","town","suburb"])
				otTags[0] = tagVal(it,'v')
			if (it.contains("<tag") && tagVal(it,'k') == "name")
				otTags[1] = tagVal(it,'v')
			if (it.contains("<tag") && tagVal(it,'k') == "is_in")
				otTags[2] = tagVal(it,'v')
		}
		osm.close()
		println "cntNode=${cntNodeMap}"
		ortsteilList.each {
			println it
		}
	}
	
	String tagVal (String line, String x) {
		Integer anfKey = line.trim().indexOf("${x}=") + x.length() + 2
		Integer endKey = line.trim().indexOf('"', anfKey)
		line.trim().substring(anfKey, endKey)
	}
}
