package fr.ybo.moteurcsv.modele;

import java.util.List;

public class InsertInList<Objet> implements InsertObject<Objet> {

	private List<Objet> objets;

	public InsertInList(List<Objet> objets) {
		this.objets = objets;
	}

	@Override
	public void insertObject(Objet objet) {
		objets.add(objet);
	}
}