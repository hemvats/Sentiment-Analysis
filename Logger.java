package com.company;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class Logger {
	void LogSentimentScores(File file, String s) throws IOException{

		FileWriter write = new FileWriter(file, true);
		Date dt = new Date();
		DateFormat dtf = new SimpleDateFormat("HH:mm:ss");
		write.append(dtf.format(dt)).append(" ==> ").append(s).append("\n");
		write.close();
	}
	void LogWordRatingFromTraining(File file, String s, double d, int i, int j) throws IOException{
		FileWriter write = new FileWriter(file, true);
		s = s + "\t" + d + "\t+" + i + "\t-" + j;
		write.append(s).append("\n");
		write.close();
	}

	void LogTestsResults(File file, String s, double score, int res ) throws IOException{
		FileWriter write = new FileWriter(file, true);
		s = s + "\t" + score + "\tResult: " + res;
		write.append(s).append("\n");
		write.close();
	}

	File createLogFile(String folder, String name, boolean createTimeStamp) throws IOException
	{
		Date date = new Date();
		long epoch_time = date.getTime();
		String full_filename;
		if (createTimeStamp) {
			full_filename = name + "_" + String.valueOf(epoch_time) + ".txt";
		} else {
			full_filename = name + ".txt";
		}
		File file = new File(folder + full_filename);
		if (file.createNewFile()) {
			System.out.println("Log Created : " + full_filename);
		} else {
			System.out.println("Log File Exists : " + full_filename);
			FileWriter write = new FileWriter(file, false);
			write.close();
		}
		return file;
	}
}
