/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
package coins.aflow.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>Title: SelfCollectingResults</p>
 * <p>Description: Map class that responds to the query ("<code>get</code>") by, if the requested key is not found, calling methods that collects the info (value for the key). This class extends HashMap,
 * so all the methods of HashMap are available. The keys of this map are the
 * the name of the information, and optionally (but almost always) other information that seems
 * convenient if the user has the direct access (rather than through the table
 * that the value of the map could have otherwise hold). The set of keys
 * present in the instance of this map can be thought of as the current level
 * of progress of analysis this map represents. </p>
 *
 * @author unascribed
 * @version 1.0
 */
public class SelfCollectingResults extends HashMap
{
	private final static String DEFAULT_METHOD_NAME = "find";
	protected static RegisterAnalClasses regClasses;
//	private final static ListNode ROOT = new ListNode();
	
	/**
	 * Map that obtains the analyzer class from the analysis name. This map doesn't have to be injective.
	 */
	private final CallMap callMap = new CallMap();
//	protected ListNode fCurrentAnal = ROOT;
	public Poset fAnalDependenceGraph = new PosetImpl();
	public Map fComrades = new HashMap();
	
	public SelfCollectingResults()
	{
		if (regClasses == null)
		{
			throw new FlowError(
			"RegisterAnalClasses not found. Call this class's static method putRegClasses before instatiating.");
		}
		
		regClasses.register(this);
	}
	
	/**
	 * Specifies which set of analyzer classes to use for the automatic analysis mechanism.
	 */
	public static void putRegClasses(RegisterAnalClasses pRegClasses)
	{
		regClasses = pRegClasses;
	}
	
	public void putAnalyzer(String pAnal, Class pAnalClass)
	{
		callMap.putUnspecified(pAnal, pAnalClass, DEFAULT_METHOD_NAME);
	}
	
	public void putAnalyzer(String pAnal, Class pAnalClass, String pMethodName)
	{
		callMap.putUnspecified(pAnal, pAnalClass, pMethodName);
	}
	
	/**
	 * Obtains the information specified by the argument <code>pAnal</code> from the <code>pResults</code> argument. If <code>pResults</code> does not contain the requested information, analyses to obtain the requested information will be performed, and the result eventually returned.
	 *
	 * @param pAnal name of the information to retrieve.
	 * @param pResults results of the flow analyses performed so far from which to retrieve the requested information.
	 * @return the requested information.
	 */
	public Object get(String pAnal)
	{
		return getFromList(list(pAnal));
	}
	
	/**
	 * Obtains the information specified by the argument <code>pAnal</code> and <code>pObj</code> from the <code>pResults</code> argument. If <code>pResults</code> does not contain the requested information, analyses to obtain the requested information will be performed, and the result eventually returned.
	 *
	 * @param pAnal name of the information to retrieve, which serves as a key for the requested information.
	 * @param pObj object that serves as another key for the requested information.
	 * @param pResults results of the flow analyses performed so far from which to retrieve the requested information.
	 * @return the requested information.
	 */
	public Object get(String pAnal, Object pObj)
	{
		return getFromList(list(pAnal, pObj));
	}
	
	public Object get(String pAnal, Object pObj, Object pObj0)
	{
		//		return getFromList(list(pAnal, pObj, pObj0));
		return getFromList0(list(pAnal, pObj, pObj0));
	}
	
	private Object getFromList(List pAnal)
	{
		if (containsListKey(pAnal))
		{
			return getRawFromList(pAnal);
		}
		
		findFromList(pAnal);
		
		return getRawFromList(pAnal);
	}
	
	private Object getFromList0(List pAnal)
	{
//		ListNode ln = new ListNode();
//		ln.addAll(pAnal);
//		fCurrentAnal.linkWithChild(ln);
		
//		if (fComrades.get(ln) == null)
//		{
//			fComrades.put(ln, new HashSet());
//		}
		
//		((Set) fComrades.get(ln)).add(pAnal);
		
		if (containsListKey(pAnal))
		{
			return getRawFromList(pAnal);
		}
		
		findFromList(pAnal);
		
		return getRawFromList(pAnal);
	}
	
	public void find(String pAnal)
	{
		find(pAnal, new Object[]
		{  });
	}
	
	public void find(String pAnal, Object pObj)
	{
		find(pAnal, new Object[]
		{ pObj });
	}
	
