package com.logfile.reader;

import static java.util.stream.Collectors.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

public class LogFileReader {

	public static void main(String[] args) throws IOException {

		File file = new File("programming-task-example-data.log");
		String fileContent = FileUtils.readFileToString(file);
		manipulateLogs(fileContent);

	}

	public static void manipulateLogs(String fileContent) {

		// Regular expression for the records
		final String regex = "^(\\S+) (\\S+) (\\S+) " + "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)"
				+ " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";

		final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(fileContent);

		// HashMap to store IP
		HashMap<String, Integer> ipMap = new HashMap<String, Integer>();

		// HashMap to store uniqueIP
		HashMap<String, Integer> uniqueIpMap = new HashMap<String, Integer>();

		// HashMap to store url
		HashMap<String, Integer> urlMap = new HashMap<String, Integer>();

		while (matcher.find()) {
			String IP = matcher.group(1);
			String url = matcher.group(6);
			// Inserting the IP addresses in the HashMap and maintaining the number of
			// occurrences
			populateMap(ipMap, IP);
			// Inserting the urls in the HashMap and maintaining the number of occurrences
			populateMap(urlMap, url);
		}
		System.out.println("Unique IP addresses:");
		uniqueIpMap = (HashMap<String, Integer>) identifyUniqueAddress(ipMap);
		printValues(uniqueIpMap);
		System.out.println("The most active IP addresses:");
		ipMap = calculateTop3(ipMap);
		printValues(ipMap);
		System.out.println("The top 3 most visited urls:");
		urlMap = calculateTop3(urlMap);
		printValues(urlMap);

	}

	public static Map<String, Integer> identifyUniqueAddress(HashMap<String, Integer> ipMap) {
		return ipMap.entrySet().stream().filter(e -> e.getValue() == 1)
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}

	public static void populateMap(HashMap<String, Integer> ipMap, String IP) {
		if (ipMap.containsKey(IP)) {
			ipMap.put(IP, ipMap.get(IP) + 1);
		} else {
			ipMap.put(IP, 1);
		}
	}

	public static void printValues(HashMap<String, Integer> valueMap) {
		valueMap.keySet().stream().forEach(System.out::println);
	}

	public static LinkedHashMap<String, Integer> calculateTop3(HashMap<String, Integer> inputMap) {
		return inputMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(3)
				.collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
	}

}
