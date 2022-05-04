package mcdata.process.utils;

@FunctionalInterface
public interface ProcessingInterface<T>{
	T process(final String p[]) throws Exception;
}
