package org.strotmann.plz

import java.util.List;

import grails.util.Holders

class Postleitzahl {
	
	Integer plz
	Integer osmId
	String ort
	Integer bundesland
	
    static constraints = {
		plz()
		osmId(nullable:true)
		ort()
		bundesland(inList:bundeslandIds)
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
	
	String getBundeslandKlar () {
		Holders.config.bundesland[bundesland]
	}
}
