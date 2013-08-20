package club.sgen.network;

public interface AsyncExecutorAware<T> {
	public void setAsyncExecutor(AsyncExecutor<T> asyncExecutor);
}
