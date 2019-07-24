package cz.kul.snippets.java.example25_TODO;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

public class JarClassLoader extends ClassLoader implements Closeable {

    private static final String FILE_URL_PROTOCOL = "file";
    private static final String JAR_ENTRY_URL_PROTOCOL = "jar";

    private static final String CLASS_FILE_EXTENSION = ".class";

    /**
     * {@link Logger} instance.
     */
    private static final Logger _LOGGER = LoggerFactory.getLogger(JarClassLoader.class);

    /**
     * Map to store JAR entries data read from the JAR input stream.
     */
    private final Map<String, byte[]> _jarEntriesData = new HashMap<>();

    /**
     * {@link Vector < InputStream >} to store streams opened by this class loader.
     */
    private final Vector<InputStream> _resourceStreams = new Vector<>();

    private static int idCounter = 0;

    private static final Map<JarClassLoader, Void> _refs = Collections.synchronizedMap(new WeakHashMap<>());

    private int id;

    private long creationTimestamp;

    public int getId() {
        return id;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    private static void addRef(JarClassLoader jarClassLoader) {
        jarClassLoader.id = idCounter++;
        jarClassLoader.creationTimestamp = System.currentTimeMillis();
        _refs.put(jarClassLoader, null);
    }

    public static List<JarClassLoader> getOldInstances(long minLivetime) {
        long now = System.currentTimeMillis();
        return _refs.keySet().stream()
                .filter(x -> {
                    long livetime = now - x.creationTimestamp;
                    return livetime >= minLivetime;
                })
                .sorted(Comparator.comparingInt(x -> x.id))
                .collect(Collectors.toList());
    }

    /**
     * Public constructor reads and stores the data for each entry in the given JAR input stream.
     *
     * @throws IOException
     */
    public JarClassLoader() throws IOException {
        this(JarClassLoader.class.getClassLoader());
    }


    /**
     * Public constructor reads and stores the data for each entry in the given JAR input stream and
     * sets the parent ClassLoader.
     *
     * @param parentClassLoader The parent class loader.
     * @throws IOException
     */
    public JarClassLoader(final ClassLoader parentClassLoader)
            throws IOException {
        super(parentClassLoader);

        addRef(this);
    }


    /**
     *
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() throws IOException {
        _resourceStreams.stream().forEach(stream -> {
            try {
                stream.close();
            } catch(final IOException e) {
                e.printStackTrace();
            }
        });
        _resourceStreams.clear();
    }


    /**
     *
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        try {
            _LOGGER.debug("JarClassLoader.finalize");
        } finally {
            super.finalize();
        }
    }


    /**
     *
     * @see java.lang.ClassLoader#findClass(java.lang.String)
     */
    @Override
    protected Class<?> findClass(final String sClassName) throws ClassNotFoundException {
        Class<?> resultClass = null;

        byte[] classData;
        final String sJarEntryPath = "customClasses/" + sClassName.replace('.', '/') + ".class";
        File classFile = new File(sJarEntryPath);
        if (classFile.exists()) {
            try {
                classData = Files.toByteArray(classFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            resultClass = defineClass(sClassName, classData, 0, classData.length, null);
        }
        return null != resultClass ? resultClass : super.findClass(sClassName);
    }

    /**
     *
     * @see java.lang.ClassLoader#findResource(java.lang.String)
     */
    @Override
    protected URL findResource(final String sResourceName) {
        try {
            return _jarEntriesData.containsKey(sResourceName)
                    ? new URL(FILE_URL_PROTOCOL, "", sResourceName)
                    : null;
        } catch(final MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     *
     * @see java.lang.ClassLoader#findResources(java.lang.String)
     */
    @Override
    protected Enumeration<URL> findResources(final String sResourceName) throws IOException {
        final List<URL> resources = new ArrayList<>();

        final URL resource = findResource(sResourceName);
        if(null != resource) {
            resources.add(resource);
        }

        final Enumeration<URL> parentResources = getParent().getResources(sResourceName);
        resources.addAll(Collections.list(parentResources));

        return Collections.enumeration(resources);
    }


    /**
     *
     * @see java.lang.ClassLoader#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(final String sResourceName) {
        InputStream inputStream = super.getResourceAsStream(sResourceName);
        if(null == inputStream) {
            final byte[] resourceData = _jarEntriesData.get(sResourceName);
            if(null != resourceData) {
                inputStream = new ByteArrayInputStream(resourceData);
                _resourceStreams.addElement(inputStream);
            }
        }

        return inputStream;
    }


    /**
     * Invokes the application in this JAR input stream given the fully qualified name of the main
     * class and an array of arguments.
     * The class must define a static method {@code main} which takes an array of {@link String}
     * arguments
     * and is of return type {@code void}.
     *
     * @param sClassName The fully qualified name of the main class.
     * @param args The arguments for the application.
     * @throws ClassNotFoundException If the specified class could not be found.
     * @throws NoSuchMethodException If the specified class does not contain a {@code main} method.
     * @throws InvocationTargetException If the application raised an exception.
     */
    public void invokeClass(final String sClassName,
                            final String ... args) throws ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException {
        final Class<?> clazz = loadClass(sClassName);

        final Method method = clazz.getDeclaredMethod("main", new Class[] { String[].class });
        method.setAccessible(true);
        final int nModifiers = method.getModifiers();
        if(method.getReturnType() != void.class || !Modifier.isStatic(nModifiers) || !Modifier.isPublic(
                nModifiers)) {
            throw new NoSuchMethodException("public static void main(final String[] args)");
        }
        try {
            method.invoke(null, new Object[] { args });
        } catch(final IllegalAccessException e) {
            // This should not happen, as we have disabled access checks
            e.printStackTrace();
        }
    }


    /**
     * Invokes the method in this JAR input stream given the fully qualified name of the method.
     *
     * @param sMethodName The fully qualified name of the method.
     * @param args The arguments for the application.
     * @return If the method completes normally, the value it returns is returned; if the value has a
     *         primitive type, it is first appropriately wrapped in an object. However, if the value
     *         has the type of an array of a primitive type, the elements of the array are not wrapped
     *         in objects; in other words, an array of primitive type is returned. If the underlying
     *         method return type is void, the invocation returns {@code null}.
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public Object invokeMethod(final String sMethodName,
                               final Object ... args) throws ClassNotFoundException,
            NoSuchMethodException,
            SecurityException,
            InstantiationException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        final int nIndex = sMethodName.lastIndexOf(".");
        final Class<?> clazz = Class.forName(sMethodName.substring(0, nIndex), true, this);

        final Method method = clazz.getMethod(
                sMethodName.substring(nIndex + 1),
                Arrays.stream(args).map(arg -> arg.getClass()).toArray(nLength -> new Class<?>[nLength]));

        final Object instance = clazz.newInstance();

        return method.invoke(instance, args);
    }


    public <T> T invokeMethod(final String sMethodName,
                              final Class<T> resultClass,
                              final Object ... args) throws ClassNotFoundException,
            NoSuchMethodException,
            SecurityException,
            InstantiationException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        final Object result = invokeMethod(sMethodName, args);

        return resultClass.cast(result);
    }


    /**
     * Invokes the {@link Runnable} class in this JAR input stream given the fully qualified name of
     * the class implementing {@link Runnable} interface.
     *
     * @param sClassName The fully qualified name of the class implementing {@link Runnable}
     *          interface.
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public void invokeRunnable(final String sClassName) throws ClassNotFoundException,
            NoSuchMethodException,
            SecurityException,
            InstantiationException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        final Class<?> clazz = Class.forName(sClassName, true, this);
        final Class<? extends Runnable> runnableClass = clazz.asSubclass(Runnable.class);

        final Constructor<? extends Runnable> constructor = runnableClass.getConstructor();
        final Runnable instance = constructor.newInstance();

        instance.run();
    }

}
