package club.sgen.network;

public interface AsyncCallback<T> {
	public void onResult(T result);

    public void exceptionOccured(Exception e);

    public void cancelled();
}
