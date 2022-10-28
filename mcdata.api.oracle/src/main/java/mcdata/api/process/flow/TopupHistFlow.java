package mcdata.api.process.flow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections4.ListUtils;

import mcdata.api.common.Constants;
import mcdata.api.common.PropertiesReader;
import mcdata.api.db.DatabaseEx;
import mcdata.api.exception.ApiException;
import mcdata.api.exception.NotStartException;
import mcdata.api.exception.ProcessingException;
import mcdata.api.exception.ValidateException;
import mcdata.api.model.ResponseObject;
import mcdata.api.model.TopupHistRequest;
import mcdata.api.server.impl.PlatformImpl;

public class TopupHistFlow {
	static Exception sqlExcep = null;
	
	public static ResponseObject run(TopupHistRequest bodyObj, ResponseObject responseObject) throws ApiException, Exception {

		List<String> responseBody = new ArrayList<>();
		
		DateFormat formater = new SimpleDateFormat("yyyyMMdd");

		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();

		startDate.setTime(formater.parse(bodyObj.getStart_date()));
		endDate.setTime(formater.parse(bodyObj.getEnd_date()));
		
		DateTimeFormatter formaterDateIn = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter formaterDateOut = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		if(startDate.get(Calendar.DAY_OF_MONTH) != 1 || endDate.get(Calendar.DAY_OF_MONTH) != 1 || !startDate.before(endDate)) {
			throw new ValidateException("sai yyyyMMdd");
		}

		PropertiesReader propReader = new PropertiesReader(Constants.Properties.DATA_PROP_FILE);
		while (startDate.before(endDate)) {
			String year = startDate.get(Calendar.YEAR) + "";
			String month = (startDate.get(Calendar.MONTH) + 1) > 9 
					? ("" + (startDate.get(Calendar.MONTH) + 1))
					: ("0" + (startDate.get(Calendar.MONTH) + 1));
			startDate.add(Calendar.MONTH, 1);
			
			
			String status = propReader.getValue(year+"-"+month);
			if(status==null || !status.matches("processing|finish")) {
				throw new NotStartException(month, year);
			}
			if(status.matches("processing")){
				throw new ProcessingException(month, year);
			}
			
		}
		startDate.setTime(formater.parse(bodyObj.getStart_date()));
		
		
		int numThreads = PlatformImpl.getPlatform().getServerProp().getMax_thread();
		int msisdnArrayLength = bodyObj.getMsisdns().length;
		
		if(msisdnArrayLength<=numThreads) {
			responseBody.addAll(DatabaseEx.selectTH(bodyObj.getMsisdns(),
					formaterDateOut.format(LocalDate.parse(bodyObj.getStart_date(), formaterDateIn)),
					formaterDateOut.format(LocalDate.parse(bodyObj.getEnd_date(), formaterDateIn))));
		} else {
			List<List<String>> msisdn2D = ListUtils.partition(Arrays.asList(bodyObj.getMsisdns()),
					(msisdnArrayLength % numThreads==0?(msisdnArrayLength/numThreads):(msisdnArrayLength/numThreads)+1));
			Iterator<List<String>> it = msisdn2D.iterator();

			numThreads = msisdn2D.size();
			
			ExecutorService executor = Executors.newFixedThreadPool(numThreads);
			for (int i = 0; i < numThreads; i++) {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							responseBody.addAll(DatabaseEx.selectTH(getList(it).toArray(new String[0]),
									formaterDateOut.format(LocalDate.parse(bodyObj.getStart_date(), formaterDateIn)),
									formaterDateOut.format(LocalDate.parse(bodyObj.getEnd_date(), formaterDateIn))));
							
						} catch (Exception e) {
							sqlExcep=e;
						}
					}
				});
			}
			executor.shutdown();
			try {
				// wait to all thread done
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				throw new ApiException(-1, "Server Error");
			}
			if(sqlExcep!=null) {
				throw sqlExcep;
			}
		}
		
		
		
		responseObject.setResponse_body(responseBody);
		responseObject.setBatch(responseBody.size());
		
		return responseObject;
		
	}
	
	static Lock lock = new ReentrantLock();
	public static List<String> getList(Iterator<List<String>> it) {
		try {
			lock.lock();
			if (it.hasNext()) {
				return it.next();
			}
		} finally {
			// TODO: handle finally clause
			lock.unlock();
		}
		return null;
	}
}
