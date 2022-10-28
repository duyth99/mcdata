package mcdata.api.process.flow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.collections4.ListUtils;

import mcdata.api.db.DatabaseEx;
import mcdata.api.exception.ApiException;
import mcdata.api.model.BaseInfoRequest;
import mcdata.api.model.ResponseObject;
import mcdata.api.server.impl.PlatformImpl;

public class BaseInfoFlow {
	static Exception sqlExcep = null;
	
	public static ResponseObject run(BaseInfoRequest bodyObj, ResponseObject responseObject) throws ApiException, Exception {

		List<String> responseBody = new ArrayList<>();
		
		DateTimeFormatter formaterDateIn = DateTimeFormatter.ofPattern("yyyyMMdd");
		DateTimeFormatter formaterDateOut = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		
//		responseBody.addAll(DatabaseEx.selectBPI(bodyObj.getMsisdns(),
//				formaterDateOut.format(LocalDate.parse(bodyObj.getSnapshot_date(), formaterDateIn))));

		int msisdnArrayLength = bodyObj.getMsisdns().length;
		int numThreads = PlatformImpl.getPlatform().getServerProp().getMax_thread();
		
		if(msisdnArrayLength<=numThreads) {
			responseBody.addAll(DatabaseEx.selectBPI(bodyObj.getMsisdns(),
					formaterDateOut.format(LocalDate.parse(bodyObj.getSnapshot_date(), formaterDateIn))));
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
							responseBody.addAll(DatabaseEx.selectBPI(getList(it).toArray(new String[0]),
									formaterDateOut.format(LocalDate.parse(bodyObj.getSnapshot_date(), formaterDateIn))));
							
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
