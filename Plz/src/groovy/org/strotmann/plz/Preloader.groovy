package org.strotmann.plz

import org.hibernate.Session
import org.hibernate.SessionFactory;

class Preloader {
	
	def String eingabeMap
	def Session hibSession 
	Map nodeMap
	List ortsteilList
	FileInputStream osmStream
	Map plzNodeMap
	List <PlzNode> nodeList
	Integer cntLoad
	Integer cntRead
	Integer cntAdr
	def BigDecimal minlat, maxlat, minlon, maxlon
		
	void aufbauNodeMap () {
		
		//nodeMap + ortsteilMap aufbauen
		BufferedReader osm = mapReader (eingabeMap)
		nodeMap = [:]
		ortsteilList = []
		Integer cntNodeMap = 0
		Integer cntOtMap = 0
		cntLoad = 0
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
			
			if (it.trim().startsWith("</node") && otTags[0] && otTags[1]) {
				cntOtMap++
				OtNode otNode = new OtNode(place:otTags[0],name:otTags[1],isIn:otTags[2]?:otTags[1],
											lat:tagVal(nAct, "lat").toBigDecimal(),lon:tagVal(nAct, "lon").toBigDecimal())
				nAct = ""
				ortsteilList << otNode
				Ortsteil ot = new Ortsteil(typ:otNode.place,name:otNode.name,liegtIn:otNode.isIn)
				if (ot.save())
					cntLoad++
				else
					ot.errors.each {
					println it
				}
			}
			
			if (it.contains("<tag") && tagVal(it,'k') == "place" && tagVal(it,'v') in ["city","town","suburb","hamlet","village"])
				otTags[0] = tagVal(it,'v')
			if (it.contains("<tag") && tagVal(it,'k') == "name")
				otTags[1] = tagVal(it,'v')
			if (it.contains("<tag") && tagVal(it,'k') == "is_in")
				otTags[2] = tagVal(it,'v')
		}
		osm.close()
		println "cntNode=${cntNodeMap},cntOtMap=${cntOtMap},cntLoad=${cntLoad}"
		ortsteilList.each {
			println it
		}
		hibSession.flush()
	}
	
	void sichAdr () {
		println "Start sichere Adressen"
		BufferedReader osm = mapReader (eingabeMap)
		cntRead = 0
		cntLoad = 0
		cntAdr = 0
		Integer cntDup = 0
		List adrL
		plzNodeMap = [:]
		String nodeActive = ""
		BigInteger refActive = 0
		Boolean lActive = false
		osm.eachLine {String it ->
			cntRead++
			if (it.trim().startsWith("<nd"))
				refActive = tagVal(it, "ref").toBigInteger()
			if (it.trim().startsWith("<node") || it.trim().startsWith("<way")) {
				adrL = [null,null,null,null,null]
				lActive = true
				if (it.trim().startsWith("<node"))
					nodeActive = it
			}
			if (it.trim().startsWith("</node") || it.trim().startsWith("</way")) {
				adrL = komplettOrtPlz(adrL)
				if (adrL[0] && adrL[1] && adrL[2] && adrL[3]) {
					cntAdr++
					def Adresse adresse = new Adresse(ort:adrL[0],hnr:adrL[1],plz:adrL[2],str:adrL[3])
										
					PlzNode plzNode = new PlzNode(plz:adresse.plz,ort:adresse.ort)
					
					if (it.trim().startsWith("</node")) {
						plzNode.lat = tagVal(nodeActive, "lat").toBigDecimal()
						plzNode.lon = tagVal(nodeActive, "lon").toBigDecimal()
						nodeActive = ""
					}
					else {//</way
						List punkt = nodeMap[refActive]
						plzNode.lat = punkt[0]
						plzNode.lon = punkt[1]
						refActive = 0
					}
					String mapKey = PlzNode.plzNodeKey (minlat,maxlat,minlon,maxlon,plzNode.lat,plzNode.lon)
					if (plzNodeMap[mapKey])
						nodeList = plzNodeMap[mapKey]
					else
						nodeList = []
					nodeList << plzNode
					plzNodeMap[mapKey] = nodeList
					adresse.ortsteil = findOt([plzNode.lat,plzNode.lon], adresse.ort)
					if (adresse.save())
						cntLoad++
					else
						cntDup++
						
					if (adrL[4]) {
						//2. Adresse bilden
						cntAdr++
						def Adresse adresse2 = new Adresse(ort:adresse.ort,hnr:adrL[4],plz:adresse.plz,str:adresse.str,ortsteil:adresse.ortsteil)
						if (adresse2.save())
							cntLoad++
						else
							cntDup++
					}
					
					if (cntRead %1000 == 0) {
						hibSession.flush()
						println "${cntLoad} sichere Adressen geladen, Duplikate nicht geladen: ${cntDup}"
					}
				}
				
				lActive = false
			}
			
			adrL = tagL (it, adrL)
		
			if (cntRead %1000000 == 0) {
				println "${cntRead} Sätze gelesen,${cntAdr} sichere Adressen gefunden"
			}
		}
	}
	
	void herlAdr () {
		println "Start Herleitung"
		BufferedReader osm = mapReader (eingabeMap)
		
		String nodeActive = ""
		BigInteger refActive = 0
		Integer cntDup = 0
		Boolean lActive = false
		Integer cntAdrHerl = 0
		Integer cntRead = 0
		List adrL
		osm.eachLine {String it ->
			cntRead++
			if (it.trim().startsWith("<nd"))
				refActive = tagVal(it, "ref").toBigInteger()
			if (it.trim().startsWith("<node") || it.trim().startsWith("<way")) {
				adrL = [null,null,null,null,null,null,null]
				lActive = true
				if (it.trim().startsWith("<node"))
					nodeActive = it
			}
			if (it.trim().startsWith("</node") || it.trim().startsWith("</way")) {
				adrL = komplettOrtPlz(adrL)
				if (!adrL[0] && !adrL[1] && !adrL[2] && !adrL[3] && adrL[5] && adrL[6]) {
					adrL[1] = ' '
					adrL[3] = adrL[6]
				}
				if (!adrL[0] && adrL[1] && !adrL[2] && adrL[3]) {
					cntAdr++
					cntAdrHerl++
					def Adresse adresse = new Adresse()
					
					List <BigDecimal> punkt
					if (it.trim().startsWith("</node")) {
						punkt = [tagVal(nodeActive, "lat").toBigDecimal(),tagVal(nodeActive, "lon").toBigDecimal()]
						nodeActive = ""
					}
					else {//</way
						punkt = nodeMap[refActive]
						refActive = 0
					}
					//jetzt aus nodeList den nächsten Punkt heraussuchen
					//ort und plz durch Nachbarschaft ermitteln
					BigDecimal bigDec0 = punkt[0]
					BigDecimal bigDec1 = punkt[1]
					String mapKey = PlzNode.plzNodeKey (minlat,maxlat,minlon,maxlon,bigDec0,bigDec1)
					if (plzNodeMap[mapKey])
						nodeList = plzNodeMap[mapKey]
					else
					if ((mapKey.toInteger()+1).toString().padLeft(2, '0').substring(0, 2))
						nodeList = plzNodeMap[(mapKey.toInteger()+1).toString().padLeft(2, '0').substring(0, 2)]
					else
						nodeList = plzNodeMap[(mapKey.toInteger()-1).toString().padLeft(2, '0').substring(0, 2)]
					if (nodeList && nodeList.size > 0) {
						PlzNode plzNode = PlzNode.nearestPlzNode(nodeList, punkt)
						adresse.ort = plzNode.ort
						adresse.plz = plzNode.plz
						if (adrL[1] == ' ')
							adresse.hnr = null
						else
							adresse.hnr = adrL[1]
						adresse.str = adrL[3]
						adresse.ortsteil = findOt(punkt, adresse.ort)
						if (adresse.save()) {
							cntLoad++
						}
						else
							cntDup++
							
						if (adrL[4]) {
							//2. Adresse bilden
							cntAdr++
							def Adresse adresse2 = new Adresse(ort:adresse.ort,hnr:adrL[4],plz:adresse.plz,str:adresse.str)
							if (adresse2.save())
								cntLoad++
							else
								cntDup++
						}
					}
					if (cntAdrHerl %1000 == 00) {
						hibSession.flush()
						println "${cntRead} Sätze gelesen,${cntAdrHerl} hergeleitete Adressen geladen, Duplikate nicht geladen: ${cntDup}"
					}
				}
				
				lActive = false
				
			}
			
			adrL = tagL (it, adrL)

		}
		println "hergeleitete Adressen:${cntAdrHerl}"
		hibSession.flush()
	}
	
	String tagVal (String line, String x) {
		Integer anfKey = line.trim().indexOf("${x}=") + x.length() + 2
		Integer endKey = line.trim().indexOf('"', anfKey)
		line.trim().substring(anfKey, endKey)
	}
	
	List tagL (String s, List adrL) {
		List l = adrL
		List hwTypes = ['path','footway','residential','primary','secondary','tertiary','primary_link','secondary_link','tertiary_link','service']
		if (s.contains("<tag") && tagVal(s,'k') == "addr:city")
			adrL[0] = tagVal(s,'v')
		if (s.contains("<tag") && tagVal(s,'k') == "addr:housenumber") {
			adrL[1] = Adresse.hNrn(tagVal(s,'v'))[0]
			adrL[4] = Adresse.hNrn(tagVal(s,'v'))[1]
		}
		if (s.contains("<tag") && tagVal(s,'k') == "addr:postcode")
			adrL[2] = tagVal(s,'v').toInteger()
		if (s.contains("<tag") && tagVal(s,'k') == "addr:street")
			adrL[3] = tagVal(s,'v')
		if (s.contains("<tag") && tagVal(s,'k') == "highway" && tagVal(s,'v') in hwTypes)
			adrL[5] = tagVal(s,'v')
		if (s.contains("<tag") && tagVal(s,'k') == "name")
			adrL[6] = tagVal(s,'v')
		l
	}
	
	BufferedReader mapReader (String s) {
		FileInputStream osmStream = new FileInputStream (s);
		InputStreamReader osmReader = new InputStreamReader(osmStream, "UTF-8")
		BufferedReader osm = new BufferedReader (osmReader)
		osm
	}
	
	Ortsteil findOt(List punkt, String ort) {
		Ortsteil ot
		OtNode otNearest = OtNode.nearestOtNode(ortsteilList,punkt)
		String otName = otNearest.name
		String isIn = otNearest.isIn
		
		ot = Ortsteil.findByNameAndLiegtIn(otName,isIn)
//		if (ot && !(ort in ot.liegtIn.split(',')))
//			ot = Ortsteil.findByNameAndTypInList(ort,['city','town'])
		if (!ot)
			println "typ=${otNearest.place},otName=${otName},isIn=${isIn}"
		ot
	}
	
	List komplettOrtPlz (List adrL) {
		List komplett = adrL
		String ort = adrL[0]
		Integer plz = adrL[2]
		List plzn = []
		if (ort && !plz) {
			plzn = Postleitzahl.findAllByOrt(ort)
			if (plzn.size() == 1)
				komplett[2] = plzn[0].plz
		}
		if (!ort && plz) {
			plzn = Postleitzahl.findAllByPlz(plz)
			if (plzn.size() == 1)
				komplett[0] = plzn[0].ort
			else
				if (plzn.size() == 0)
					println "plz ${plz} nicht gefunden"
		}
		komplett
	}
	
	Integer getCntRead() {cntRead} 
	Integer getCntLoad() {cntLoad}
	Integer getCntAdr() {cntAdr}
}
