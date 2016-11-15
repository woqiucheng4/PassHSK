/**
 * 
 */
package com.qc.hsk.network.bean;


import com.qc.hsk.network.value.Version;

/**
 * @author CreditEase/FSO
 * 
 */
public class VersionBean extends BaseBean {

	private Version value;

	/**
	 * @return the value
	 */
	public final Version getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public final void setValue(Version value) {
		this.value = value;
	}

}