	public void find(String pAnal, Object pObj, Object pObj0)
	{
		find(pAnal, new Object[]
		{ pObj, pObj0 });
	}
	
	private void find(String pAnal, Object[] pArgs)
	{
		int lLength = pArgs.length;
		Object[] lMethodArgs = new Object[lLength];
		Class[] lMethodArgTypes = new Class[lLength];
		
		Method lAnalMethod;
		Class lAnalClass;
		Constructor lConstr;
		String lMethodName;
		Object[] lPair;
		Analyzer lAnalyzer;
		
		lPair = (Object[]) callMap.get(pAnal);
		
		if (lPair == null)
		{
			throw new IllegalArgumentException(
			"Analyzer class corresponding to \"" + pAnal +
			"\" not registered.");
		}
		
		lConstr = (Constructor) lPair[0];
		lAnalClass = lConstr.getDeclaringClass();
		System.arraycopy(pArgs, 0, lMethodArgs, 0, lLength);
		
		if (lPair[1] instanceof Method)
		{
			lAnalMethod = (Method) lPair[1];
		} else
		{
			for (int i = 0; i < lLength; i++)
			{
				try
				{
					lMethodArgTypes[i] = lMethodArgs[i].getClass();
				} catch (NullPointerException e)
				{
					throw new FlowError(
					"Null argument not accepted for the methods of " +
					lAnalClass);
				}
			}
			
			lMethodName = (String) lPair[1];
			lAnalMethod = getMethod(lAnalClass, lMethodName, lMethodArgTypes);
			
			if (lAnalMethod == null)
			{
				throw new FlowError("The specified method " +
				lAnalClass.getName() + "#" + lMethodName +
				" could not be located.");
			}
		}
		
		try
		{
			lAnalyzer = (Analyzer) lConstr.newInstance(new Object[]
			{ this });
		} catch (Exception e)
		{
			if (e instanceof IllegalArgumentException)
			{
				try
				{
					lAnalyzer = (Analyzer) lConstr.newInstance(new Object[]
					{  });
				} catch (Exception e0)
				{
					if (e0 instanceof ClassCastException)
					{
						throw new ClassCastException(
						"Analyzer class not implementing Analyzer?");
					} else if (e0 instanceof InstantiationException)
					{
						throw new FlowError("Analyzer class " + lAnalClass +
						" abstract.");
					} else if (e0 instanceof InvocationTargetException)
					{
						e0.printStackTrace();
						throw new FlowError(
						"Analyzer class constructor threw an exception.");
					} else
					{
						throw new FlowError(
						"Analyzer class construction failed.");
					}
				}
			} else if (e instanceof ClassCastException)
			{
				throw new ClassCastException(
				"Analyzer class not implementing Analyzer?");
			} else if (e instanceof InstantiationException)
			{
				throw new FlowError("Analyzer class " + lAnalClass +
				" abstract.");
			} else if (e instanceof InvocationTargetException)
			{
				e.printStackTrace();
				throw new FlowError(
				"Analyzer class constructor threw an exception.");
			} else
			{
				throw new FlowError("Analyzer class construction failed.");
			}
		}
		
//		ListNode lCurrentAnalSave = fCurrentAnal;
//		ListNode ln = new ListNode();
//		ln.add(pAnal);
//		ln.addAll(Arrays.asList(pArgs));
		//        List l = list(pAnal);
		//        l.addAll(Arrays.asList(pArgs));
		//        ln.addAll(l);
//		fCurrentAnal = ln;
		
		//                        if (lCurrentAnalSave != null)
//		lCurrentAnalSave.linkWithChild(fCurrentAnal);
		
		//                        System.out.println("Analysis Dependence : ");
		//                        System.out.println(fAnalDependenceGraph);
		try
		{
			lAnalMethod.invoke(lAnalyzer, lMethodArgs);
		} catch (Exception e)
		{
			if (e instanceof InvocationTargetException)
			{
				e.printStackTrace();
				throw new FlowError("Analysis " + lAnalyzer + " failed.");
			} else
			{
				throw new FlowError("Analyzer method invocation failed.");
			}
		}
		
//		for (Iterator lIt = fCurrentAnal.fChildren.iterator(); lIt.hasNext();)
//		{
//			ListNode lChildAnal = (ListNode) lIt.next();
			
//			buildDependence(lChildAnal, fCurrentAnal);
//			fComrades.remove(lChildAnal);
//		}
		
//		fCurrentAnal = lCurrentAnalSave;
		
		//                        System.out.println("Analysis Dependence : ");
		//                        System.out.println(fAnalDependenceGraph);
	}
	
