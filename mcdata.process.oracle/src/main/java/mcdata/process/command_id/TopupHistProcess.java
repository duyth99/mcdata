package mcdata.process.command_id;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import mcdata.process.db.Database;
import mcdata.process.model.Data;
import mcdata.process.utils.Props;

public class TopupHistProcess {
	public void startApp(String args[], Connection conn) throws Exception {
		
		long begin = System.currentTimeMillis();

		String input = args[1];
		String indexMsisdn = args[2];
		String service_provider = args[3];
		String indexDateTime = args[4].split(",", -1)[0];
		String patternDateTimeIn = args[4].split(",", -1)[1];
		String patternDateTimeOut = args[4].split(",", -1)[2];
		String separator = args[7];


		HashMap<String, String> mapIndex = new HashMap<>();
//		mapIndex.put("msisdn", indexMsisdn);
//		mapIndex.put("datetime", indexDateTime);

		for (int i = 5; i < 7; i++) {
			mapIndex.put(args[i].split("=")[0], args[i].split("=")[1]);
		}

		DateTimeFormatter formaterIn = DateTimeFormatter.ofPattern(patternDateTimeIn);
		DateTimeFormatter formaterOut = DateTimeFormatter.ofPattern(patternDateTimeOut);
//		DateTimeFormatter formaterOutDateTime = DateTimeFormatter.ofPattern(Constants.FORMAT_DATETIME);
//		DateTimeFormatter formaterOutDate = DateTimeFormatter.ofPattern(Constants.FORMAT_DATE);

		List<String> list = new ArrayList<>();
		if (Files.isDirectory(Paths.get(input))) {
			list = Files.walk(Paths.get(input), 1).filter(Files::isRegularFile).map(x -> x.toString())
					.collect(Collectors.toList());
		} else {
			list.add(input);
		}

		Pattern patternMsisdn = Pattern.compile(Props.getProp("msisdn_pattern"));
		Pattern patternP = Pattern.compile("(\\w)#(\\w)#(\\w+)(,(.+),(.+))?");
		Pattern patternS = Pattern.compile("(\\w)#(\\w*)");
		Pattern patternDate = Pattern.compile("(?<yyyy>\\d{4})(?<MM>\\d{2})(?<dd>\\d{2})(?<HHmmss>\\d{6})");

		
		int batch = Integer.parseInt(Props.getProp("batch"));
		long count = 0;
		long skip = 0;
		long msInsert = 0;
		for (String inputFile : list) {
			long now = System.currentTimeMillis();
			System.out.println("read: " + inputFile);

			BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile), StandardCharsets.UTF_8);
			
			String firstLine = reader.readLine();
			int countColumn = firstLine.replaceAll("\"", "").split(separator, -1).length;
			if (countColumn < 2) {
				System.out.println("-------------------------can't read-------------------------");
				continue;
			}

