/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     ybonnel - initial API and implementation
 */
package fr.ybonnel.csvengine;

import fr.ybonnel.csvengine.adapter.*;
import fr.ybonnel.csvengine.annotation.CsvColumn;
import fr.ybonnel.csvengine.annotation.CsvFile;
import fr.ybonnel.csvengine.exception.CsvEngineException;
import fr.ybonnel.csvengine.exception.CsvErrorsExceededException;
import fr.ybonnel.csvengine.factory.AbstractCsvReader;
import fr.ybonnel.csvengine.factory.AbstractCsvWriter;
import fr.ybonnel.csvengine.factory.CsvManagerFactory;
import fr.ybonnel.csvengine.model.EngineParameters;
import fr.ybonnel.csvengine.model.InsertBatch;
import fr.ybonnel.csvengine.model.Result;
import fr.ybonnel.csvengine.validator.ValidationError;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for CsvEngine.
 * 
 * @author ybonnel
 * 
 */
public class CsvEngineTest {

	/**
	 * Object use for the tests.
	 * 
	 * @author ybonnel
	 * 
	 */
	@CsvFile(separator = "\\|")
	public static class CsvObject {

		/**
		 * Simple attribute.
		 */
		@CsvColumn(value = "att_1", order = 0)
		private String attribute1;

		/**
		 * Boolean attribute.
		 */
		@CsvColumn(value = "att_2", order = 1, adapter = AdapterBoolean.class)
		private Boolean attribute2;

		/**
		 * Double attribute.
		 */
		@CsvColumn(value = "att_3", order = 2, adapter = AdapterDouble.class)
		private Double attribute3;

		/**
		 * Integer attribute.
		 */
		@CsvColumn(value = "att_4", order = 5, adapter = AdapterInteger.class)
		private Integer attribute4;

		/**
         * Simple attribute with adapter.
		 */
		@CsvColumn(value = "att_5", order = 3, adapter = AdapterString.class)
		private String attribute5;

		/**
		 * Time attribute.
		 */
		@CsvColumn(value = "att_6", order = 6, adapter = AdapterTime.class)
		private Integer attribute6;

		/**
		 * Equals.
		 * 
		 * @param att1
		 *            att1.
		 * @param att2
		 *            att2.
		 * @param att3
		 *            att3.
		 * @param att4
		 *            att4.
		 * @param att5
		 *            att5.
		 * @param att6
		 *            att6.
		 * @return true if equals.
		 */
		protected boolean equals(String att1, Boolean att2, Double att3, Integer att4, String att5, Integer att6) {
			return ((att1 == null && attribute1 == null || att1 != null && attribute1 != null && att1.equals(attribute1))
					&& (att2 == null && attribute2 == null || att2 != null && attribute2 != null
							&& att2.equals(attribute2))
					&& (att3 == null && attribute3 == null || att3 != null && attribute3 != null
							&& att3.equals(attribute3))
					&& (att4 == null && attribute4 == null || att4 != null && attribute4 != null
							&& att4.equals(attribute4))
					&& (att5 == null && attribute5 == null || att5 != null && attribute5 != null
							&& att5.equals(attribute5)) && (att6 == null && attribute6 == null || att6 != null
					&& attribute6 != null && att6.equals(attribute6)));
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((attribute1 == null) ? 0 : attribute1.hashCode());
			result = prime * result + ((attribute2 == null) ? 0 : attribute2.hashCode());
			result = prime * result + ((attribute3 == null) ? 0 : attribute3.hashCode());
			result = prime * result + ((attribute4 == null) ? 0 : attribute4.hashCode());
			result = prime * result + ((attribute5 == null) ? 0 : attribute5.hashCode());
			result = prime * result + ((attribute6 == null) ? 0 : attribute6.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CsvObject other = (CsvObject) obj;
			if (attribute1 == null) {
				if (other.attribute1 != null)
					return false;
			} else if (!attribute1.equals(other.attribute1))
				return false;
			if (attribute2 == null) {
				if (other.attribute2 != null)
					return false;
			} else if (!attribute2.equals(other.attribute2))
				return false;
			if (attribute3 == null) {
				if (other.attribute3 != null)
					return false;
			} else if (!attribute3.equals(other.attribute3))
				return false;
			if (attribute4 == null) {
				if (other.attribute4 != null)
					return false;
			} else if (!attribute4.equals(other.attribute4))
				return false;
			if (attribute5 == null) {
				if (other.attribute5 != null)
					return false;
			} else if (!attribute5.equals(other.attribute5))
				return false;
			if (attribute6 == null) {
				if (other.attribute6 != null)
					return false;
			} else if (!attribute6.equals(other.attribute6))
				return false;
			return true;
		}

	}

