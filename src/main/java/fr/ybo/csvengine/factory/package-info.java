/**
 * Package containing all the object associate to the factory for write and read CSV File.<br/>
 * By default CsvEngine use open-csv, you can modify it :<br/>
 * You must implement the class {@linkplain CsvManagerFactory} and pass
 * it to CsvEngine with method {@linkplain fr.ybo.csvengine.CsvEngine#setFactory(CsvManagerFactory)}.<br/><br/>
 *
 * <i><u>French :</i> Package contenant l'ensemble des objets associés au mécanisme de factory
 * pour l'écriture et la lecture de fichier CSV.<br/>
 *
 * Par défaut le moteur utilise open-csv, il est touteois possible de modifier ce comportement.<br/>
 * Il suffit d'implémenter la classe {@linkplain CsvManagerFactory}
 * et de la fournit au moteur via la méthode
 * {@linkplain fr.ybo.csvengine.CsvEngine#setFactory(CsvManagerFactory)}.<br/>
 */
package fr.ybo.csvengine.factory;