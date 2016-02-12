package org.strotmann.plz

class Ortsteil {
	
	String typ
	String name
	String liegtIn

    static constraints = {
		typ(inList:['town','city','suburb','hamlet'])
		name(unique:['liegtIn'])
		liegtIn()
    }
	
	static mapping = {
		name column: "name", index: "name"
	}
}
