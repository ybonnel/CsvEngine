/**
 * Ce package contient les annotations à utiliser pour mapper une classe avec un fichier CSV.<br/>
 * Déclaration d'une classe :<br/>
 * <pre>
 * {@code @FichierCsv(separateur = ";")}
 * public static class ObjetCsv {
 * 	{@code @BaliseCsv(value = "att_1", ordre = 0)}
 * 	private String attribut1;
 * 	{@code @BaliseCsv(value = "att_2", ordre = 1, adapter = AdapterBoolean.class)}
 * 	private Boolean attribut2;
 * 	{@code @BaliseCsv(value = "att_3", ordre = 2, adapter = AdapterDouble.class)}
 * 	private Double attribut3;
 * 	{@code @BaliseCsv(value = "att_4", ordre = 5, adapter = AdapterInteger.class)}
 * 	private Integer attribut4;
 * 	{@code @BaliseCsv(value = "att_5", ordre = 3, adapter = AdapterString.class)}
 * 	private String attribut5;
 * 	{@code @BaliseCsv(value = "att_6", ordre = 6, adapter = AdapterTime.class)}
 * 	private Integer attribut6;
 * }
</pre>
 * La classe ObjetCsv correspondra au CSV suivant :<br/>
 * <pre>
 * att_1;att_2;att_3;att_4;att_5;att_6
 * valeur1;0;0.25;3;valeur5;12:25
 * </pre>
 */
package fr.ybo.moteurcsv.annotation;

