package com.jm.wf.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jm.wf.common.logging.LoggerManager;

public class Alarmer 
{
	 // path to the raise_alarm function
    private static String raiseAlarmProgram = "/opt/platform7/lbin/raise_alarm";

    // define the various alert levels that we can have
    public enum alertLevel {
                             NONE,
                             MINOR,
                             MAJOR,
                             CRITICAL,
                             INFORMATION,
                             WARNING,
                             URGENT,
                             TASKFATAL,
                             FATAL;
                           };

    // main class so we can do debug testing
    public static void main(String args[]) 
    {
        int returnCode = 0;
        int errNumber  = 0;
        alertLevel errLevel = alertLevel.MINOR;
        String info1 = "";
        String info2 = "";

        // check if we have been passed the required number of parameters
        if (args.length < 2) {
            System.err.println("Action must be of the form: ErrLevel | ErrNumber [ info1 | info 2 ]");
            System.err.print("Was passed [" + args.length + "] args: [ ");
            for (int i = 0; i < args.length; i++) { 
                System.err.print(args[i] + " ");
            }
            System.err.println("]");
            System.exit(255);
        }

        // allocate the passed in args
        try 
        {
            // Allocate the error level
            errLevel  = alertLevel.values()[Integer.parseInt( args[0] )];

            // Allocate the error number
            errNumber = Integer.parseInt( args[1] );

            // check if we have been passed info1 or info2
            if (args.length >= 3) {
                info1 = args[2];
            }
            if (args.length >= 4) {
                info2 = args[3];
            }
        } 
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.err.println("Bad error level passed");
            System.exit(255);
        }
        catch (IllegalArgumentException e) 
        {
            System.err.println("Bad parameters passed");
            System.exit(255);
        }

        // raise the alarm
        returnCode = raiseAlarm (errLevel, errNumber, info1, info2);

        System.exit(returnCode);
    }

    public static int raiseAlarm (alertLevel severity, int alarmNum) 
    {
        return (issueAlarm (severity, alarmNum, "", ""));
    }

    public static int raiseAlarm (alertLevel severity, int alarmNum, String addInfo1) 
    {
        return (issueAlarm (severity, alarmNum, addInfo1, ""));
    }

    public static int raiseAlarm (alertLevel severity, int alarmNum, String addInfo1, String addInfo2) 
    {
        return (issueAlarm (severity, alarmNum, addInfo1, addInfo2));
    }

    private static int issueAlarm (alertLevel severity, int alarmNum, String addInfo1, String addInfo2) 
    {
        int returnCode = 0;
        
        String[] cmdArr = new String[5];

        File alm = new File(raiseAlarmProgram);
        if (alm.exists() && alm.canExecute())
	    {
	        // build up the command line we are going to use
	        cmdArr[0] = raiseAlarmProgram;
	        cmdArr[1] = "" + severity.ordinal();
	        cmdArr[2] = "" + alarmNum;
	        
	        if ( addInfo1.length() != 0 ) 
	        {
	            cmdArr[3] = addInfo1;
	        }
	        else
	        {
	        	cmdArr[3] = "";
	        }
	        
	        if ( addInfo2.length() != 0 ) 
	        {
	            cmdArr[4] = addInfo2;
	        }
	        else
	        {
	        	cmdArr[4] = "";
	        }
	
	        //LoggerManager.getLogger().info("Comman Array = " + cmdArr[0] + cmdArr[1] + cmdArr[2] + cmdArr[3] + cmdArr[4]);
	        
	        try 
	        {
	            // execute the command
	            Process p = Runtime.getRuntime().exec(cmdArr);
	
	            // read stdout from the command
	            BufferedReader stdInput = new BufferedReader(new 
	                 InputStreamReader(p.getInputStream()));
	            
	            while ((stdInput.readLine()) != null) {};
	
	            // read stderr from the command
	            BufferedReader stdError = new BufferedReader(new 
	                 InputStreamReader(p.getErrorStream()));
	            
	            while ((stdError.readLine()) != null) {};
	
	            // check the return code
	            int exitCode = p.waitFor();
	            if ( exitCode != 0 ) 
	            {
	                // something went wrong
	                LoggerManager.getLogger().error("Alarmer Had a non zero exit status [" + exitCode + "]" +
	                		" for command [" + raiseAlarmProgram + " " + severity + " " + alarmNum + 
	                		" " + addInfo1 + " " + addInfo2 + "]");
	                returnCode = 1;
	            }
	        } 
	        catch (InterruptedException e) 
	        {
	        	LoggerManager.getLogger().error("Process failed not exit");
	            returnCode = 1;
	        }
	        catch (IOException e)
	        {
	        	LoggerManager.getLogger().error("IOException : " + e);
	            //e.printStackTrace();
	            returnCode = 1;
	        }
	    }
        else
        {
        	LoggerManager.getLogger().error("Cannot Execute " + raiseAlarmProgram);
        }
        return returnCode;
    }
    
}
