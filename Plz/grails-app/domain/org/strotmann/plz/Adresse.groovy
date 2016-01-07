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
		str()
    }
	
	String toString() {"${ort},${plz},${str},${hnr}" }
}
