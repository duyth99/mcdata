package mcdata.api.provider;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;

import mcdata.api.common.Constants;
import mcdata.api.exception.ApiException;
import mcdata.api.exception.CertificateException;
import mcdata.api.exception.ValidateException;
import mcdata.api.model.AllRequest;
import mcdata.api.model.ArpuRequest;
import mcdata.api.model.BaseInfoRequest;
import mcdata.api.model.CallHistRequest;
import mcdata.api.model.RequestObject;
import mcdata.api.model.SmsHistRequest;
import mcdata.api.model.TopupHistRequest;
import mcdata.api.server.impl.PlatformImpl;

public class Provider {


	public static <T> T process(final ApiProcessingInterface<T> p, final RequestObject requestObject, int maxMsisdn) throws ApiException, Exception {

		verify(requestObject);
		T r = p.process(requestObject, maxMsisdn);
		return r;
	}
	
//	public static <T> T processN(final ApiProcessingInterface<T> p, final RequestObject requestObject) throws ApiException, Exception {
//
//		T r = p.process(requestObject);
//		return r;
//	}

	public static void verify(RequestObject requestObject) throws ApiException, Exception{
		
		String token = requestObject.getToken();
		String request_id = requestObject.getRequest_id();
		String request_type = requestObject.getRequest_type();
		Object request_body = requestObject.getRequest_body();
		
		Gson gson = new Gson();
		String requestBody="";
		switch (request_type) {
		case "base_info":
			requestBody = gson.toJson(gson.fromJson(gson.toJson(request_body), BaseInfoRequest.class));
			break;
		case "arpu":
			requestBody = gson.toJson(gson.fromJson(gson.toJson(request_body), ArpuRequest.class));
			break;
		case "call_hist":
			requestBody = gson.toJson(gson.fromJson(gson.toJson(request_body), CallHistRequest.class));
			break;
		case "sms_hist":
			requestBody = gson.toJson(gson.fromJson(gson.toJson(request_body), SmsHistRequest.class));
			break;
		case "topup_hist":
			requestBody = gson.toJson(gson.fromJson(gson.toJson(request_body), TopupHistRequest.class));
			break;
		case "all":
			requestBody = gson.toJson(gson.fromJson(gson.toJson(request_body), AllRequest.class));
			break;

		default:
			throw new ValidateException("sai request_type: " + requestObject.getRequest_type());
	}
//		System.out.println(gson.toJson(request_body));
//		System.out.println(requestBody);
//		System.out.println(genToken(request_id, request_type, requestBody, getSecretKey(request_id)));
		if (!validate(token, request_id, request_type, requestBody)) {
			throw new ValidateException();
		}
		
		
		if (!token.equals(genToken(request_id, request_type, requestBody, getSecretKey(request_id)))) {
			throw new CertificateException("sai token");
		}
		
	}


	// validate input
	public static boolean validate(String token, String request_id, String request_type, String request_body) {
		
		if(token == null || token.trim().isEmpty() || !token.matches("[a-fA-F0-9]{64}")){
			return false;
		}
		if(request_id == null || request_id.trim().isEmpty() || !request_id.matches("[a-zA-Z0-9]{1,}")){
			return false;
		}
		if(request_type == null || request_type.trim().isEmpty() || !request_type.matches("base_info|arpu|call_hist|sms_hist|topup_hist|all")){
			return false;
		}
		if(request_body == null || request_body.trim().isEmpty()){
			return false;
		}
		

		return true;
	}

	public static List<String> getAllClient() throws ApiException, Exception{
		List<String> list = new ArrayList<>();
	    
	    File fXmlFile = new File(PlatformImpl.getPlatform().getServerProp().getLinkCertificate());
	    
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    Document doc = dBuilder.parse(fXmlFile);
	    doc.getDocumentElement().normalize();
	    NodeList nList = doc.getElementsByTagName("client");
	    for (int temp = 0; temp < nList.getLength(); temp++) {
	        Node nNode = nList.item(temp);
	        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	            Element eElement = (Element) nNode;
	            list.add(eElement.getElementsByTagName("request_id").item(0).getTextContent());
	        }
	    }
	    return list;
	}
	
	public static String getSecretKey(String request_id) throws ApiException, Exception{
	    String secretKey = null;
	    
	    File fXmlFile = new File(PlatformImpl.getPlatform().getServerProp().getLinkCertificate());
	    
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    Document doc = dBuilder.parse(fXmlFile);
	    doc.getDocumentElement().normalize();
	    NodeList nList = doc.getElementsByTagName("client");
	    for (int temp = 0; temp < nList.getLength(); temp++) {
	        Node nNode = nList.item(temp);
	        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	            Element eElement = (Element) nNode;
	            if(eElement.getElementsByTagName("request_id").getLength() !=0 
	            		&& eElement.getElementsByTagName("request_id").item(0).getTextContent().equals(request_id)) {
	            	secretKey = eElement.getElementsByTagName("secret_key").item(0).getTextContent();
	            	break;
	            }
	        }
	    }
	    if(secretKey == null) {
	    	throw new CertificateException("khong ton tai request_id: " + request_id);
	    }
	    return secretKey;
	}
	
	
	public static boolean validCertificate(String fileName) throws ApiException, Exception{
	    boolean ok=false;
	    
	    if(!Files.exists(Paths.get(fileName))) {
	    	return false;
	    }
	    File fXmlFile = new File(fileName);
	    
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    Document doc = dBuilder.parse(fXmlFile);
	    doc.getDocumentElement().normalize();
	    NodeList nList = doc.getElementsByTagName("client");
	    for (int temp = 0; temp < nList.getLength(); temp++) {
	        Node nNode = nList.item(temp);
	        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	            Element eElement = (Element) nNode;
	            if(eElement.getElementsByTagName("request_id").getLength() > 0) {
	            	ok=true;
	            	break;
	            }
	        }
	    }
	    return ok;
	}
	

	public static String genToken(String request_id, String request_type, String request_body, String secretKey) {
		String license_str = Constants.SP + secretKey + Constants.SP + request_id + Constants.SP + request_type + Constants.SP + request_body + Constants.SP;
		String licens_key = SHA256(license_str);
		return licens_key;

	}

	public static String SHA256(String inp) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(inp.getBytes("UTF-8"));
			byte[] digest = md.digest();
			return String.format("%064x", new java.math.BigInteger(1, digest));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
