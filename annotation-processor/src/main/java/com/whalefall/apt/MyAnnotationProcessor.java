package com.whalefall.apt;

import com.google.auto.service.AutoService;


import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.whalefall.use.apt.BuilderProperty")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class MyAnnotationProcessor extends AbstractProcessor {

    private Messager messager;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "processor is init ....");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "processor is processing");
        annotations.forEach(e -> messager.printMessage(Diagnostic.Kind.NOTE, e.getQualifiedName()));

        messager.printMessage(Diagnostic.Kind.NOTE, "separator");

        Set<? extends Element> rootElements = roundEnv.getRootElements();
        rootElements.forEach(r -> messager.printMessage(Diagnostic.Kind.NOTE, r.getSimpleName()));
        return true;
    }

}