	/**
	 * CsvEngine.
	 */
	private CsvEngine engine = null;

	/**
	 * Inputstream.
	 */
	private InputStream stream = null;

	/**
	 * Header.
	 */
	private static final String HEADER_654321 = "att_6|att_5|att_4|att_3|att_2|att_1";

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		engine = new CsvEngine(CsvObject.class, CsvObject.class);
		stream = new InputStream() {
			String string = HEADER_654321 + "\n01:30|String1|5|8.0|1|String2\n" + "|String1|5|8.0|1|String2\n"
					+ "\n01:30||5|8.0|1|String2\n" + "01:30|String1||8.0|1|String2\n" + "01:30|String1|5||1|String2\n"
					+ "01:30|String1|5|8.0||String2\n" + "01:30|String1|5|8.0|1|\n";
			private int count = 0;

			@Override
			public int read() throws IOException {
				if (count >= string.length()) {
					return -1;
				}
				return string.charAt(count++);
			}
		};
	}

	@Test
	public void testParseInputStream() throws IOException, CsvErrorsExceededException {

		List<CsvObject> objects = engine.parseInputStream(stream, CsvObject.class).getObjects();
		assertEquals(7, objects.size());
		assertTrue(objects.get(0).equals("String2", true, 8.0, 5, "String1", 90));
		assertTrue(objects.get(1).equals("String2", true, 8.0, 5, "String1", null));
		assertTrue(objects.get(2).equals("String2", true, 8.0, 5, null, 90));
		assertTrue(objects.get(3).equals("String2", true, 8.0, null, "String1", 90));
		assertTrue(objects.get(4).equals("String2", true, null, 5, "String1", 90));
		assertTrue(objects.get(5).equals("String2", null, 8.0, 5, "String1", 90));
		assertTrue(objects.get(6).equals(null, true, 8.0, 5, "String1", 90));

		File file = File.createTempFile("csv_object", "txt");

		engine.writeFile(new FileWriter(file), objects, CsvObject.class);

		List<CsvObject> newObjects = engine.parseInputStream(new FileInputStream(file), CsvObject.class).getObjects();
		assertEquals(objects, newObjects);

	}

