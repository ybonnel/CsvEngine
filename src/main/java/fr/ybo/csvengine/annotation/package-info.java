/**
 * This package contain all annotations used to map a class with a CSV File.<br/>
 * Declaration of a class :<br/>
 * <pre>
 * {@code @CsvFile(separator = ";")}
 * public static class Exemple {
 * 	{@code @CsvColumn(value = "att_1", order = 0)}
 * 	private String attribut1;
 * 	{@code @CsvColumn(value = "att_2", order = 1, adapter = AdapterBoolean.class)}
 * 	private Boolean attribut2;
 * 	{@code @CsvColumn(value = "att_3", order = 2, adapter = AdapterDouble.class)}
 * 	private Double attribut3;
 * 	{@code @CsvColumn(value = "att_4", order = 5, adapter = AdapterInteger.class)}
 * 	private Integer attribut4;
 * 	{@code @CsvColumn(value = "att_5", order = 3, adapter = AdapterString.class)}
 * 	private String attribut5;
 * 	{@code @CsvColumn(value = "att_6", order = 6, adapter = AdapterTime.class)}
 * 	private Integer attribut6;
 * }
 </pre>
 * The Exemple class will correspond to the CSV File :<br/>
 * <pre>
 * att_1;att_2;att_3;att_4;att_5;att_6
 * valeur1;0;0.25;3;valeur5;12:25
 * </pre>
 * <br/><br/>
 *
 * <u><i>French :</i></u>
 * Ce package contient les annotations à utiliser pour mapper une classe avec un fichier CSV.<br/>
 * Déclaration d'une classe :<br/>
 * <pre>
 * {@code @CsvFile(separator = ";")}
 * public static class Exemple {
 * 	{@code @CsvColumn(value = "att_1", order = 0)}
 * 	private String attribut1;
 * 	{@code @CsvColumn(value = "att_2", order = 1, adapter = AdapterBoolean.class)}
 * 	private Boolean attribut2;
 * 	{@code @CsvColumn(value = "att_3", order = 2, adapter = AdapterDouble.class)}
 * 	private Double attribut3;
 * 	{@code @CsvColumn(value = "att_4", order = 5, adapter = AdapterInteger.class)}
 * 	private Integer attribut4;
 * 	{@code @CsvColumn(value = "att_5", order = 3, adapter = AdapterString.class)}
 * 	private String attribut5;
 * 	{@code @CsvColumn(value = "att_6", order = 6, adapter = AdapterTime.class)}
 * 	private Integer attribut6;
 * }
</pre>
 * La classe Exemple correspondra au CSV suivant :<br/>
 * <pre>
 * att_1;att_2;att_3;att_4;att_5;att_6
 * valeur1;0;0.25;3;valeur5;12:25
 * </pre>
 */
package fr.ybo.csvengine.annotation;

