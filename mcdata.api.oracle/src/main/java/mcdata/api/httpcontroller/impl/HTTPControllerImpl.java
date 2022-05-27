/**
 * 
 */
package mcdata.api.httpcontroller.impl;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import mcdata.api.common.Constants;
import mcdata.api.common.Utils;
import mcdata.api.exception.ApiException;
import mcdata.api.exception.OverloadedException;
import mcdata.api.exception.ValidateException;
import mcdata.api.httpcontroller.HTTPController;
import mcdata.api.model.RequestObject;
import mcdata.api.model.ResponseObject;
import mcdata.api.process.GetAll;
import mcdata.api.process.GetArpu;
import mcdata.api.process.GetBaseInfo;
import mcdata.api.process.GetCallHist;
import mcdata.api.process.GetSmsHist;
import mcdata.api.process.GetTopupHist;
import mcdata.api.provider.Provider;
import mcdata.api.server.impl.PlatformImpl;


@Path("/")
@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED })
@Produces(MediaType.APPLICATION_JSON)
public class HTTPControllerImpl implements HTTPController {
	private static Logger logger = LoggerFactory.getLogger("api_logger");

	@Path("/ping")
	@GET
	public Response ping() {

//		try {
//			System.out.println(DatabaseEx.ping());
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return Response.ok().entity("pong").build();
	}

