/**
 * 
 */
package me.finiteloop.planner.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.optaplanner.core.config.solver.SolverConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author kunal@finiteloop.me
 *
 */
public class SolverSchemaReader {

	private static final String packageName = SolverConfig.class.getPackage()
			.getName();

	private Object createEmptyNode(Object objectInstance)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Field[] fields = objectInstance.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Constructor<?>[] constructors = fields[i].getType()
					.getDeclaredConstructors();
			if (constructors.length > 0) {
				Constructor<?> defaultConstructor = null;
				for (int index = 0; index < constructors.length
						&& defaultConstructor == null; index++) {
					if (constructors[index].isAccessible()
							&& constructors[index].getParameterCount() == 0) {
						defaultConstructor = constructors[index];
					}
				}
				if (defaultConstructor != null) {
					fields[i] = (Field) defaultConstructor.newInstance();
					if (fields[i].getType().getPackage().getName()
							.contains(packageName)) {
						fields[i] = (Field) createEmptyNode(fields[i]);
					}
				}
			}
		}
		return objectInstance;
	}

	private void populateEmptySolverConfig() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.solverConfig = (SolverConfig) createEmptyNode(new SolverConfig());

	}

	public SolverSchemaReader() throws InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		populateEmptySolverConfig();

		//System.out.println(xstream.toXML(this.solverConfig));
	}

	public String toXML() {
		StringBuffer strBuffer = new StringBuffer();

		return strBuffer.toString();
	}

	public String toJSON(Object instance) {
		StringBuffer strBuffer = new StringBuffer();
		
		GsonBuilder builder = new GsonBuilder();
		builder.serializeNulls();
        Gson gson = builder.create();
        
        System.out.println(gson.toJson(instance));
		return strBuffer.toString();
	}

	public static void main(String args[]) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		SolverSchemaReader reader = new SolverSchemaReader();
		
		System.out.println(reader.toJSON(new SolverConfig()));
	}
}
