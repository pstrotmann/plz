package org.strotmann.plz
import org.hibernate.*

class UmstellungController {
	SessionFactory sessionFactory

    def index() {
		assert sessionFactory != null
		render ("Strassen Tabelle wird umgestellt")
		Integer cntRead = 0
		Integer cntUpd = 0
		Strasse.findAll ("from Strasse").each {Strasse s ->
			cntRead++
			
			s.postleitzahl = s.plz.plz
			
			String zusVon
			if (s.zusVon) 
				zusVon = s.zusVon
			else
				zusVon = '' 
			if (s.hnrVon)
				s.hausNrVon = s.hnrVon.toString().padLeft(4,'0') + zusVon
				
			String zusBis
			if (s.zusBis)
				zusBis = s.zusBis
			else
				zusBis = ''
			if (s.hnrBis)
				s.hausNrBis = s.hnrBis.toString().padLeft(4,'0') + zusBis
			
			if (cntRead %1000 == 0)
				println "s=${s},cntUpd=${cntUpd}"
			
			if (s.save())
				cntUpd++
			else
				s.errors.each {
					println it
				}
		}
		def hibSession = sessionFactory.getCurrentSession()
		assert hibSession != null
		hibSession.flush()
		println "gelesen:${cntRead}, gespeichert:${cntUpd}"
		render ("Strassen Tabelle ist umgestellt")
	}
}
