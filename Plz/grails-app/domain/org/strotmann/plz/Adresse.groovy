package org.strotmann.plz

class Adresse {

	String ort
	String hnr
	Integer plz
	String str
	Ortsteil ortsteil
	
    static constraints = {
		ort()
		hnr(nullable:true)
		plz()
		str(unique:['plz','str','hnr'])
		ortsteil(nullable:true)
    }
	
	String toString() {"${ort},${plz},${str},${hnr}" }
	
	static List getStrassen () {
		def c = Adresse.createCriteria()
		
		def results = c.list {
			projections {
				sqlGroupProjection 'ort, plz, str, min(hnr) as hnrVon, max(hnr) as hnrBis', 'ort, plz, str', ['ort','plz','str','hnrVon','hnrBis'], [STRING,STRING,STRING,STRING,STRING]
			}
		}
		
		results
	}
	
	static List hNrn(String hnr)  {
		
		List <String> hnrTeile = [] 
		List <String> l = []
		
		if (hnr.contains(",")) 
			l = hnr.split(",") 
		else
		if (hnr.contains(";"))
			l = hnr.split(";")
		else
		if (hnr.contains("/"))
			l = hnr.split("/")
		else
		if (hnr.contains("-"))
			l = hnr.split("-")
		else
			l << hnr
			
		hnrTeile << l[0].trim()
		hnrTeile[0] = hnrPad(hnrTeile[0])
		if (l.size() > 1) {
			hnrTeile << l[l.size()-1].trim()
			hnrTeile[1] = hnrPad(hnrTeile[1])
		}
			
		hnrTeile
		
	}
	
	static String hnrPad (String hnr) {
		if (!hnrChk(hnr))
			return null
		def String n = ''
		def String a = ''
		for (Integer i = 0; i < hnr.length(); i++) {
			String s = hnr.substring(i, i+1)
			if (s.isNumber())
				n += s
			else
				a += s
		}
		n = n.padLeft(4,'0') + a
		
	}
	
	static Boolean hnrChk (String hnr) {
		Boolean ok = true
		Boolean nonNum = false
		for (Integer i = 0; i < hnr.length(); i++) {
			String s = hnr.substring(i, i+1)
			if (s.isNumber() && nonNum) 
				ok = false
			if (!s.isNumber())
				nonNum = true
		}
		ok
	}
}
