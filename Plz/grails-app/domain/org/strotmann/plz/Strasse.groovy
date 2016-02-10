package org.strotmann.plz

class Strasse {
	
	String strasse
	Integer postleitzahl
	String hausNrVon
	String hausNrBis
	Ortsteil ortsteil

    static constraints = {
		strasse(unique:['postleitzahl','strasse','hausNrVon'])
		postleitzahl()
		hausNrVon(nullable:true)
		hausNrBis(nullable:true)
		ortsteil(nullable:true)
	}
	
	static mapping = {
		strasse column: "strasse", index: "strasse"
		postleitzahl column: "postleitzahl", index: "postleitzahl"
	}
	
	String toString() {"${postleitzahl} ${strasse} ${hausNrVon} ${hausNrBis}" }
}
