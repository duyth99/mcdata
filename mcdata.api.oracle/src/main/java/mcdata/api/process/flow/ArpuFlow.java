package mcdata.api.process.flow;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
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
import mcdata.api.model.ArpuRequest;
import mcdata.api.model.ResponseObject;
import mcdata.api.server.impl.PlatformImpl;

public class ArpuFlow {
	static Exception sqlExcep = null;
	
//	static int outputBatchSize = 0;
//	public static synchronized int plusBatch(int plus) {
//		return outputBatchSize+=plus;
//	}
	public static ResponseObject run(ArpuRequest bodyObj, ResponseObject responseObject) throws ApiException, Exception {

		List<HashMap<String, Object>> responseBody = new ArrayList<>();
		
		DateFormat formater = new SimpleDateFormat("yyyyMMdd");

		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();

		startDate.setTime(formater.parse(bodyObj.getStart_date()));
		endDate.setTime(formater.parse(bodyObj.getEnd_date()));
		
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
		
		
		int outputBatchSize = 0;
		while (startDate.before(endDate)) {
			
			HashMap<String, Object> map = new HashMap<>();

//			List<String> listArpu = new ArrayList<>();
			
			String year = startDate.get(Calendar.YEAR) + "";
			String month = (startDate.get(Calendar.MONTH) + 1) > 9 
					? ("" + (startDate.get(Calendar.MONTH) + 1))
					: ("0" + (startDate.get(Calendar.MONTH) + 1));
			
			Calendar c = Calendar.getInstance();
			c.setTime(new SimpleDateFormat("yyyyMMdd").parse(year+month+"01"));
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			String day = c.get(Calendar.DAY_OF_MONTH)+"";
			
			map.put("snapshot_date", year+month+day);
			
		
			
//			List<Data> listArpu = new ArrayList<Data>();
//			for(int i=0;i<bodyObj.getMsisdns().length;i+=1000) {
//				if(i+1000>bodyObj.getMsisdns().length) {
//					listArpu.addAll(DatabaseEx.selectARPU(Arrays.copyOfRange(bodyObj.getMsisdns(), i, bodyObj.getMsisdns().length)));
//					continue;
//				}
//				listArpu.addAll(DatabaseEx.selectARPU(Arrays.copyOfRange(bodyObj.getMsisdns(), i, i+1000)));
//			}
			
			List<String> listArpu = new ArrayList<>();
			
			int numThreads = PlatformImpl.getPlatform().getServerProp().getMax_thread();
			int msisdnArrayLength = bodyObj.getMsisdns().length;
			
			if(msisdnArrayLength<=numThreads) {
				List<String> tmpList = DatabaseEx.selectARPU(bodyObj.getMsisdns(), year+"-"+month+"-"+"01", year+"-"+month+"-"+day);
				listArpu.addAll(tmpList);
//				plusBatch(tmpList.size());
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
								List<String> tmpList = DatabaseEx.selectARPU(getList(it).toArray(new String[0]), year+"-"+month+"-"+"01", year+"-"+month+"-"+day);
								listArpu.addAll(tmpList);
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
			
//			List<Data> listArpu = DatabaseEx.selectARPU(bodyObj.getMsisdns(), year+month+"01", year+month+day);
			
//			outputBatchSize = listArpu.size();
			
			map.put("arpu_data", listArpu);
			outputBatchSize+=listArpu.size();
			responseBody.add(map);
			
			startDate.add(Calendar.MONTH, 1);
		}

		
		
		responseObject.setResponse_body(responseBody);
		responseObject.setBatch(outputBatchSize);
		
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
