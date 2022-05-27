package mcdata.api.provider;

import mcdata.api.exception.ApiException;
import mcdata.api.model.RequestObject;

@FunctionalInterface
public interface ApiProcessingInterface<T>{
	T process(final RequestObject requestObject, int maxMsisdn) throws ApiException, Exception;
}