	@Path("/telco-data/base-info") 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response baseInfo(String json) {
		Gson gson = new Gson();
		
		ResponseObject responseObject = new ResponseObject();
		RequestObject requestObject = gson.fromJson(json, RequestObject.class);
		try {
			logger.info("listen ... -- " + json.toString());
			if (!PlatformImpl.getPlatform().tryAcquireTrain(requestObject.getRequest_id())) {
				throw new OverloadedException();
			}
			
			
			if(!requestObject.getRequest_type().equals("base_info")) {
				throw new ValidateException();
			}
			
			
			responseObject = Provider.process(GetBaseInfo.PROCESS, requestObject, PlatformImpl.getPlatform().getServerProp().getMax_msisdns_train());
			
			
			if(responseObject.getResponse_status_code() == null) {
				responseObject.setResponse_status_code(Constants.Code.OK);
				responseObject.setResponse_status_dsc(Constants.Description.OK);
			}

			logger.info("listen end -- ");

			// sleep
			TimeUnit.MILLISECONDS.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			// return
			return Response.ok().entity(gson.toJson(responseObject)).build();

		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getCode() + ":" + e.getLocalizedMessage()+ " -- " + json);
			responseObject.setResponse_status_code(e.getCode());
			responseObject.setResponse_status_dsc(e.getDsc());
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (JsonSyntaxException e) {
			logger.error(Constants.Error.EVALIDATION + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.EVALIDATION);
			responseObject.setResponse_status_dsc(Constants.Error.EVALIDATION+"/json error");
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (Exception e) {
			logger.error("Error", e.fillInStackTrace());
			logger.error(Constants.Error.ERROR + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.ERROR);
			responseObject.setResponse_status_dsc(Constants.Error.ERROR+"/"+Constants.Description.ERROR);
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} finally {
			PlatformImpl.getPlatform().releaseSemaphoreTrain(requestObject.getRequest_id());
		}
	}
	
	@Path("/telco-data/arpu") 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response arpu(String json) {
		Gson gson = new Gson();
		
		ResponseObject responseObject = new ResponseObject();
		RequestObject requestObject = gson.fromJson(json, RequestObject.class);
		try {
			logger.info("listen ... -- " + json);
			if (!PlatformImpl.getPlatform().tryAcquireTrain(requestObject.getRequest_id())) {
				throw new OverloadedException();
			}
			
			
			if(!requestObject.getRequest_type().equals("arpu")) {
				throw new ValidateException();
			}
			
			
			responseObject = Provider.process(GetArpu.PROCESS, requestObject, PlatformImpl.getPlatform().getServerProp().getMax_msisdns_train());
			
			if(responseObject.getResponse_status_code() == null) {
				responseObject.setResponse_status_code(Constants.Code.OK);
				responseObject.setResponse_status_dsc(Constants.Description.OK);
			}

			logger.info("listen end -- ");

			// sleep
			TimeUnit.MILLISECONDS.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			// return
			return Response.ok().entity(gson.toJson(responseObject)).build();

		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getCode() + ":" + e.getLocalizedMessage()+ " -- " + json);
			responseObject.setResponse_status_code(e.getCode());
			responseObject.setResponse_status_dsc(e.getDsc());
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (JsonSyntaxException e) {
			logger.error(Constants.Error.EVALIDATION + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.EVALIDATION);
			responseObject.setResponse_status_dsc(Constants.Error.EVALIDATION+"/json error");
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (Exception e) {
			logger.error("Error", e.fillInStackTrace());
			logger.error(Constants.Error.ERROR + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.ERROR);
			responseObject.setResponse_status_dsc(Constants.Error.ERROR+"/"+Constants.Description.ERROR);
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} finally {
			PlatformImpl.getPlatform().releaseSemaphoreTrain(requestObject.getRequest_id());
		}
	}
	
	@Path("/telco-data/call-hist") 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response callHist(String json) {
		Gson gson = new Gson();
		
		ResponseObject responseObject = new ResponseObject();
		RequestObject requestObject = gson.fromJson(json, RequestObject.class);
		try {
			logger.info("listen ... -- " + json);
			
			
			if (!PlatformImpl.getPlatform().tryAcquireTrain(requestObject.getRequest_id())) {
				throw new OverloadedException();
			}
			
			
			if(!requestObject.getRequest_type().equals("call_hist")) {
				throw new ValidateException();
			}
			
			
			responseObject = Provider.process(GetCallHist.PROCESS, requestObject, PlatformImpl.getPlatform().getServerProp().getMax_msisdns_train());
			
			
			if(responseObject.getResponse_status_code() == null) {
				responseObject.setResponse_status_code(Constants.Code.OK);
				responseObject.setResponse_status_dsc(Constants.Description.OK);
			}

			logger.info("listen end -- ");

			// sleep
			TimeUnit.MILLISECONDS.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			// return
			return Response.ok().entity(gson.toJson(responseObject)).build();

		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getCode() + ":" + e.getLocalizedMessage()+ " -- " + json);
			responseObject.setResponse_status_code(e.getCode());
			responseObject.setResponse_status_dsc(e.getDsc());
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (JsonSyntaxException e) {
			logger.error(Constants.Error.EVALIDATION + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.EVALIDATION);
			responseObject.setResponse_status_dsc(Constants.Error.EVALIDATION+"/json error");
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (Exception e) {
			logger.error("Error", e.fillInStackTrace());
			logger.error(Constants.Error.ERROR + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.ERROR);
			responseObject.setResponse_status_dsc(Constants.Error.ERROR+"/"+Constants.Description.ERROR);
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} finally {
			PlatformImpl.getPlatform().releaseSemaphoreTrain(requestObject.getRequest_id());
		}
	}
	
	@Path("/telco-data/sms-hist") 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response smsHist(String json) {
		Gson gson = new Gson();
		
		ResponseObject responseObject = new ResponseObject();
		RequestObject requestObject = gson.fromJson(json, RequestObject.class);
		try {
			logger.info("listen ... -- " + json.toString());
			if (!PlatformImpl.getPlatform().tryAcquireTrain(requestObject.getRequest_id())) {
				throw new OverloadedException();
			}
			
			
			if(!requestObject.getRequest_type().equals("sms_hist")) {
				throw new ValidateException();
			}
			
			
			responseObject = Provider.process(GetSmsHist.PROCESS, requestObject, PlatformImpl.getPlatform().getServerProp().getMax_msisdns_train());
			
			
			if(responseObject.getResponse_status_code() == null) {
				responseObject.setResponse_status_code(Constants.Code.OK);
				responseObject.setResponse_status_dsc(Constants.Description.OK);
			}

			logger.info("listen end -- ");

			// sleep
			TimeUnit.MILLISECONDS.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			// return
			return Response.ok().entity(gson.toJson(responseObject)).build();

		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getCode() + ":" + e.getLocalizedMessage()+ " -- " + json);
			responseObject.setResponse_status_code(e.getCode());
			responseObject.setResponse_status_dsc(e.getDsc());
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (JsonSyntaxException e) {
			logger.error(Constants.Error.EVALIDATION + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.EVALIDATION);
			responseObject.setResponse_status_dsc(Constants.Error.EVALIDATION+"/json error");
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (Exception e) {
			logger.error("Error", e.fillInStackTrace());
			logger.error(Constants.Error.ERROR + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.ERROR);
			responseObject.setResponse_status_dsc(Constants.Error.ERROR+"/"+Constants.Description.ERROR);
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} finally {
			PlatformImpl.getPlatform().releaseSemaphoreTrain(requestObject.getRequest_id());
		}
	}
	
	@Path("/telco-data/topup-hist") 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response topup(String json) {
		Gson gson = new Gson();
		
		ResponseObject responseObject = new ResponseObject();
		RequestObject requestObject = gson.fromJson(json, RequestObject.class);
		try {
			logger.info("listen ... -- " + json.toString());
			if (!PlatformImpl.getPlatform().tryAcquireTrain(requestObject.getRequest_id())) {
				throw new OverloadedException();
			}
			
			
			if(!requestObject.getRequest_type().equals("topup_hist")) {
				throw new ValidateException();
			}
			
			
			responseObject = Provider.process(GetTopupHist.PROCESS, requestObject, PlatformImpl.getPlatform().getServerProp().getMax_msisdns_train());
			
			
			if(responseObject.getResponse_status_code() == null) {
				responseObject.setResponse_status_code(Constants.Code.OK);
				responseObject.setResponse_status_dsc(Constants.Description.OK);
			}

			logger.info("listen end -- ");

			// sleep
			TimeUnit.MILLISECONDS.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			// return
			return Response.ok().entity(gson.toJson(responseObject)).build();

		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getCode() + ":" + e.getLocalizedMessage()+ " -- " + json);
			responseObject.setResponse_status_code(e.getCode());
			responseObject.setResponse_status_dsc(e.getDsc());
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (JsonSyntaxException e) {
			logger.error(Constants.Error.EVALIDATION + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.EVALIDATION);
			responseObject.setResponse_status_dsc(Constants.Error.EVALIDATION+"/json error");
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (Exception e) {
			logger.error("Error", e.fillInStackTrace());
			logger.error(Constants.Error.ERROR + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.ERROR);
			responseObject.setResponse_status_dsc(Constants.Error.ERROR+"/"+Constants.Description.ERROR);
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} finally {
			PlatformImpl.getPlatform().releaseSemaphoreTrain(requestObject.getRequest_id());
		}
	}
	
	
	@Path("/telco-data/all") 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response all(String json) {
		Gson gson = new Gson();
		
		ResponseObject responseObject = new ResponseObject();
		RequestObject requestObject = gson.fromJson(json, RequestObject.class);
		try {
			logger.info("listen ... -- " + json.toString());
			if (!PlatformImpl.getPlatform().tryAcquireTrain(requestObject.getRequest_id())) {
				throw new OverloadedException();
			}
			
			
			responseObject = Provider.process(GetAll.PROCESS, requestObject, PlatformImpl.getPlatform().getServerProp().getMax_msisdns_train());
			
			if(responseObject.getResponse_status_code() == null) {
				responseObject.setResponse_status_code(Constants.Code.OK);
				responseObject.setResponse_status_dsc(Constants.Description.OK);
			}

			logger.info("listen end -- ");

			// sleep
			TimeUnit.MILLISECONDS.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			// return
			return Response.ok().entity(gson.toJson(responseObject)).build();

		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getCode() + ":" + e.getLocalizedMessage()+ " -- " + json);
			responseObject.setResponse_status_code(e.getCode());
			responseObject.setResponse_status_dsc(e.getDsc());
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (JsonSyntaxException e) {
			logger.error(Constants.Error.EVALIDATION + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.EVALIDATION);
			responseObject.setResponse_status_dsc(Constants.Error.EVALIDATION+"/json error");
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (Exception e) {
			logger.error("Error", e.fillInStackTrace());
			logger.error(Constants.Error.ERROR + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.ERROR);
			responseObject.setResponse_status_dsc(Constants.Error.ERROR+"/"+Constants.Description.ERROR);
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} finally {
			PlatformImpl.getPlatform().releaseSemaphoreTrain(requestObject.getRequest_id());
		}
	}
	
	
	@Path("/telco-data/sf/base-info") 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response baseInfoSingleFetch(String json) {
		Gson gson = new Gson();
		
		ResponseObject responseObject = new ResponseObject();
		RequestObject requestObject = gson.fromJson(json, RequestObject.class);
		try {
			logger.info("listen ... -- " + json.toString());
			if (!PlatformImpl.getPlatform().tryAcquireFetch(requestObject.getRequest_id())) {
				throw new OverloadedException();
			}
			
			
			if(!requestObject.getRequest_type().equals("base_info")) {
				throw new ValidateException();
			}
			
			
			responseObject = Provider.process(GetBaseInfo.PROCESS, requestObject, PlatformImpl.getPlatform().getServerProp().getMax_msisdns_fetch());
			
			
			if(responseObject.getResponse_status_code() == null) {
				responseObject.setResponse_status_code(Constants.Code.OK);
				responseObject.setResponse_status_dsc(Constants.Description.OK);
			}

			logger.info("listen end -- ");

			// sleep
			TimeUnit.MILLISECONDS.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			// return
			return Response.ok().entity(gson.toJson(responseObject)).build();

		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getCode() + ":" + e.getLocalizedMessage()+ " -- " + json);
			responseObject.setResponse_status_code(e.getCode());
			responseObject.setResponse_status_dsc(e.getDsc());
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (JsonSyntaxException e) {
			logger.error(Constants.Error.EVALIDATION + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.EVALIDATION);
			responseObject.setResponse_status_dsc(Constants.Error.EVALIDATION+"/json error");
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (Exception e) {
			logger.error("Error", e.fillInStackTrace());
			logger.error(Constants.Error.ERROR + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.ERROR);
			responseObject.setResponse_status_dsc(Constants.Error.ERROR+"/"+Constants.Description.ERROR);
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} finally {
			PlatformImpl.getPlatform().releaseSemaphoreFetch(requestObject.getRequest_id());
		}
	}
	
	@Path("/telco-data/sf/arpu") 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response arpuSingleFetch(String json) {
		Gson gson = new Gson();
		
		ResponseObject responseObject = new ResponseObject();
		RequestObject requestObject = gson.fromJson(json, RequestObject.class);
		try {
			logger.info("listen ... -- " + json);
			if (!PlatformImpl.getPlatform().tryAcquireFetch(requestObject.getRequest_id())) {
				throw new OverloadedException();
			}
			
			
			if(!requestObject.getRequest_type().equals("arpu")) {
				throw new ValidateException();
			}
			
			
			responseObject = Provider.process(GetArpu.PROCESS, requestObject, PlatformImpl.getPlatform().getServerProp().getMax_msisdns_fetch());
			
			if(responseObject.getResponse_status_code() == null) {
				responseObject.setResponse_status_code(Constants.Code.OK);
				responseObject.setResponse_status_dsc(Constants.Description.OK);
			}

			logger.info("listen end -- ");

			// sleep
			TimeUnit.MILLISECONDS.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			// return
			return Response.ok().entity(gson.toJson(responseObject)).build();

		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getCode() + ":" + e.getLocalizedMessage()+ " -- " + json);
			responseObject.setResponse_status_code(e.getCode());
			responseObject.setResponse_status_dsc(e.getDsc());
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (JsonSyntaxException e) {
			logger.error(Constants.Error.EVALIDATION + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.EVALIDATION);
			responseObject.setResponse_status_dsc(Constants.Error.EVALIDATION+"/json error");
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (Exception e) {
			logger.error("Error", e.fillInStackTrace());
			logger.error(Constants.Error.ERROR + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.ERROR);
			responseObject.setResponse_status_dsc(Constants.Error.ERROR+"/"+Constants.Description.ERROR);
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} finally {
			PlatformImpl.getPlatform().releaseSemaphoreFetch(requestObject.getRequest_id());
		}
	}
	
	@Path("/telco-data/sf/call-hist") 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response callHistSingleFetch(String json) {
		Gson gson = new Gson();
		
		ResponseObject responseObject = new ResponseObject();
		RequestObject requestObject = gson.fromJson(json, RequestObject.class);
		try {
			logger.info("listen ... -- " + json);
			
			
			if (!PlatformImpl.getPlatform().tryAcquireFetch(requestObject.getRequest_id())) {
				throw new OverloadedException();
			}
			
			
			if(!requestObject.getRequest_type().equals("call_hist")) {
				throw new ValidateException();
			}
			
			
			responseObject = Provider.process(GetCallHist.PROCESS, requestObject, PlatformImpl.getPlatform().getServerProp().getMax_msisdns_fetch());
			
			
			if(responseObject.getResponse_status_code() == null) {
				responseObject.setResponse_status_code(Constants.Code.OK);
				responseObject.setResponse_status_dsc(Constants.Description.OK);
			}

			logger.info("listen end -- ");

			// sleep
			TimeUnit.MILLISECONDS.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			// return
			return Response.ok().entity(gson.toJson(responseObject)).build();

		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getCode() + ":" + e.getLocalizedMessage()+ " -- " + json);
			responseObject.setResponse_status_code(e.getCode());
			responseObject.setResponse_status_dsc(e.getDsc());
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (JsonSyntaxException e) {
			logger.error(Constants.Error.EVALIDATION + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.EVALIDATION);
			responseObject.setResponse_status_dsc(Constants.Error.EVALIDATION+"/json error");
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (Exception e) {
			logger.error("Error", e.fillInStackTrace());
			logger.error(Constants.Error.ERROR + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.ERROR);
			responseObject.setResponse_status_dsc(Constants.Error.ERROR+"/"+Constants.Description.ERROR);
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} finally {
			PlatformImpl.getPlatform().releaseSemaphoreFetch(requestObject.getRequest_id());
		}
	}
	
	@Path("/telco-data/sf/sms-hist") 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response smsHistSingleFetch(String json) {
		Gson gson = new Gson();
		
		ResponseObject responseObject = new ResponseObject();
		RequestObject requestObject = gson.fromJson(json, RequestObject.class);
		try {
			logger.info("listen ... -- " + json.toString());
			if (!PlatformImpl.getPlatform().tryAcquireFetch(requestObject.getRequest_id())) {
				throw new OverloadedException();
			}
			
			
			if(!requestObject.getRequest_type().equals("sms_hist")) {
				throw new ValidateException();
			}
			
			
			responseObject = Provider.process(GetSmsHist.PROCESS, requestObject, PlatformImpl.getPlatform().getServerProp().getMax_msisdns_fetch());
			
			
			if(responseObject.getResponse_status_code() == null) {
				responseObject.setResponse_status_code(Constants.Code.OK);
				responseObject.setResponse_status_dsc(Constants.Description.OK);
			}

			logger.info("listen end -- ");

			// sleep
			TimeUnit.MILLISECONDS.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			// return
			return Response.ok().entity(gson.toJson(responseObject)).build();

		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getCode() + ":" + e.getLocalizedMessage()+ " -- " + json);
			responseObject.setResponse_status_code(e.getCode());
			responseObject.setResponse_status_dsc(e.getDsc());
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (JsonSyntaxException e) {
			logger.error(Constants.Error.EVALIDATION + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.EVALIDATION);
			responseObject.setResponse_status_dsc(Constants.Error.EVALIDATION+"/json error");
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (Exception e) {
			logger.error("Error", e.fillInStackTrace());
			logger.error(Constants.Error.ERROR + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.ERROR);
			responseObject.setResponse_status_dsc(Constants.Error.ERROR+"/"+Constants.Description.ERROR);
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} finally {
			PlatformImpl.getPlatform().releaseSemaphoreFetch(requestObject.getRequest_id());
		}
	}
	
	@Path("/telco-data/sf/topup-hist") 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response topupSingleFetch(String json) {
		Gson gson = new Gson();
		
		ResponseObject responseObject = new ResponseObject();
		RequestObject requestObject = gson.fromJson(json, RequestObject.class);
		try {
			logger.info("listen ... -- " + json.toString());
			if (!PlatformImpl.getPlatform().tryAcquireFetch(requestObject.getRequest_id())) {
				throw new OverloadedException();
			}
			
			
			if(!requestObject.getRequest_type().equals("topup_hist")) {
				throw new ValidateException();
			}
			
			
			responseObject = Provider.process(GetTopupHist.PROCESS, requestObject, PlatformImpl.getPlatform().getServerProp().getMax_msisdns_fetch());
			
			
			if(responseObject.getResponse_status_code() == null) {
				responseObject.setResponse_status_code(Constants.Code.OK);
				responseObject.setResponse_status_dsc(Constants.Description.OK);
			}

			logger.info("listen end -- ");

			// sleep
			TimeUnit.MILLISECONDS.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			// return
			return Response.ok().entity(gson.toJson(responseObject)).build();

		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getCode() + ":" + e.getLocalizedMessage()+ " -- " + json);
			responseObject.setResponse_status_code(e.getCode());
			responseObject.setResponse_status_dsc(e.getDsc());
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (JsonSyntaxException e) {
			logger.error(Constants.Error.EVALIDATION + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.EVALIDATION);
			responseObject.setResponse_status_dsc(Constants.Error.EVALIDATION+"/json error");
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (Exception e) {
			logger.error("Error", e.fillInStackTrace());
			logger.error(Constants.Error.ERROR + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.ERROR);
			responseObject.setResponse_status_dsc(Constants.Error.ERROR+"/"+Constants.Description.ERROR);
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} finally {
			PlatformImpl.getPlatform().releaseSemaphoreFetch(requestObject.getRequest_id());
		}
	}
	
	
	@Path("/telco-data/sf/all") 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response allSingleFetch(String json) {
		Gson gson = new Gson();
		
		ResponseObject responseObject = new ResponseObject();
		RequestObject requestObject = gson.fromJson(json, RequestObject.class);
		try {
			logger.info("listen ... -- " + json.toString());
			if (!PlatformImpl.getPlatform().tryAcquireFetch(requestObject.getRequest_id())) {
				throw new OverloadedException();
			}
			
			
			responseObject = Provider.process(GetAll.PROCESS, requestObject, PlatformImpl.getPlatform().getServerProp().getMax_msisdns_fetch());
			
			if(responseObject.getResponse_status_code() == null) {
				responseObject.setResponse_status_code(Constants.Code.OK);
				responseObject.setResponse_status_dsc(Constants.Description.OK);
			}

			logger.info("listen end -- ");

			// sleep
			TimeUnit.MILLISECONDS.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			// return
			return Response.ok().entity(gson.toJson(responseObject)).build();

		} catch (ApiException e) {
			e.printStackTrace();
			logger.info(e.getCode() + ":" + e.getLocalizedMessage()+ " -- " + json);
			responseObject.setResponse_status_code(e.getCode());
			responseObject.setResponse_status_dsc(e.getDsc());
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (JsonSyntaxException e) {
			logger.error(Constants.Error.EVALIDATION + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.EVALIDATION);
			responseObject.setResponse_status_dsc(Constants.Error.EVALIDATION+"/json error");
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} catch (Exception e) {
			logger.error("Error", e.fillInStackTrace());
			logger.error(Constants.Error.ERROR + ":" + e.getLocalizedMessage()+ " -- " + json);

			responseObject.setResponse_status_code(Constants.Code.ERROR);
			responseObject.setResponse_status_dsc(Constants.Error.ERROR+"/"+Constants.Description.ERROR);
			try {
				TimeUnit.MILLISECONDS
						.sleep(Integer.valueOf(Utils.getServerProperties().getApi_executing_delay_in_milliseconds()));
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return Response.ok().entity(gson.toJson(responseObject)).build();
		} finally {
			PlatformImpl.getPlatform().releaseSemaphoreFetch(requestObject.getRequest_id());
		}
	}
}
