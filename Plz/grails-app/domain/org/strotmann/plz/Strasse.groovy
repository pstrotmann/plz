package org.strotmann.plz

class Strasse {
	
	String strasse
	Integer hnrVon
	String zusVon
	Integer hnrBis
	String zusBis
	Postleitzahl plz

    static constraints = {
		strasse(unique:['plz','strasse','hnrVon','zusVon'])
		hnrVon(nullable:true)
		zusVon(nullable:true)
		hnrBis(nullable:true)
		zusBis(nullable:true)
		plz()
	}
}
