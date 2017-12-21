package com.jm.wf.events.expressions;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jm.wf.common.datavalues.DataValue;
import com.jm.wf.common.datavalues.DataValueNull;
import com.jm.wf.common.logging.LoggerManager;
import com.jm.wf.events.expressions.parsers.FieldValueExpressionParser;
import com.jm.wf.events.filters.FilterTemplate;

public class FieldValueExpression
{
	private List<ExpressionBase> expressions = null;
	
	private Map<String, DataValue> localVariables = new HashMap<String,DataValue>();
    
    /**
     * <h1>Field Value Expressions</h1>
     * <h2>Operator Precedence</h2>
     * <table cellpadding="5">
     * <thead>
     * <tr><td align="center"><b>Operator</b></td><td><b>Description</b></td><td align="center"><b>Level</b></td><td><b>Associativity</b></td></tr>
     * </thead>
     * <tbody>
     * <tr><td align="center">()                       </td><td>Invoke a method     </td><td align="center">1 </td><td align="center">left to right</td></tr>
     * <tr><td align="center">+                        </td><td>Unary plus          </td><td align="center">2 </td><td align="center">right to left</td></tr>
     * <tr><td align="center">-                        </td><td>Unary minus         </td><td align="center">2 </td><td align="center">right to left</td></tr>
     * <tr><td align="center">~                        </td><td>Bitwise NOT         </td><td align="center">2 </td><td align="center">right to left</td></tr>
     * <tr><td align="center">!                        </td><td>Boolean NOT         </td><td align="center">2 </td><td align="center">right to left</td></tr>
     * <tr><td align="center">( type )                 </td><td>Type Cast           </td><td align="center">3 </td><td align="center">right to left</td></tr>
     * <tr><td align="center">*<br/>/<br/>%            </td><td>Multiplicative      </td><td align="center">4 </td><td align="center">left to right</td></tr>
     * <tr><td align="center">+ -<br/>+                </td><td>Additive<br/>
     *                                                          String Concatenation</td><td align="center">5 </td><td align="center">left to right</td></tr>
     * <tr><td align="center">&lt;&lt;<br/>&gt;&gt;    </td><td>Shift               </td><td align="center">6 </td><td align="center">left to right</td></tr>
     * <tr><td align="center">&lt; &lt;=<br/>&gt; &gt;=</td><td>Relational          </td><td align="center">7 </td><td align="center">left to right</td></tr>
     * <tr><td align="center">==<br/>!=                </td><td>Equality            </td><td align="center">8 </td><td align="center">left to right</td></tr>
     * <tr><td align="center">&                        </td><td>Bitwise AND         </td><td align="center">9 </td><td align="center">left to right</td></tr>
     * <tr><td align="center">^                        </td><td>Bitwise XOR         </td><td align="center">10</td><td align="center">left to right</td></tr>
     * <tr><td align="center">|                        </td><td>Bitwise OR          </td><td align="center">11</td><td align="center">left to right</td></tr>
     * <tr><td align="center">&&                       </td><td>Boolean AND         </td><td align="center">12</td><td align="center">left to right</td></tr>
     * <tr><td align="center">||                       </td><td>Boolean OR          </td><td align="center">13</td><td align="center">left to right</td></tr>
     * <tr><td align="center">?:                       </td><td>Conditional         </td><td align="center">14</td><td align="center">right to left</td></tr>
     * <tr><td align="center">=<br/>+= -=<br/>
     *                        *= /= %=<br/>
     *                        &amp;= |= ^=<br/>
     *                        &lt;&lt;= &gt;&gt;=      </td><td>Assignment          </td><td align="center">15</td><td align="center">right to left</td></tr>
     * </tbody>
     * </table>
     *
     * <h2>Grammar Production Rules</h2>
     * <table cellpadding="5">
     * <tr><td>expressions  </td><td>::=</td><td>expression   (';' expression)*</td></tr>
     * <tr><td>expression   </td><td>::=</td><td>(VAR_NAME assign-op)* cond-term</td></tr>
     * <tr><td>cond-term    </td><td>::=</td><td>log-or-term  (cond-true-op log-or-term cond-false-op log-or-term)?</td></tr>
     * <tr><td>log-or-term  </td><td>::=</td><td>log-and-term (log-or-op  log-and-term)?</td></tr>
     * <tr><td>log-and-term </td><td>::=</td><td>bit-or-term  (log-and-op bit-or-term)?</td></tr>
     * <tr><td>bit-or-term  </td><td>::=</td><td>bit-xor-term (bit-or-op  bit-xor-term)*</td></tr>
     * <tr><td>bit-xor-term </td><td>::=</td><td>bit-and-term (bit-xor-op bit-and-term)*</td></tr>
     * <tr><td>bit-and-term </td><td>::=</td><td>log-eq-term  (bit-and-op log-eq-term)*</td></tr>
     * <tr><td>log-eq-term  </td><td>::=</td><td>log-rel-term (log-eq-op  log-rel-term)?</td></tr>
     * <tr><td>log-rel-term </td><td>::=</td><td>shift-term   (log-rel-op shift-term)?</td></tr>
     * <tr><td>shift-term   </td><td>::=</td><td>add-term     (shift-op   add-term)*</td></tr>
     * <tr><td>add-term     </td><td>::=</td><td>mul-term     (add-op     mul-term)*</td></tr>
     * <tr><td>mul-term     </td><td>::=</td><td>unary-term  ( mul-op     unary-term)*</td></tr>
     * <tr><td>unary-term   </td><td>::=</td><td>(unary-op)?  method-term</td></tr>
     * <tr><td>method-term  </td><td>::=</td><td>factor | method-call</td></tr>
     * <tr><td>factor       </td><td>::=</td><td>NUMBER | QSTRING | VAR_NAME |
     *                                           FilterTemplate | '(' expression ')'</td></tr>
     * <tr><td>method-call  </td><td>::=</td><td>MTHD_NAME ('(' (param-list)? ')')?</td></tr>
     * <tr><td>param-list   </td><td>::=</td><td>param (',' param)*</td></tr>
     * <tr><td>param        </td><td>::=</td><td>expression</td></tr>
     * <tr><td>assign-op    </td><td>::=</td><td>'='  | '+=' | '-=' | '*='  | '/=' | '%=' |
     *                                           '&=' | '|=' | '^=' | '<<=' | '>>='</td></tr>
     * <tr><td>cond-true-op </td><td>::=</td><td>'?'</td></tr>
     * <tr><td>cond-false-op</td><td>::=</td><td>':'</td></tr>
     * <tr><td>log-or-op    </td><td>::=</td><td>'||'</td></tr>
     * <tr><td>log-and-op   </td><td>::=</td><td>'&&'</td></tr>
     * <tr><td>bit-or-op    </td><td>::=</td><td>'|'</td></tr>
     * <tr><td>bit-xor-op   </td><td>::=</td><td>'^'</td></tr>
     * <tr><td>bit-and-op   </td><td>::=</td><td>'&'</td></tr>
     * <tr><td>log-eq-op    </td><td>::=</td><td>'==' | '!='</td></tr>
     * <tr><td>log-rel-op   </td><td>::=</td><td>'<' | '<=' | '>' | '>='</td></tr>
     * <tr><td>shift-op     </td><td>::=</td><td>'<<' | '>>'</td></tr>
     * <tr><td>add-op       </td><td>::=</td><td>'+' | '-'</td></tr>
     * <tr><td>mul-op       </td><td>::=</td><td>'*' | '/' | '%'</td></tr>
     * <tr><td>unary-op     </td><td>::=</td><td>'+' | '-' | '~' | '!'</td></tr>
     * <tr><td>VAR_NAME     </td><td>::=</td><td>'$' IDENTIFIER</td></tr>
     * <tr><td>MTHD_NAME    </td><td>::=</td><td>'@' IDENTIFIER</td></tr>
     * </table>
     */
    public static FieldValueExpression create(Class<? extends FilterTemplate> filterClass,
                                              String                          expressionText)
    throws InvalidExpressionFormatException
    {
        FieldValueExpression fieldValueExpression = null;

        // Use the FieldValueExpressionParser to create the FieldValueExpression object.
        
        try
        {
            StringReader strReader = new StringReader(expressionText);
            Reader       reader    = new BufferedReader(strReader);
            
            FieldValueExpressionParser parser = new FieldValueExpressionParser(filterClass,
            																   LoggerManager.getLogger(),
            		                                                           reader);
        
            parser.ReInit(reader);
            
          	fieldValueExpression = parser.FieldValueExpression();
        }
        catch (Exception e)
        {
            throw new InvalidExpressionFormatException(e);
        }
        
        return fieldValueExpression;
    }
    
