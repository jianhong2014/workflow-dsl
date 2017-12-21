package com.jm.wf.events.expressions.methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jm.wf.events.expressions.ExpressionBase;

public abstract class MethodBase extends ExpressionBase
{
    private String methodName = "";
    
    private List<ExpressionBase> parameters = new ArrayList<ExpressionBase>();
    
    private static Map<String, Class<?>> mthdMapping = new HashMap<String, Class<?>>();
    
    private static boolean initialised = false;
    
    
    public static MethodBase create(String mthdStr)
    {
        MethodBase method = null;
    
        if (!initialised)
        {
            initialise();
        }
        
        Class<?> mthdClass = mthdMapping.get(mthdStr.toLowerCase());
        
        if (mthdClass != null)
        {
            try
            {
                method = (MethodBase) mthdClass.newInstance();
                
                method.methodName = mthdStr.toLowerCase();
            }
            catch (Exception e)
            {
                method = null;
            }
        }
        
        return method;
    }
    
    private static void initialise()
    {
        mthdMapping.put("avg",           MethodAvg.class);
        mthdMapping.put("boolean",       MethodBoolean.class);
        mthdMapping.put("contains",      MethodContains.class);
        mthdMapping.put("date",          MethodDate.class);
        mthdMapping.put("firstvalue",    MethodFirstValue.class);
        mthdMapping.put("foreach",       MethodForEach.class);
        mthdMapping.put("get",           MethodGet.class);
        mthdMapping.put("hex",           MethodHex.class);
        mthdMapping.put("integer",       MethodInteger.class);
        mthdMapping.put("join",          MethodJoin.class);
        mthdMapping.put("length",        MethodLength.class);
        mthdMapping.put("list",          MethodList.class);
        mthdMapping.put("map",           MethodMap.class);
        mthdMapping.put("matchesall",    MethodMatchesAll.class);
        mthdMapping.put("matchesany",    MethodMatchesAny.class);
        mthdMapping.put("max",           MethodMax.class);
        mthdMapping.put("min",           MethodMin.class);
        mthdMapping.put("null",          MethodNull.class);
        mthdMapping.put("output",        MethodOutput.class);
        mthdMapping.put("real",          MethodReal.class);
        mthdMapping.put("replace",       MethodReplace.class);
        mthdMapping.put("replaceall",    MethodReplaceAll.class);
        mthdMapping.put("set",           MethodSet.class);
        mthdMapping.put("setmerge",      MethodSetMerge.class);
        mthdMapping.put("size",          MethodSize.class);
        mthdMapping.put("split",         MethodSplit.class);
        mthdMapping.put("string",        MethodString.class);
        mthdMapping.put("substr",		 MethodSubstr.class);
        mthdMapping.put("sum",           MethodSum.class);
        mthdMapping.put("tolower",       MethodToLower.class);
        mthdMapping.put("toupper",       MethodToUpper.class);
        mthdMapping.put("truncinterval", MethodTruncInterval.class);
        mthdMapping.put("intsplit",      MethodIntSplit.class);
        mthdMapping.put("momatch",       MethodMoMatch.class);
        mthdMapping.put("concat",        MethodConcat.class);
        mthdMapping.put("collsize",      MethodCollSize.class);
        mthdMapping.put("isnull",        MethodIsNull.class);
        mthdMapping.put("lth",           MethodLargeThan.class);
    }
    
    public void addParameter(ExpressionBase parameter)
    {
        parameters.add(parameter);
    }
    
    public List<ExpressionBase> getParameters()
    {
        return parameters;
    }
    
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        
        str.append("@").append(methodName).append("(");
        
        boolean first = true;
        
        for (ExpressionBase parameter : parameters)
        {
            if (!first)
            {
                str.append(", ");
            }
            
            str.append(parameter);
            
            first = false;
        }
        
        str.append(")");
        
        return str.toString();
    }
    
}
