package me.killstorm103.Rebug.Utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

import me.killstorm103.Rebug.RebugsAntiCheatSwitcherPlugin;

public class ReflectionUtils 
{
	public static boolean classExists(@NotNull String path) 
	{
        try {
            Class.forName(path);
            return true;
        }
        catch (Throwable e) 
        {
            return false;
        }
    }

	public static Class<?> getClass (String name) 
	{
		try
		{
			return Class.forName(name);
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

    public static boolean methodExists(@NotNull Class<?> clazz, @NotNull String method, Class<?> ... parameterTypes) {
        try {
            clazz.getMethod(method, parameterTypes);
            return true;
        }
        catch (NoSuchMethodException e) {
            return false;
        }
    }

    @NotNull
    public static List<Field> getFields(@NotNull Class<?> clazz, @NotNull Class<?> type) {
        ArrayList<Field> list = new ArrayList<Field>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() != type) continue;
            list.add(ReflectionUtils.setAccessible(field));
        }
        return list;
    }

    @NotNull
    public static List<Method> getMethods(@NotNull Class<?> clazz, @NotNull Class<?> returnType, Class<?> ... parameterTypes) {
        ArrayList<Method> list = new ArrayList<Method>();
        for (Method m : clazz.getMethods()) {
            if (!returnType.isAssignableFrom(m.getReturnType()) || m.getParameterCount() != parameterTypes.length) continue;
            Class<?>[] types = m.getParameterTypes();
            boolean valid = true;
            for (int i = 0; i < types.length; ++i) {
                if (types[i] == parameterTypes[i]) continue;
                valid = false;
                break;
            }
            if (!valid) continue;
            list.add(m);
        }
        return list;
    }

    @NotNull
    public static Method getMethod(@NotNull Class<?> clazz, @NotNull String[] names, Class<?> ... parameterTypes) throws NoSuchMethodException 
    {
        ArrayList<String> list = new ArrayList<String>();
        for (Method m : clazz.getMethods()) {
            if (m.getParameterCount() != parameterTypes.length) continue;
            Class<?>[] types = m.getParameterTypes();
            boolean valid = true;
            for (int i = 0; i < types.length; ++i) {
                if (types[i] == parameterTypes[i]) continue;
                valid = false;
                break;
            }
            if (!valid) continue;
            for (String name : names)
            {
                if (m.getName().equals(name)) {
                    return m;
                }
                String[] array = m.getName().split("_");
                if (array.length <= 2 || !array[2].equals(name)) continue;

                return m;
            }
            list.add(m.getName());
        }
        throw new NoSuchMethodException("No method found with possible names " + Arrays.toString(names) + " with parameters " + Arrays.toString(parameterTypes) + " in class " + clazz.getName() + ". Methods with matching parameters: " + list);
    }
    public static Method getMethod(boolean Throw, @NotNull Class<?> clazz, @NotNull String[] names, boolean setAccessible, Class<?> ... parameterTypes) throws NoSuchMethodException 
    {
        ArrayList<String> list = new ArrayList<String>();
        for (Method m : clazz.getMethods()) {
            if (m.getParameterCount() != parameterTypes.length) continue;
            Class<?>[] types = m.getParameterTypes();
            boolean valid = true;
            for (int i = 0; i < types.length; ++i) {
                if (types[i] == parameterTypes[i]) continue;
                valid = false;
                break;
            }
            if (!valid) continue;
            for (String name : names)
            {
                if (m.getName().equals(name)) {
                    return m;
                }
                String[] array = m.getName().split("_");
                if (array.length <= 2 || !array[2].equals(name)) continue;

                m.setAccessible(setAccessible);
                return m;
            }
            list.add(m.getName());
        }
        if (Throw)
        	throw new NoSuchMethodException("No method found with possible names " + Arrays.toString(names) + " with parameters " + Arrays.toString(parameterTypes) + " in class " + clazz.getName() + ". Methods with matching parameters: " + list);
        else
        	return null;
    }