	@Test
	public void testOtherFactory() throws CsvErrorsExceededException {

		engine.setFactory(new CsvManagerFactory() {

			public AbstractCsvWriter createWriterCsv(final Writer writer, char separator, boolean addQuoteCar) {
				return new AbstractCsvWriter() {

					public void close() throws IOException {
						writer.close();
					}

					@Override
					public void writeLine(List<String> fields) {
						try {
							writer.append("hello\n");
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				};
			}

			public AbstractCsvReader createReaderCsv(final Reader reader, char separator) {
				return new AbstractCsvReader() {

					boolean first = true;

					public void close() throws IOException {
						reader.close();
					}

					public String[] readLine() throws IOException {
						if (first) {
							first = false;
							return HEADER_654321.split("\\|");
						}
						return null;
					}
				};
			}
		});

		assertTrue(engine.parseInputStream(stream, CsvObject.class).getObjects().isEmpty());

	}

	/**
	 * Technical tests
	 * 
	 * @throws fr.ybonnel.csvengine.validator.ValidationError
	 */
	@Test
	public void testCreateObject() throws ValidationError {
		try {
			engine.createObject();
            fail("An exception must be throw");
		} catch (CsvEngineException exception) {
			assertTrue(exception.getMessage().contains("newCsvFile"));
		}

		engine.setFactory(new CsvManagerFactory() {
			public AbstractCsvWriter createWriterCsv(Writer writer, char separator, boolean addQuoteCar) {
				return null;
			}

			public AbstractCsvReader createReaderCsv(Reader reader, char separator) {
				return new AbstractCsvReader() {
					public void close() throws IOException {
					}

					private boolean first = true;

					public String[] readLine() throws IOException {
						if (first) {
							first = false;
							return HEADER_654321.split("\\|");
						}
						throw new IOException();
					}
				};
			}
		});

		engine.newCsvFile(new InputStreamReader(stream), CsvObject.class);

		try {
			engine.createObject();
            fail("An exception must be throw");
		} catch (CsvEngineException exception) {
			assertEquals(IOException.class, exception.getCause().getClass());
		}
	}

	@Test
	public void testNewCsvFile() {
		try {
			engine.newCsvFile(null, String.class);
            fail("An exception must be throw");
		} catch (CsvEngineException exception) {
			assertTrue(exception.getMessage().contains("String"));
		}

		engine.setFactory(new CsvManagerFactory() {
			public AbstractCsvWriter createWriterCsv(Writer writer, char separator, boolean addQuoteCar) {
				return null;
			}

			public AbstractCsvReader createReaderCsv(Reader reader, char separator) {
				return new AbstractCsvReader() {
					public void close() throws IOException {
					}

					public String[] readLine() throws IOException {
						throw new IOException();
					}
				};
			}
		});

		try {
			engine.newCsvFile(null, CsvObject.class);
            fail("An exception must be throw");
		} catch (CsvEngineException exception) {
			assertEquals(IOException.class, exception.getCause().getClass());
		}
	}

	@Test
	public void testScanClass() {
		try {
			engine.scanClass(String.class);
            fail("An exception must be throw");
		} catch (CsvEngineException exception) {
			assertTrue(exception.getMessage().contains("String"));
		}
	}

	@Test
	public void testWriteFile() {
		engine.setFactory(new CsvManagerFactory() {

			public AbstractCsvWriter createWriterCsv(Writer writer, char separator, boolean addQuoteCar) {
				return new AbstractCsvWriter() {
					public void close() throws IOException {
					}

					public void writeLine(List<String> fields) {
						throw new NullPointerException();
					}
				};
			}

			public AbstractCsvReader createReaderCsv(Reader reader, char separator) {
				return null;
			}
		});

		try {
			engine.writeFile(null, new ArrayList<CsvObject>(), CsvObject.class);
            fail("An exception must be throw");
		} catch (CsvEngineException exception) {
			assertEquals(NullPointerException.class, exception.getCause().getClass());
		}
	}

	@CsvFile
	public static class ObjectWithExceptionInConstructor {

		public ObjectWithExceptionInConstructor() {
			throw new NullPointerException();
		}

		@CsvColumn("att")
		public String att;
	}

	@Test
	public void testExceptionInConstructor() throws CsvErrorsExceededException {
		CsvEngine engine = new CsvEngine(ObjectWithExceptionInConstructor.class);
		StringStream stream = new StringStream("att\nvalue");
		try {
			engine.parseInputStream(stream, ObjectWithExceptionInConstructor.class);
            fail("An exception must be throw");
		} catch (CsvEngineException exception) {
			assertEquals(NullPointerException.class, exception.getCause().getClass());
		}
	}

	@CsvFile(separator = ",,,")
	public static class ObjectWithTooBigSeparator {

	}

	@Test
	public void testTooBigSeparator() throws CsvErrorsExceededException {
		CsvEngine engine = new CsvEngine(ObjectWithTooBigSeparator.class);
		StringStream stream = new StringStream("att\nvalue");
		try {
			engine.parseInputStream(stream, ObjectWithTooBigSeparator.class);
            fail("An exception must be throw");
		} catch (CsvEngineException exception) {
			assertTrue(exception.getMessage().contains(",,,"));
		}
	}

	@CsvFile
	public static class ObjectWithoutConstructor {
		public ObjectWithoutConstructor(String something) {
		}
	}

	@Test
	public void testWithoutConstructor() {
		try {
			new CsvEngine(ObjectWithoutConstructor.class);
            fail("An exception must be throw");
		} catch (CsvEngineException exception) {
			assertTrue(exception.getMessage().contains("this class miss a constructor without parameters"));
		}
	}

	@CsvFile
	public static class SimpleObject {
		@CsvColumn("att")
		public String att;
	}

	@Test
	public void testAddQuoteCar() {
		SimpleObject object = new SimpleObject();
		object.att = "value";

		CsvEngine engine =
				new CsvEngine(EngineParameters.createBuilder().setAddQuoteCar(true).build(), SimpleObject.class);
		StringWriter writer = new StringWriter();

		engine.writeFile(writer, Arrays.asList(object), SimpleObject.class);

        assertEquals("\"att\"\n\"value\"\n", writer.getBuffer().toString());

		engine = new CsvEngine(EngineParameters.createBuilder().setAddQuoteCar(false).build(), SimpleObject.class);
		writer = new StringWriter();

		engine.writeFile(writer, Arrays.asList(object), SimpleObject.class);

		assertEquals("att\nvalue\n", writer.getBuffer().toString());

	}

    public static class Counter {
        int count = 0;
        public void increament() {
            count++;
        }
        public void reset() {
            count = 0;
        }
        public int get() {
            return count;
        }
    }

    @Test
    public void testParseFileAndBatch() throws CsvErrorsExceededException {
        CsvEngine engine = new CsvEngine(SimpleObject.class);
        String csvContent = "att\nline1\nline2\nline3\nline4\n";

        // First test, batchSize=1
        final List<SimpleObject> resultObjects = new ArrayList<SimpleObject>();
        final Counter count = new Counter();
        engine.parseFileAndHandleBatch(new InputStreamReader(new StringStream(csvContent)), SimpleObject.class, new InsertBatch<SimpleObject>() {
            public void handleBatch(List<SimpleObject> objects) {
                assertEquals(1, objects.size());
                resultObjects.addAll(objects);
                count.increament();
            }
        }, 1);
        assertEquals(4, count.get());
        assertEquals("line1", resultObjects.get(0).att);
        assertEquals("line2", resultObjects.get(1).att);
        assertEquals("line3", resultObjects.get(2).att);
        assertEquals("line4", resultObjects.get(3).att);

        // batchSize = 2
        resultObjects.clear();
        count.reset();
        engine.parseFileAndHandleBatch(new InputStreamReader(new StringStream(csvContent)), SimpleObject.class, new InsertBatch<SimpleObject>() {
            public void handleBatch(List<SimpleObject> objects) {
                assertEquals(2, objects.size());
                resultObjects.addAll(objects);
                count.increament();
            }
        }, 2);
        assertEquals(2, count.get());
        assertEquals("line1", resultObjects.get(0).att);
        assertEquals("line2", resultObjects.get(1).att);
        assertEquals("line3", resultObjects.get(2).att);
        assertEquals("line4", resultObjects.get(3).att);

        // batchSize = 3
        resultObjects.clear();
        count.reset();
        engine.parseFileAndHandleBatch(new InputStreamReader(new StringStream(csvContent)), SimpleObject.class, new InsertBatch<SimpleObject>() {
            public void handleBatch(List<SimpleObject> objects) {
                if (count.get() == 0) {
                    assertEquals(3, objects.size());
                } else {
                    assertEquals(1, objects.size());
                }
                resultObjects.addAll(objects);
                count.increament();
            }
        }, 3);
        assertEquals(2, count.get());
        assertEquals("line1", resultObjects.get(0).att);
        assertEquals("line2", resultObjects.get(1).att);
        assertEquals("line3", resultObjects.get(2).att);
        assertEquals("line4", resultObjects.get(3).att);
    }

    @Test
    public void testParseFirstLinesOfInputStream() throws CsvErrorsExceededException {
        CsvEngine engine = new CsvEngine(SimpleObject.class);
        String csvContent = "att\nline1\nline2\nline3\nline4\n";

        // Stop before end.
        Result<SimpleObject> result = engine.parseFirstLinesOfInputStream(new StringStream(csvContent), SimpleObject.class, 2);
        assertEquals(2, result.getObjects().size());
        assertEquals("line1", result.getObjects().get(0).att);
        assertEquals("line2", result.getObjects().get(1).att);

        // nbLinesToParse > nbLines of file.
        result = engine.parseFirstLinesOfInputStream(new StringStream(csvContent), SimpleObject.class, 10);
        assertEquals(4, result.getObjects().size());
        assertEquals("line1", result.getObjects().get(0).att);
        assertEquals("line2", result.getObjects().get(1).att);
        assertEquals("line3", result.getObjects().get(2).att);
        assertEquals("line4", result.getObjects().get(3).att);
    }

    @Test
    public void testGetColumnNames() {

        CsvEngine engine = new CsvEngine(CsvObject.class);
        List<String> columnNames = engine.getColumnNames(CsvObject.class);
        assertEquals(6, columnNames.size());
        assertEquals("att_1", columnNames.get(0));
        assertEquals("att_2", columnNames.get(1));
        assertEquals("att_3", columnNames.get(2));
        assertEquals("att_5", columnNames.get(3));
        assertEquals("att_4", columnNames.get(4));
        assertEquals("att_6", columnNames.get(5));
    }

    @Test
    public void testUtf16() throws CsvErrorsExceededException {
        CsvEngine engine = new CsvEngine(CsvObject.class);

        List<CsvObject> results = engine.parseInputStream(CsvEngine.class.getResourceAsStream("/testutf16.csv"), Charset.forName("UTF-16"), CsvObject.class).getObjects();

        assertEquals(2, results.size());
        assertEquals("é", results.get(0).attribute1);
        assertTrue(results.get(0).attribute2);
        assertEquals("à", results.get(1).attribute1);
        assertFalse(results.get(1).attribute2);
    }
}
