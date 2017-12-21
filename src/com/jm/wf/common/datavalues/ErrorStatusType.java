package com.jm.wf.common.datavalues;

public class ErrorStatusType
{
	/*
	 * Supported values for the SFlow Activation Status Code.
	 */
	public static final int ASC_OK                     = 200;
	public static final int ASC_EMPTY_NO_CONTENT_SEEN  = 499;
	public static final int ASC_NO_REPLY_OR_TIMEOUT    = 599;
	public static final int ASC_UNKNOWN_SERVICE        = 800;
	
	/*
	 * Supported values for the TFlow success field.
	 */
	public static final int TFLOW_SUCCESS_NOERROR      = 0;
	public static final int TFLOW_SUCCESS_TCPERROR     = 1;
	public static final int TFLOW_SUCCESS_WAPERROR     = 2;	
}