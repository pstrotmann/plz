package org.strotmann.plz

import java.util.List;

import grails.util.Holders

class Postleitzahl {
	
	Integer plz
	Integer osmId
	String ort
	Integer bundesland
	String gkMerk
	String grosskunde
	
    static constraints = {
		plz()
		osmId(nullable:true)
		ort()
		bundesland(inList:bundeslandIds)
		gkMerk(nullable:true, inList:['-','+','*'])
		grosskunde(nullable:true)
    }
	
	String toString() {"${this.plz}  ${this.ort}" }
	
	static List getBundeslandIds () {
		List l = []
		Holders.config.bundesland.each {entry ->
			l << entry.key
		}
		l
	}
	
	static Integer bundeslandId (String bundesland) {
		def found = Holders.config.bundesland.find {entry -> entry.value == bundesland }
		found.key
	}
	
	static getSortiertNachPlz() {
		Postleitzahl.list().sort{it.plz}
	}
	
	String getBundeslandKlar () {
		Holders.config.bundesland[bundesland]
	}
}
