package mcdata.api.provider;

import mcdata.api.exception.ApiException;
import mcdata.api.model.RequestObject;
import mcdata.api.model.ResponseObject;

@FunctionalInterface
public interface ApiProcessingInterface<T>{
	T process(final RequestObject requestObject, ResponseObject responseObject, int maxMsisdn) throws ApiException, Exception;
}
