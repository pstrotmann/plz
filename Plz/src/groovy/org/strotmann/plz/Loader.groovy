package org.strotmann.plz

import java.util.List;

import org.hibernate.Session
import org.hibernate.SessionFactory

class Loader {
	
	def Session hibSession
	
	List speichernStrassen () {
		def List cnt = [0,0]
		def List strList = strassen
		strList.each {
			cnt[0]++
			Strasse strasse = new Strasse(ort:it[0],postleitzahl:it[1].toInteger(),strasse:it[2],hausNrVon:it[3],hausNrBis:it[4])
			if (strasse.save())
				cnt[1]++
				else
					strasse.errors.each {
					println it
				}
		}
			
		hibSession.flush()
		cnt
		
	}
	
	List getStrassen () {
		def c = Adresse.createCriteria()
		
		def results = c.list {

			//'in' ('plz',[45739])
			'in' ('ort',['Dortmund'])
			projections {
				sqlGroupProjection 'ort, plz, str, min(hnr) as hnrVon, max(hnr) as hnrBis', 'ort, plz, str', ['ort','plz','str','hnrVon','hnrBis'], [STRING,STRING,STRING,STRING,STRING]
			}
		}
		
		results
	}

}