    public FieldValueExpression()
    {
    }

    public void setLocalVariable(String    symbol,
                                 DataValue value)
    {
    	localVariables.put(symbol, value);
    }
    
    public DataValue getLocalVariable(String symbol)
    {
    	DataValue value = localVariables.get(symbol);
    	
    	if (value == null)
    	{
    	    try
    	    {
    	        value = new DataValueNull();
    	    }
    	    catch (Exception e) {}
    	}
    	
    	return value;
    }
    
    /**
     * The result of evaluating a field value expression is the result of the last
     * expression in the expression list.
     */
    public DataValue evaluate(Object object)
    {
        DataValue value = null;
        
        localVariables.clear();
        
        for (ExpressionBase expression : expressions)
        {
        	value = expression.evaluate(object);
        }
        
        return value;
    }

	public List<ExpressionBase> getExpressions()
	{
		return expressions;
	}

	public void setExpressions(List<ExpressionBase> expressions)
	{
		this.expressions = expressions;
	}
	
	public String toString()
	{
	    StringBuilder str = new StringBuilder();
	    
	    boolean first = true;
	    
	    for (ExpressionBase expression : expressions)
	    {
	        if (!first)
	        {
	            str.append("; ");
	        }
	        
	        str.append(expression);
	        
	        first = false;
	    }
	    
	    return str.toString();
	}
}
