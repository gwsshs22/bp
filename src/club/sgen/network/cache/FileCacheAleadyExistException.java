package club.sgen.network.cache;

public class FileCacheAleadyExistException extends RuntimeException {
	public FileCacheAleadyExistException(String s){
		super(s);
	}
}