    @NotNull
    public static List<Field> getInstanceFields(@NotNull Class<?> clazz, @NotNull Class<?> fieldType) {
        ArrayList<Field> list = new ArrayList<Field>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() != fieldType || Modifier.isStatic(field.getModifiers())) continue;
            list.add(ReflectionUtils.setAccessible(field));
        }
        return list;
    }

    @NotNull
    public static <T extends AccessibleObject> T setAccessible(@NotNull T o) {
        o.setAccessible(true);
        return o;
    }

    @NotNull
    public static Constructor<?> getOnlyConstructor(@NotNull Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length != 1) {
            throw new IllegalStateException("Class " + clazz.getName() + " is expected to have 1 constructor, but has " + constructors.length + ": \n" + Arrays.stream(constructors).map(Constructor::toString).collect(Collectors.joining("\n")));
        }
        return constructors[0];
    }

    @NotNull
    public static Field getOnlyField(@NotNull Class<?> clazz, @NotNull Class<?> type) {
        ArrayList<Field> list = new ArrayList<Field>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() != type) continue;
            list.add(ReflectionUtils.setAccessible(field));
        }
        if (list.size() != 1) {
            throw new IllegalStateException("Class " + clazz.getName() + " is expected to have 1 field of type " + type.getName() + ", but has " + list.size() + ": " + list.stream().map(Field::getName).collect(Collectors.toList()));
        }
        return (Field)list.get(0);
    }

    @NotNull
    public static Method getOnlyMethod(@NotNull Class<?> clazz, @NotNull Class<?> returnType, Class<?> ... parameterTypes) {
        List<Method> list = ReflectionUtils.getMethods(clazz, returnType, parameterTypes);
        if (list.size() != 1) {
            throw new IllegalStateException("Class " + clazz.getName() + " is expected to have 1 method with return type " + returnType.getName() + " and parameters " + Arrays.toString(parameterTypes) + ", but has " + list.size() + ": \n" + list.stream().map(Method::toString).collect(Collectors.joining("\n")));
        }
        return list.get(0);
    }

    @NotNull
    public static Field getOnlyField (@NotNull Class<?> clazz) 
    {
        Field[] fields = (Field[])Arrays.stream(clazz.getDeclaredFields()).filter(f -> !Modifier.isStatic(f.getModifiers())).toArray(Field[]::new);
        if (fields.length != 1) 
        {
            throw new IllegalStateException("Class " + clazz.getName() + " is expected to have 1 field, but has " + fields.length + ": " + Arrays.stream(fields).map(Field::getName).collect(Collectors.toList()));
        }
        return ReflectionUtils.setAccessible(fields[0]);
    }
    public static boolean hasMethod (String type, Class<?> clazz, String methodName, Class<?>... parameterTypes) 
    {
        try 
        {
            Method method = clazz.getMethod(methodName, parameterTypes);
            switch (type.toLowerCase())
    		{
			case "boolean":
				if (method.getReturnType() == boolean.class)
					return true;
					
				break;
				
			case "int":
				if (method.getReturnType() == int.class)
					return true;
				
				break;
				
			case "float":
				if (method.getReturnType() == float.class)
					return true;
				
				break;
				
			case "double":
				if (method.getReturnType() == double.class)
					return true;
				
				break;
				
			case "short":
				if (method.getReturnType() == short.class)
					return true;
				
				break;
				
			case "string":
				if (method.getReturnType() == String.class)
					return true;
				
				break;
				
			case "arraylist":
				if (method.getReturnType() == ArrayList.class)
					return true;
				
				break;

			default:
				RebugsAntiCheatSwitcherPlugin.Log(Level.SEVERE, "Type is unknown: hasMethod");
				return method != null;
			}
            return method != null;
        }
        catch (NoSuchMethodException e)
        {
            return false;
        }
    }
    public static boolean hasField (@NotNull Class<?> clazz, @NotNull String fieldName, String type) 
    {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) 
        {
            if (field.getName().equals(fieldName))
            {
            	if (PT.isStringNull(type))
            		return true;
            	else
            	{
            		switch (type.toLowerCase())
            		{
					case "boolean":
						if (field.getType() == boolean.class)
							return true;
							
						break;
						
					case "int":
						if (field.getType() == int.class)
							return true;
						
						break;
						
					case "float":
						if (field.getType() == float.class)
							return true;
						
						break;
						
					case "double":
						if (field.getType() == double.class)
							return true;
						
						break;
						
					case "short":
						if (field.getType() == short.class)
							return true;
						
						break;
						
					case "string":
						if (field.getType() == String.class)
							return true;
						
						break;
						
					case "arraylist":
						if (field.getType() == ArrayList.class)
							return true;
						
						break;

					default:
						RebugsAntiCheatSwitcherPlugin.Log(Level.SEVERE, "Type is unknown: CheckIfFieldExists");
						return false;
					}
            	}
            }
        }
        return false;
    }
    public static Field getField (boolean Throw, @NotNull Class<?> clazz, String ... names) 
    {
        for (String name : names) 
        {
            try
            {
                return ReflectionUtils.setAccessible(clazz.getDeclaredField(name));
            }
            catch (NoSuchFieldException noSuchFieldException) {}
        }
        if (Throw)
        	throw new IllegalArgumentException("Class " + clazz.getName() + " does not contain a field with potential names " + Arrays.toString(names));
        else
        	return null;
    }
}
