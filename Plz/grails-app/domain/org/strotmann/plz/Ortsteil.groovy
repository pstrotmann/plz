package org.strotmann.plz

class Ortsteil {
	
	String name
	String liegtIn

    static constraints = {
		name(unique:['liegtIn'])
		liegtIn()
    }
}