	private void findFromList(List pAnal)
	{
		find((String) pAnal.get(0), pAnal.subList(1, pAnal.size()).toArray());
	}
	
	private static Method getMethod(Class pClass, String pMethodName,
	Class[] pTypes)
	{
		Method[] lCandidates = pClass.getMethods();
		Method lChamp = null;
		Class[] lCandParamTypes;
		Class[] lChampParamTypes = null;
		boolean lPassed;
		int i;
		int j;
		
		for (i = 0; i < lCandidates.length; i++)
		{
			if (!pMethodName.equals(lCandidates[i].getName()))
			{
				continue;
			}
			
			lCandParamTypes = lCandidates[i].getParameterTypes();
			
			if (lCandParamTypes.length != pTypes.length)
			{
				continue;
			}
			
			for (j = 0, lPassed = true; j < lCandParamTypes.length; j++)
			{
				if (!(lCandParamTypes[j].isAssignableFrom(pTypes[j])))
				{
					lPassed = false;
					
					break;
				} else if ((lChamp != null) &&
				!(lChampParamTypes[j].isAssignableFrom(
				lCandParamTypes[j])))
				{
					lPassed = false;
					
					break;
				}
			}
			
			if (lPassed)
			{
				lChamp = lCandidates[i];
				lChampParamTypes = lChamp.getParameterTypes();
			}
		}
		
		return lChamp;
	}
	
	private static Class getClassForName(String pClassName)
	{
		Class lClass;
		
		try
		{
			lClass = Class.forName(pClassName);
		} catch (ClassNotFoundException e)
		{
			throw new FlowError("Class for " + pClassName + " not found.");
		}
		
		return lClass;
	}
	
	private Constructor getConstructor(Class pClass)
	{
		Constructor[] lConstrs = pClass.getConstructors();
		int lLength = lConstrs.length;
		Constructor lConstr;
		Class[] lParamTypes;
		int lParamCount;
		Constructor lDefaultConstr = null;
		
		if (lLength > 2)
		{
			throw new FlowError("Too many public constructors."); // Should be a warning.
		}
		
		for (int i = 0; i < lLength; i++)
		{
			lConstr = lConstrs[i];
			lParamTypes = lConstr.getParameterTypes();
			
			if (((lParamCount = lParamTypes.length) == 1) &&
			getClass().isAssignableFrom(lParamTypes[0]))
			{
				return lConstr;
			} else if (lParamCount == 0)
			{
				lDefaultConstr = lConstr;
			}
		}
		
		if (lDefaultConstr == null)
		{
			throw new FlowError(
			"Qualifying public constructor not found. It has to be with a single argument that is a subclass of " +
			SelfCollectingResults.this.getClass() +
			" or with no argument.");
		}
		
		return lDefaultConstr;
	}
	
	/**
	 * Registers the result of a flow analysis to this map.
	 * See put(String, HashMap, Object);
	 *
	 * @param pAnal analysis name.
	 * @param pVal result of the analysis.
	 */
	public Object put(String pAnal, Object pVal)
	{
		List lKey = list(pAnal);
		
		return putListKey(lKey, pVal);
	}
	
	/**
	 * Registers the result of a flow analysis for the specified object to this map.
	 * See put(String, Object, HashMap, Object).
	 *
	 * @param pAnal analysis name.
	 * @param pObj object of analysis.
	 * @param pVal result of the analysis.
	 */
	public Object put(String pAnal, Object pObj, Object pVal)
	{
		return putListKey(list(pAnal, pObj), pVal);
	}
	
	public Object put(String pAnal, Object pObj, Object pObj0, Object pVal)
	{
		return putListKey(list(pAnal, pObj, pObj0), pVal);
	}
	
	private Object putListKey(List pAnal, Object pObj)
	{
		
		return put(pAnal, pObj);
	}
	
	public Object getRaw(String pAnal)
	{
		return getRawFromList(list(pAnal));
	}
	
