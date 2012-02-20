/**
 * Package contenant l'ensemble des objets associés au mécanisme de factory pour l'écriture et la lecture de fichier CSV.
 * 
 * Par défaut le moteur utilise open-csv, il est touteois possible de modifier ce comportement.<br>
 * Il suffit d'implémenter la classe {@linkplain fr.ybo.moteurcsv.factory.GestionnaireCsvFactory}
 * et de la fournit au moteur via la méthode {@linkplain fr.ybo.moteurcsv.MoteurCsv#setFactory(GestionnaireCsvFactory)}.
 */
package fr.ybo.moteurcsv.factory;