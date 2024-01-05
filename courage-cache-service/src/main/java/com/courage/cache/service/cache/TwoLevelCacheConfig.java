package com.courage.cache.service.cache;

/**
 * Cache config object used for Spring cache configuration.
 *
 * @author Nikita Koksharov
 *
 */
public class TwoLevelCacheConfig {

    private long ttl;

    private long maxIdleTime;
    
    private int maxSize;

    /**
     * Creates config object with
     * <code>ttl = 0</code> and <code>maxIdleTime = 0</code>.
     *
     */
    public TwoLevelCacheConfig() {
    }

    /**
     * Creates config object.
     *
     * @param ttl - time to live for key\value entry in milliseconds.
     *              If <code>0</code> then time to live doesn't affect entry expiration.
     * @param maxIdleTime - max idle time for key\value entry in milliseconds.
     * <p>
     * if <code>maxIdleTime</code> and <code>ttl</code> params are equal to <code>0</code>
     * then entry stores infinitely.
     */
    public TwoLevelCacheConfig(long ttl, long maxIdleTime) {
        super();
        this.ttl = ttl;
        this.maxIdleTime = maxIdleTime;
    }

    public long getTTL() {
        return ttl;
    }

    /**
     * Set time to live for key\value entry in milliseconds.
     *
     * @param ttl - time to live for key\value entry in milliseconds.
     *              If <code>0</code> then time to live doesn't affect entry expiration.
     */
    public void setTTL(long ttl) {
        this.ttl = ttl;
    }

    
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Set max size of map. Superfluous elements are evicted using LRU algorithm.
     *
     * @param maxSize - max size
     *                  If <code>0</code> the cache is unbounded (default).
     */
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public long getMaxIdleTime() {
        return maxIdleTime;
    }

    /**
     * Set max idle time for key\value entry in milliseconds.
     *
     * @param maxIdleTime - max idle time for key\value entry in milliseconds.
     *              If <code>0</code> then max idle time doesn't affect entry expiration.
     */
    public void setMaxIdleTime(long maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }


}