	public Object getRaw(String pAnal, Object pObj)
	{
		return getRawFromList(list(pAnal, pObj));
	}
	
	public Object getRaw(String pAnal, Object pObj, Object pObj0)
	{
		return getRawFromList(list(pAnal, pObj, pObj0));
	}
	
	private Object getRawFromList(List pAnal)
	{
		return super.get(pAnal);
	}
	
	public boolean containsKey(String pAnal)
	{
		return containsListKey(list(pAnal));
	}
	
	public boolean containsKey(String pAnal, Object pObj)
	{
		return containsListKey(list(pAnal, pObj));
	}
	
	public boolean containsKey(String pAnal, Object pObj, Object pObj0)
	{
		return containsListKey(list(pAnal, pObj, pObj0));
	}
	
	private boolean containsListKey(List pAnal)
	{
		return containsKey(pAnal);
	}
	
//	public void invalidate(List pAnal)
//	{
//		if (!containsKey(pAnal))
//		{
//			System.out.println("Cannot invalidate nonexistent analysis results for " + pAnal);
//			return;
//		}
//		Set invalids = fAnalDependenceGraph.greatersOf(pAnal);
//		
//		for (Iterator lIt = invalids.iterator(); lIt.hasNext();)
//		{
//			remove(lIt.next());
//		}
//	}
	
	public static List list(Object pObj)
	{
		List lList = new ArrayList();
		lList.add(pObj);
		
		return lList;
	}
	
	public static List list(Object pObj, Object pObj0)
	{
		List lList = new ArrayList();
		lList.add(pObj);
		lList.add(pObj0);
		
		return lList;
	}
	
	public static List list(Object pObj, Object pObj0, Object pObj1)
	{
		List lList = new ArrayList();
		lList.add(pObj);
		lList.add(pObj0);
		lList.add(pObj1);
		
		return lList;
	}
//	
//	private void buildDependence(ListNode pPrerequisite, ListNode pDependent)
//	{
//		Set lPrerequisites = (Set) fComrades.get(pPrerequisite);
//		Set lDependents = (Set) fComrades.get(pDependent);
//		List lPrerequisiteAnal;
//		List lDependentAnal;
//		
//		for (Iterator lIt = lPrerequisites.iterator(); lIt.hasNext();)
//		{
//			lPrerequisiteAnal = (List) lIt.next();
//			
//			for (Iterator lIt0 = lDependents.iterator(); lIt0.hasNext();)
//			{
//				lDependentAnal = (List) lIt0.next();
//				
//				if (!lDependents.contains(lPrerequisiteAnal) &&
//				!(lPrerequisites.contains(lDependentAnal)))
//				{
//					fAnalDependenceGraph.connect(lPrerequisiteAnal,
//					lDependentAnal);
//				}
//			}
//		}
//	}
	
	/**
	 * Map between the analysis name and the class responsible for the analysis.
	 */
	private class CallMap extends HashMap
	{
		private void putSpecified(String pAnal, Class pAnalClass,
		String pMethodName, Class[] pArgTypes)
		{
			try
			{
				putPair(pAnal, getConstructor(pAnalClass),
				pAnalClass.getMethod(pMethodName, pArgTypes));
			} catch (Exception e)
			{
				throw new FlowError("Analyzer method not found.");
			}
		}
		
		private void putUnspecified(String pAnal, Class pAnalClass,
		String pAnalMethod)
		{
			putPair(pAnal, getConstructor(pAnalClass), pAnalMethod);
		}
		
		private void putPair(String pAnal, Constructor pConstr, Object pObj)
		{
			Object[] lPair = new Object[]
			{ pConstr, pObj };
			
			if (super.containsKey(pAnal))
			{
				throw new IllegalArgumentException("Duplicate analysis name.");
			}
			
			this.put(pAnal, lPair);
		}
		
		//                private void putAnalObj(String pAnal, Analyzer pAnalyzer) {
		//                }
	}
	
//	private static class ListNode extends ArrayList
//	{
//		ListNode fParent;
//		Set fChildren = new HashSet();
//		
//		void linkWithChild(ListNode pChild)
//		{
//			fChildren.add(pChild);
//			pChild.fParent = this;
//		}
//		
//		public String toString()
//		{
//			if (fParent == null)
//			{
//				return "ROOT";
//			} else
//			{
//				return super.toString();
//			}
//		}
//	}
}
