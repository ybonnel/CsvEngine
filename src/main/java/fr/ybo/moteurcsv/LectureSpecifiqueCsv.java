package fr.ybo.moteurcsv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class LectureSpecifiqueCsv extends AbstractLectureCsv {
	
	private BufferedReader reader;
	private char separateur;
	
	

	public LectureSpecifiqueCsv(Reader reader, char separateur) {
		super();
		this.reader = new BufferedReader(reader);
		this.separateur = separateur;
	}
	
	public static String[] splitLine(String ligne, char separateur) {
		if (ligne == null) {
			return null;
		}
		List<String> champs = new ArrayList<String>();
		int indiceDebutChaine = -1;
		boolean enCours = false;
		boolean guillemetEnCours = false;
		boolean guillemetTermine = false;
		for (int indice = 0; indice < ligne.length(); indice++) {
			if (!enCours) {
				if (ligne.charAt(indice) == separateur) {
					champs.add("");
					guillemetEnCours = false;
					guillemetTermine = false;
					indiceDebutChaine = -1;
				} else if (ligne.charAt(indice) == '"') {
					guillemetEnCours = true;
				} else {
					enCours = true;
					indiceDebutChaine = indice;
				}
			} else {
				if (guillemetEnCours) {
					if ('"' == ligne.charAt(indice)) {
						guillemetEnCours = false;
						guillemetTermine = true;
					}
				} else if (ligne.charAt(indice) == separateur) {
					if (indiceDebutChaine == -1) {
						champs.add("");
					} else {
						int indiceFin = indice;
						if (guillemetTermine) {
							indiceFin = indice - 1;
						}
						champs.add(ligne.substring(indiceDebutChaine, indiceFin));
						guillemetEnCours = false;
						guillemetTermine = false;
						enCours = false;
						indiceDebutChaine = -1;
					}
				}
			}
		}
		if (enCours) {
			if (indiceDebutChaine == -1) {
				champs.add("");
			} else {
				int indiceFin = ligne.length();
				if (guillemetTermine) {
					indiceFin = ligne.length() - 1;
				}
				champs.add(ligne.substring(indiceDebutChaine, indiceFin));
			}
		}
		return champs.toArray(new String[champs.size()]);
	}

	@Override
	public String[] readLine() throws IOException {
		return splitLine(reader.readLine(), separateur);
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}
	
	

}
