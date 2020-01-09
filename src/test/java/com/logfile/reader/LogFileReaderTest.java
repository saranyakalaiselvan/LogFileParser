package com.logfile.reader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import com.logfile.reader.LogFileReader;

public class LogFileReaderTest {

	File file = new File("programming-task-example-data.log");

	// HashMap to store IP
	HashMap<String, Integer> ipMap = new HashMap<String, Integer>();
	// HashMap to store url
	HashMap<String, Integer> urlMap = new HashMap<String, Integer>();

	@Test
	public void checkFileAvailability() throws IOException {
		File file = new File("programming-task-example-data.log");
		assertTrue(file.exists());
	}

	@Test
	public void manipulateLogsTest() throws IOException {
		String record = FileUtils.readFileToString(file);
		String regex = "^(\\S+) (\\S+) (\\S+) " + "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)"
				+ " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(record);

		assertTrue(matcher.find());
		while (matcher.find()) {
			assertNotNull(matcher.group(1));
			assertNotNull(matcher.group(6));
			LogFileReader.populateMap(ipMap, matcher.group(1));
			LogFileReader.populateMap(urlMap, matcher.group(6));
			assertNotNull(ipMap);
			assertNotNull(urlMap);
		}
		assertNotNull(LogFileReader.identifyUniqueAddress(ipMap));
		assertNotNull(LogFileReader.calculateTop3(ipMap));
		assertNotNull(LogFileReader.calculateTop3(urlMap));

	}

}
