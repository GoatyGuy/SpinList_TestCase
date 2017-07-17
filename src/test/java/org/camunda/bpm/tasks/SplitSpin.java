package org.camunda.bpm.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.spin.Spin;
import org.camunda.spin.SpinList;
import org.camunda.spin.impl.SpinListImpl;
import org.camunda.spin.xml.SpinXmlElement;

import java.util.ArrayList;

/**
 * Splits the given "rootElement" into its child nodes.
 */
public class SplitSpin implements JavaDelegate {

    public void execute(final DelegateExecution execution) throws Exception {

        // Create element for testing.
        final SpinXmlElement rootElement = Spin.XML("<root><child1/><child2/></root>");
        final String transferType = (String) execution.getVariable("transferType");

        // Get the children of the element.
        final SpinList<SpinXmlElement> childElements = rootElement.childElements();

        // Every child element can still fetch information from its ancestors or siblings.
        // To prevent this, we need to "reconvert" the Spin-elements.
        final SpinList<SpinXmlElement> disconnectedChildElements = new SpinListImpl<SpinXmlElement>();
        for (final SpinXmlElement child : childElements) {
            disconnectedChildElements.add(Spin.XML(child.toString()));
        }

        // Now we have a list of independent SpinXmlElements. But it cannot be serialized directly.
        // Every run will use a different way of transferring the SpinList. Every run fails to do so.
        if (transferType.equals("spinList")) {
            // First we use the SpinList.
            // What will happen: The engine will not be able to serialize the list. A NotSerializableException will occur.
            execution.setVariable("spinList", disconnectedChildElements);
        } else if (transferType.equals("arrayList")) {
            // Here we use an ArrayList instead, because the first try didn't work.
            // What will happen: The engine will not be able to serialize the list. A NotSerializableException will occur.
            final ArrayList<SpinXmlElement> elements = new ArrayList<SpinXmlElement>(disconnectedChildElements);
            execution.setVariable("spinList", elements);
        } else if (transferType.equals("objectValue")) {
            // So we use an object value.
            // What will happen: After the first asynchronous continuation "border" the list behind the object value will be cleared
            // and a NoSuchElement exception will occur.
            final ObjectValue objectValue = Variables.objectValue(disconnectedChildElements).serializationDataFormat("application/xml").create();
            execution.setVariable("spinList", objectValue);
        }

    }
}