			String output = "";
			List<Data> listInsert = new ArrayList<>();
			String[] line;
			String thisLine = null;
			while ((thisLine = reader.readLine()) != null) {
				try {
					Matcher matcher;

					line = thisLine.replaceAll("\"", "").split(separator, -1);
					if (line.length < countColumn) {
						skip++;
//						System.out.println("skip: " + count);
						Files.createDirectories(Paths.get("log_error", args[0],service_provider));
						FileUtils.writeStringToFile(
								new File(Paths.get("log_error", args[0],service_provider,FilenameUtils.removeExtension(new File(inputFile).getName())+".skip").toString()),
								thisLine+"\n",StandardCharsets.UTF_8, true);
						continue;
					}

					JSONObject mapOutput = new JSONObject();

					String msisdn = line[Integer.parseInt(indexMsisdn)];
					if(msisdn==null || msisdn.trim().isEmpty() || msisdn.length() > 15) {
						Files.createDirectories(Paths.get("log_error", args[0],service_provider));
						FileUtils.writeStringToFile(
								new File(Paths.get("log_error", args[0],service_provider,FilenameUtils.removeExtension(new File(inputFile).getName())+".msisdn").toString()),
								msisdn+"\n",StandardCharsets.UTF_8, true);
						skip++;
						continue;
					}
					
					matcher = patternMsisdn.matcher(msisdn);
					if (matcher.find()) {
						msisdn = "84" + matcher.group("g1");
					}else {
                        Files.createDirectories(Paths.get("log_error", args[0],service_provider));
                        FileUtils.writeStringToFile(
                                new File(Paths.get("log_error", args[0],service_provider,FilenameUtils.removeExtension(new File(inputFile).getName())+".msisdn").toString()),
                                msisdn+"\n",StandardCharsets.UTF_8, true);
                        skip++;
                        continue;
					}

					String datetime = formaterOut.format(LocalDateTime.parse(line[Integer.parseInt(indexDateTime)], formaterIn));

					mapOutput.put("msisdn", msisdn);
					mapOutput.put("datetime", datetime);
					mapOutput.put("service_provider", service_provider);

					
					matcher = patternDate.matcher(datetime);
					matcher.matches();

					String yyyy=matcher.group("yyyy");
					String MM=matcher.group("MM");
					String dd=matcher.group("dd");

					for (Map.Entry<String, String> entry : mapIndex.entrySet()) {
						if (entry.getValue().charAt(0) == 'p') {

							matcher = patternP.matcher(entry.getValue());
							matcher.matches();

							String cell = line[Integer.parseInt(matcher.group(3))];

							if (matcher.group(2).equals("i")) {
								mapOutput.put(entry.getKey(), Integer.parseInt(cell));
							} else if (matcher.group(2).equals("l")) {
								mapOutput.put(entry.getKey(), Long.parseLong(cell));
							} else if (matcher.group(2).equals("f")) {
								mapOutput.put(entry.getKey(), Float.parseFloat(cell));
							} else if (matcher.group(2).equals("d")) {
								mapOutput.put(entry.getKey(), Double.parseDouble(cell));
							} else if (matcher.group(2).equals("t")) {
								String patternDateTime2 = matcher.group(5);
								DateTimeFormatter formaterIn2 = DateTimeFormatter
										.ofPattern(patternDateTime2);
								String patternDateTime3 = matcher.group(6);
								DateTimeFormatter formaterOut2 = DateTimeFormatter
										.ofPattern(patternDateTime3);
								try {
									mapOutput.put(entry.getKey(), formaterOut2
											.format(LocalDateTime.parse(cell, formaterIn2)));
								} catch (java.time.format.DateTimeParseException e) {
									mapOutput.put(entry.getKey(),
											formaterOut2.format(LocalDate.parse(cell, formaterIn2)));
								}
							} else {
								mapOutput.put(entry.getKey(), cell);
							}
						} else {
							matcher = patternS.matcher(entry.getValue());
							matcher.matches();
							if (!matcher.group(2).equals("null")) {
								mapOutput.put(entry.getKey(), matcher.group(2));
							}
						}
					}
					
					if(mapOutput.toString().length()>300) {
                        Files.createDirectories(Paths.get("log_error", args[0],service_provider));
                        FileUtils.writeStringToFile(
                                        new File(Paths.get("log_error", args[0],service_provider,FilenameUtils.removeExtension(new File(inputFile).getName())+".jsonstring").toString()),
                                        msisdn+"\n",StandardCharsets.UTF_8, true);
                        skip++;
                        continue;
					}

					listInsert.add(new Data(msisdn,yyyy+"-"+MM+"-"+dd,mapOutput.toString()));
					
					if(count % batch == 0) {
						long beforeInsert = System.currentTimeMillis();
						Database.insertTH(conn, listInsert);
						msInsert += (System.currentTimeMillis()-beforeInsert);
						
						listInsert.clear();
						Files.createDirectories(Paths.get("log", args[0],service_provider));
						long duration = System.currentTimeMillis() - begin;
						output = "running: " + count + "\n"
								+ "duration: " + duration + " ms\n"
								+ "insert: " + msInsert + " ms, ("+((double)msInsert/duration)+")\n"
								+ "1hour: " + count * 60000 / duration * 60 + "\n"
								+ "6000b: " + 6000000000f/(count * 60000 / duration * 60) / 24;
						Files.write(Paths.get("log", args[0],service_provider,FilenameUtils.removeExtension(new File(inputFile).getName())+".running"), output.getBytes(StandardCharsets.UTF_8));
					}
				} catch (Exception e2) {
					if(System.getProperty("debug")!=null && System.getProperty("debug").equals("Y")) {
						System.out.print(count+"-");
						System.out.println("\n"+e2.fillInStackTrace());
					}
					Files.createDirectories(Paths.get("log_error", args[0],service_provider));
					FileUtils.writeStringToFile(
							new File(Paths.get("log_error", args[0],service_provider,FilenameUtils.removeExtension(new File(inputFile).getName())+".excep").toString()),
							thisLine+"\n",StandardCharsets.UTF_8, true);
					skip++;
					continue;
				} finally {
					count++;
				}
			}
					
			

			Database.insertTH(conn, listInsert);
			listInsert.clear();
			
			Files.write(Paths.get("log", args[0],service_provider,FilenameUtils.removeExtension(new File(inputFile).getName())+".running"),
					(count+"\nfinish: "+(System.currentTimeMillis()-now)+"ms, skip "+skip+" lines\n\n"+output)
					.toString().getBytes(StandardCharsets.UTF_8));
			Files.move(Paths.get("log", args[0],service_provider,FilenameUtils.removeExtension(new File(inputFile).getName())+".running")
					,Paths.get("log", args[0],service_provider,FilenameUtils.removeExtension(new File(inputFile).getName())+".finished")
					,StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println("-----finish in "+(System.currentTimeMillis()-now)+"ms, read "+count+" lines, skip "+skip+" lines-----\n");
			
			count = 0;
			skip = 0;
			begin = System.currentTimeMillis();
			msInsert = 0;
			
			reader.close();

			TimeUnit.MILLISECONDS.sleep(100);
		}
	}

	

	public static TopupHistProcess app;

	public static TopupHistProcess getApp() {
		if (app == null) {
			try {
				app = new TopupHistProcess();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return app;
	}
}
