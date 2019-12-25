package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * 
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {
	//------------start edit--------------------**/
	private  T result;
	private boolean isDone;
	//------------end edit----------------------**/

	/**
	 * This should be the the only public constructor in this class.
	 */
	public Future() {
		//------------start edit--------------------**/
		result = null;
		isDone = false;
		//------------end edit ---------------------**/
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
     * 	       
     */
	public T get() {
		//------------start edit - 19/12----------------------**/
		synchronized (this){
			if (!isDone)		//changed from while to if - ONLY 1 TIME resolve is done
			{
				try {
					this.wait();
				} catch (InterruptedException e) {}
			}
			//this.notifyAll();
			return this.result;
		}
		//------------end edit - 19/12-----------------------**/
	}
	
	/**
     * Resolves the result of this Future object.
     */
	public void resolve (T result) {
		//------------start edit - 16/12--------------------**/
		synchronized (this){
			if(!isDone){
				this.result=result;
				isDone=true;
			}
			this.notifyAll();				//Wake up the threads that waits for answer
		}
		//------------end edit - 16/12----------------------**/
	}
	
	/**
     * @return true if this object has been resolved, false otherwise
     */
	public boolean isDone() {
		//------------start edit - 16/12--------------------/
		return isDone;		//no need for sync - the isDone is only by reading
		//------------end edit - 16/12----------------------/
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     * @param timeout 	the maximal amount of time units to wait for the result.
     * @param unit		the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not, 
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
     */
	public T get(long timeout, TimeUnit unit) throws InterruptedException {
		//------------start edit - 16/12--------------------/
		synchronized (this){
			if(!isDone){
				unit.timedWait(this,timeout);	//waits untill passed time, or notified
			// old configure:	this.wait(unit.toMillis(timeout));
			}	//only 1 time will be resolved, than it surely made isDone=true
			return result;
		}
	}
		//------------end edit - 16/12----------------------/
}

