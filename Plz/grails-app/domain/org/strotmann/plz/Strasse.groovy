package org.strotmann.plz

class Strasse {
	
	String strasse
	Integer postleitzahl
	String hausNrVon
	String hausNrBis
	Ortsteil ortsteil
	String ort

    static constraints = {
		strasse(unique:['postleitzahl','strasse','hausNrVon'])
		postleitzahl()
		hausNrVon(nullable:true)
		hausNrBis(nullable:true)
		ortsteil(nullable:true)
		ort(nullable:true)
	}
	
	static mapping = {
		strasse column: "strasse", index: "strasse"
		postleitzahl column: "postleitzahl", index: "postleitzahl"
	}
	
	String toString() {"${postleitzahl} ${ortsteil} ${strasse} ${hausNrVon} ${hausNrBis}" }

	String getOrtsteilKlar() {
		String n
		n = ortsteil.name
		if (n == Postleitzahl.findByPlz(postleitzahl).ort)
			n = null
		n
	}
	
	String getOrt() {
		String o
		if (ort)
			o = ort
		else
			o = Postleitzahl.findByPlz(postleitzahl).ort
		o
	}
}
