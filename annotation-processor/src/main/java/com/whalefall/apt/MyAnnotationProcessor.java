package com.whalefall.apt;

import com.google.auto.service.AutoService;
import com.google.j2objc.annotations.Property;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.beans.Introspector;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.whalefall.use.apt.BuilderProperty")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class MyAnnotationProcessor extends AbstractProcessor {

    @SuppressWarnings("unused")
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "processor is init ....");
    }

    @Override
    @SuppressWarnings("all")
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement t : annotations) {
            Map<String, Property> props = new LinkedHashMap<>();
            String beanClassName = null;
            for (Element e : roundEnv.getElementsAnnotatedWith(t)) {
                String mname = e.getSimpleName().toString();
                String[] prefixes = {"get", "set", "is"};
                boolean found = false;
                for (int i = 0; !found && i < prefixes.length; i++)
                    if (mname.startsWith(prefixes[i])) {
                        found = true;
                        int start = prefixes[i].length();
                        String name = Introspector.decapitalize(mname.substring(start));
                        props.put(name, e.getAnnotation(Property.class));
                    }

                if (!found)
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@Property must be applied to getXxx, setXxx, or isXxx method", e);
                else if (null == beanClassName)
                    beanClassName = ((TypeElement) e.getEnclosingElement()).getQualifiedName()
                            .toString();
            }
            /*try {
                if (beanClassName != null) writeBeanInfoFile(beanClassName, props);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
        return true;
    }

    /**
     * Writes the source file for the BeanInfo class.
     *
     * @param beanClassName the name of the bean class
     * @param props         a map of property names and their annotations
     */
    @SuppressWarnings("unused")
    private void writeBeanInfoFile(String beanClassName, Map<String, Property> props)
            throws IOException {
        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(
                beanClassName + "BeanInfo");
        PrintWriter out = new PrintWriter(sourceFile.openWriter());
        int i = beanClassName.lastIndexOf(".");
        if (i > 0) {
            out.print("package ");
            out.print(beanClassName.substring(0, i));
            out.println(";");
        }
        out.print("public class ");
        out.print(beanClassName.substring(i + 1));
        out.println("BeanInfo extends java.beans.SimpleBeanInfo");
        out.println("{");
        out.println("   public java.beans.PropertyDescriptor[] getPropertyDescriptors()");
        out.println("   {");
        out.println("      try");
        out.println("      {");
        for (Map.Entry<String, Property> e : props.entrySet()) {
            out.print("         java.beans.PropertyDescriptor ");
            out.print(e.getKey());
            out.println("Descriptor");
            out.print("             = new java.beans.PropertyDescriptor(\"");
            out.print(e.getKey());
            out.print("\", ");
            out.print(beanClassName);
            out.println(".class);");
            String ed = e.getValue().value();
            if (!ed.equals("")) {
                out.print("          ");
                out.print(e.getKey());
                out.print("Descriptor.setPropertyEditorClass(");
                out.print(ed);
                out.println(".class);");
            }
        }
        out.println("         return new java.beans.PropertyDescriptor[]");
        out.print("         {");
        boolean first = true;
        for (String p : props.keySet()) {
            if (first) first = false;
            else out.print(",");
            out.println();
            out.print("            ");
            out.print(p);
            out.print("Descriptor");
        }
        out.println();
        out.println("         };");
        out.println("      }");
        out.println("      catch (java.beans.IntrospectionException e)");
        out.println("      {");
        out.println("         e.printStackTrace();");
        out.println("         return null;");
        out.println("      }");
        out.println("   }");
        out.println("}");
        out.close();
    }


}
