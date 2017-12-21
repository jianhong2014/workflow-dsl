/*
 * $Id: CaafSystemUtils.java 11518 2008-03-31 22:07:18Z tcarlson $
 * --------------------------------------------------------------------------------------
 */

package com.jm.wf.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.jm.wf.common.logging.LoggerManager;

// @ThreadSafe
public class JmSystemUtils extends org.apache.commons.lang.SystemUtils
{
    // bash prepends: declare -x
    // zsh prepends: typeset -x
	
    private static final String[] UNIX_ENV_PREFIXES = new String[]
    { "declare -", "typeset -" };

    // the environment of the VM process
    private static Map<String, String> environment = null;

    /**
     * Get the operating system environment variables. This should work for
     * Windows and Linux.
     * 
     * @return Map<String, String> or an empty map if there was an error.
     */
    @SuppressWarnings("unchecked")
    public static synchronized Map<String, String> getenv()
    {
        if (environment == null)
        {
            try
            {
                if (JmSystemUtils.IS_JAVA_1_4)
                {
                    // fallback to external process
                    environment = Collections.unmodifiableMap(getenvJDK14());
                }
                else
                {
                    // the following runaround is necessary since we still want
                    // to
                    // compile on JDK 1.4
                    Class<?> target = System.class;
                    Method envMethod = target.getMethod("getenv", JmArrayUtils.EMPTY_CLASS_ARRAY);
                    environment = Collections.unmodifiableMap((Map) envMethod.invoke((Object) target, (Object[]) null));
                }
            }
            catch (Exception ex)
            {
            	LoggerManager.getLogger().error("Could not access OS environment: ", ex);
                environment = Collections.EMPTY_MAP;
            }
        }

        return environment;
    }

    private static Map<String, String> getenvJDK14() throws Exception
    {
        Map<String, String> env = new HashMap<String, String>();
        Process process = null;

        try
        {
            boolean isUnix = true;
            String command;

            if (JmSystemUtils.IS_OS_WINDOWS)
            {
                command = "cmd /c set";
                isUnix = false;
            }
            else
            {
                command = "env";
            }

            process = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = br.readLine()) != null)
            {
                for (int prefix = 0; prefix < UNIX_ENV_PREFIXES.length; prefix++)
                {
                    if (line.startsWith(UNIX_ENV_PREFIXES[prefix]))
                    {
                        line = line.substring(UNIX_ENV_PREFIXES[prefix].length());
                    }
                }

                int index = -1;
                if ((index = line.indexOf('=')) > -1)
                {
                    String key = line.substring(0, index).trim();
                    String value = line.substring(index + 1).trim();
                    // remove quotes, if any
                    if (isUnix && value.length() > 1 && (value.startsWith("\"") || value.startsWith("'")))
                    {
                        value = value.substring(1, value.length() - 1);
                    }
                    env.put(key, value);
                }
                else
                {
                    env.put(line, JmStringUtils.EMPTY);
                }
            }
        }
        catch (Exception e)
        {
            throw e; // bubble up
        }
        finally
        {
            if (process != null)
            {
                process.destroy();
            }
        }

        return env;
    }

    public static String getenv(String name)
    {
        return (String) JmSystemUtils.getenv().get(name);
    }

    public static boolean isSunJDK()
    {
        return JmSystemUtils.JAVA_VM_VENDOR.toUpperCase().indexOf("SUN") != -1;
    }

    public static boolean isIbmJDK()
    {
        return JmSystemUtils.JAVA_VM_VENDOR.toUpperCase().indexOf("IBM") != -1;
    }

    /**
     * Returns a Map of all valid property definitions in
     * <code>-Dkey=value</code> format. <code>-Dkey</code> is interpreted as
     * <code>-Dkey=true</code>, everything else is ignored. Whitespace in
     * values is properly handled but needs to be quoted properly:
     * <code>-Dkey="some value"</code>.
     * 
     * @param input
     *            String with property definitionn
     * @return a {@link Map} of property String keys with their defined values
     *         (Strings). If no valid key-value pairs can be parsed, the map is
     *         empty.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parsePropertyDefinitions(String input)
    {
        if (JmStringUtils.isEmpty(input))
        {
            return Collections.EMPTY_MAP;
        }

        // the result map of property key/value pairs
        final Map<String, String> result = new HashMap<String, String>();

        // where to begin looking for key/value tokens
        int tokenStart = 0;

        // this is the main loop that scans for all tokens
        findtoken: while (tokenStart < input.length())
        {
            // find first definition or bail
            tokenStart = JmStringUtils.indexOf(input, "-D", tokenStart);
            if (tokenStart == JmStringUtils.INDEX_NOT_FOUND)
            {
                break findtoken;
            }
            else
            {
                // skip leading -D
                tokenStart += 2;
            }

            // find key
            int keyStart = tokenStart;
            int keyEnd = keyStart;

            if (keyStart == input.length())
            {
                // short input: '-D' only
                break;
            }

            // let's check out what we have next
            char cursor = input.charAt(keyStart);

            // '-D xxx'
            if (cursor == ' ')
            {
                continue findtoken;
            }

            // '-D='
            if (cursor == '=')
            {
                // skip over garbage to next potential definition
                tokenStart = JmStringUtils.indexOf(input, ' ', tokenStart);
                if (tokenStart != JmStringUtils.INDEX_NOT_FOUND)
                {
                    // '-D= ..' - continue with next token
                    continue findtoken;
                }
                else
                {
                    // '-D=' - get out of here
                    break findtoken;
                }
            }

            // apparently there's a key, so find the end
            findkey: while (keyEnd < input.length())
            {
                cursor = input.charAt(keyEnd);

                // '-Dkey ..'
                if (cursor == ' ')
                {
                    tokenStart = keyEnd;
                    break findkey;
                }

                // '-Dkey=..'
                if (cursor == '=')
                {
                    break findkey;
                }

                // keep looking
                keyEnd++;
            }

            // yay, finally a key
            String key = JmStringUtils.substring(input, keyStart, keyEnd);

            // assume that there is no value following
            int valueStart = keyEnd;
            int valueEnd = keyEnd;

            // default value
            String value = "true";

            // now find the value, but only if the current cursor is not a space
            if (keyEnd < input.length() && cursor != ' ')
            {
                // bump value start/end
                valueStart = keyEnd + 1;
                valueEnd = valueStart;

                // '-Dkey="..'
                cursor = input.charAt(valueStart);
                if (cursor == '"')
                {
                    // opening "
                    valueEnd = JmStringUtils.indexOf(input, '"', ++valueStart);
                }
                else
                {
                    // unquoted value
                    valueEnd = JmStringUtils.indexOf(input, ' ', valueStart);
                }

                // no '"' or ' ' delimiter found - use the rest of the string
                if (valueEnd == JmStringUtils.INDEX_NOT_FOUND)
                {
                    valueEnd = input.length();
                }

                // create value
                value = JmStringUtils.substring(input, valueStart, valueEnd);
            }

            // finally create key and value && loop again for next token
            result.put(key, value);

            // start next search at end of value
            tokenStart = valueEnd;
        }

        return result;
    }

}
