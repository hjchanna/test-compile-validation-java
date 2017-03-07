package com.mac.compile_validation;

import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

/**
 *
 * @author hjchanna
 */
@SupportedAnnotationTypes("com.mac.compile_validation.CompileValidation")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CompileValidationProcessor extends AbstractProcessor {

    /**
     * Processes a set of annotation types on type elements originating from the
     * prior round and returns whether or not these annotation types are claimed
     * by this processor. If {@code
     * true} is returned, the annotation types are claimed and subsequent
     * processors will not be asked to process them; if {@code false} is
     * returned, the annotation types are unclaimed and subsequent processors
     * may be asked to process them. A processor may always return the same
     * boolean value or may vary the result based on chosen criteria.
     *
     * The input set will be empty if the processor supports {@code
     * "*"} and the root elements have no annotations. A {@code
     * Processor} must gracefully handle an empty set of annotations.
     *
     * @param annotations the annotation types requested to be processed
     * @param roundEnv environment for information about the current and prior
     * round
     * @return whether or not the set of annotation types are claimed by this
     * processor
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //Iterate through compiling files which annotated with @CompileValidation
        for (Element elem : roundEnv.getElementsAnnotatedWith(CompileValidation.class)) {
            //find type element for element
            TypeElement typeElement = findEnclosingTypeElement(elem);

            //required parameter types
            TypeElement stringType = processingEnv.getElementUtils().getTypeElement("java.lang.String");
            TypeElement integerType = processingEnv.getElementUtils().getTypeElement("java.lang.Integer");

            //find construtors according to your scenario
            ExecutableElement conA = findConstructor(typeElement, stringType.asType(), integerType.asType());
            ExecutableElement conB = findConstructor(typeElement, stringType.asType(), stringType.asType());

            //check availability of constructors, if not available it should show a warning message in compile time
            if (conA == null || conB == null) {
                String message = "Type " + typeElement + " has malformed constructors.";
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, message);
            }

        }
        return true; // no further processing of this annotation type
    }

    /**
     * Returns a constructor which have two parameters and parameter types equal
     * to paramA and paramB. Return null if required constructor is not
     * available.
     *
     * @param typeElement like the class which may constructors encapsulated
     * @param paramA first parameter of required constructor
     * @param paramB second parameter of required constructor
     * @return constructor which have required parameters
     */
    private static ExecutableElement findConstructor(TypeElement typeElement, TypeMirror paramA, TypeMirror paramB) {
        List<ExecutableElement> executableElements = ElementFilter.constructorsIn(typeElement.getEnclosedElements());

        for (ExecutableElement executableElement : executableElements) {
            List<VariableElement> variableElements = (List<VariableElement>) executableElement.getParameters();

            //match constructor params and length
            if (variableElements.size() == 2
                    && variableElements.get(0).asType().equals(paramA)
                    && variableElements.get(1).asType().equals(paramB)) {
                return executableElement;
            }
        }

        return null;
    }

    /**
     * Returns the TypeElement of element e.
     *
     * @param e Element which contain TypeElement
     * @return Type element
     */
    public static TypeElement findEnclosingTypeElement(Element e) {
        while (e != null && !(e instanceof TypeElement)) {
            e = e.getEnclosingElement();
        }

        return TypeElement.class.cast(e);
    }
}
