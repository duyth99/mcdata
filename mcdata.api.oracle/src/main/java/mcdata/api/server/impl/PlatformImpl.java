/**
 * 
 */
package mcdata.api.server.impl;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mcdata.api.common.Constants;
import mcdata.api.common.DFSUtils;
import mcdata.api.common.PropertiesReader;
import mcdata.api.common.ServerProperties;
import mcdata.api.common.Utils;
import mcdata.api.db.DBUtils;
import mcdata.api.db.DatabaseInfo;
import mcdata.api.exception.CertificateException;
import mcdata.api.http.HTTPService;
import mcdata.api.http.impl.HTTPServiceImpl;
import mcdata.api.provider.Provider;
import mcdata.api.server.Platform;

/**
 * @author TrongHoang
 *
 */
public class PlatformImpl extends Platform {
	private static Logger logger = LoggerFactory.getLogger("api_logger");
	private static PlatformImpl singleton;
	private ServerProperties serverProp;
//	private Semaphore semaphore;
	private HashMap<String, Integer> semaphoreByClientTrain;
//	private HashMap<String, Integer> semaphoreByClientFetch;

	private PropertiesReader propReader;
	private HTTPService httpService;
//	private String rootFolder;
	private String cdrFolder;
	
	private Lock lock = new ReentrantLock();

	private DatabaseInfo db_info;
	private DBUtils dbUtils;
	/**
	 * @param args
	 */

	private PlatformImpl() {

	}

	private void init() throws Exception {
		initServer();
	}

	private void initServer() throws Exception {
		// global variable////
		logger.info("--------------- Init API Server Start---------------");
		setPropReader(new PropertiesReader(Constants.Properties.APP_PROP_FILE));

//		rootFolder = System.getenv("MCDATA");
		cdrFolder = System.getenv("MCCDR");
		
		serverProp = new ServerProperties();
		db_info = new DatabaseInfo(propReader);
		dbUtils = new DBUtils(db_info);
		try {
			if (!DFSUtils.lockFile((new File(serverProp.getFileLock())))) {
				logger.debug("File: " + serverProp.getFileLock() + " was locked => Module cannot start");
				return;
			} else {
				logger.debug("File: " + serverProp.getFileLock() + " locked => Start module");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e.fillInStackTrace());
//			return;
		}
		semaphoreByClientTrain = new HashMap<>();
		for (String clientId : Provider.getAllClient()) {
			semaphoreByClientTrain.put(clientId, Utils.getServerProperties().getMax_request_per_client_train());
//			logger.info(Utils.getServerProperties().getMax_request_per_client_train()+"");
		}
//		semaphoreByClientFetch = new HashMap<>();
//		for (String clientId : Provider.getAllClient()) {
//			semaphoreByClientFetch.put(clientId, Utils.getServerProperties().getMax_request_per_client_fetch());
////			logger.info(Utils.getServerProperties().getMax_request_per_client_fetch()+"");
//		}
		
//		semaphore = new Semaphore(Utils.getServerProperties().getMaximum_number_of_concurrent_requests());
		System.out.println(serverProp.getLinkCertificate());
		if(!Provider.validCertificate(serverProp.getLinkCertificate())) {
			logger.error("---file certificate error---");
			System.exit(-1);
		}
		httpService = new HTTPServiceImpl();
		httpService.startHTTPService();
		
		Thread.sleep(1500);
		logger.info("--------------- Init API Server End---------------");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			PlatformImpl.getPlatform().init();
		} catch (Exception e) {
			logger.info("Can not start server.");
			e.printStackTrace();
			logger.info("Error: " + e.toString());
		}
	}

	public synchronized static PlatformImpl getPlatform() {
		if (singleton == null) {
			singleton = new PlatformImpl();
		}
		return singleton;
	}

	public boolean tryAcquireTrain(String clientId) throws CertificateException {
		if(semaphoreByClientTrain.get(clientId)==null) {
			throw new CertificateException("khong ton tai user_id: " + clientId);
		}
		try {
			lock.lock();
			semaphoreByClientTrain.put(clientId, semaphoreByClientTrain.get(clientId)-1);
			return semaphoreByClientTrain.get(clientId)>=0;
		}finally {
			lock.unlock();
		}
		
		
	}
//	public boolean tryAcquireFetch(String clientId) throws CertificateException {
//		if(semaphoreByClientFetch.get(clientId)==null) {
//			throw new CertificateException("khong ton tai user_id: " + clientId);
//		}
//		try {
//			lock.lock();
//			semaphoreByClientFetch.put(clientId, semaphoreByClientFetch.get(clientId)-1);
//			return semaphoreByClientFetch.get(clientId)>=0;
//		}finally {
//			lock.unlock();
//		}
//	}
	
	public void releaseSemaphoreTrain(String clientId) {
		if(semaphoreByClientTrain.get(clientId)==null) {
			return;
		}
		lock.lock();
		semaphoreByClientTrain.put(clientId, semaphoreByClientTrain.get(clientId)+1);
		lock.unlock();
	}
//	public void releaseSemaphoreFetch(String clientId) {
//		if(semaphoreByClientFetch.get(clientId)==null) {
//			return;
//		}
//		lock.lock();
//		semaphoreByClientFetch.put(clientId, semaphoreByClientFetch.get(clientId)+1);
//		lock.unlock();
//	}
	
//	public boolean tryAcquire() {
//		return semaphore.tryAcquire();
//	}

	/**
	 * @return the propReader
	 */
	public PropertiesReader getPropertiesReader() {
		return propReader;
	}

	/**
	 * @param propReader the propReader to set
	 */
	public void setPropReader(PropertiesReader propReader) {
		this.propReader = propReader;
	}

	/**
	 * @return the serverProp
	 */
	public ServerProperties getServerProp() {
		return serverProp;
	}

	/**
	 * @param serverProp the serverProp to set
	 */
	public void setServerProp(ServerProperties serverProp) {
		this.serverProp = serverProp;
	}


	/**
	 * @return the semaphore
	 */
	
	
//	public Semaphore getSemaphore() {
//		return semaphore;
//	}

//	public String getRootFolder() {
//		return rootFolder;
//	}
//
//	public void setRootFolder(String rootFolder) {
//		this.rootFolder = rootFolder;
//	}

	public String getCdrFolder() {
		return cdrFolder;
	}

	public void setCdrFolder(String cdrFolder) {
		this.cdrFolder = cdrFolder;
	}

	
	public DBUtils getDbUtils() {
		return dbUtils;
	}

	public void setDbUtils(DBUtils dbUtils) {
		this.dbUtils = dbUtils;
	}
	
	
}
