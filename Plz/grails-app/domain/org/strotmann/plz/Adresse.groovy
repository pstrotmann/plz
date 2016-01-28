package org.strotmann.plz

class Adresse {

	String ort
	String hnr
	Integer plz
	String str
	
    static constraints = {
		ort()
		hnr(nullable:true)
		plz()
		str(unique:['plz','str','hnr'])
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
}
