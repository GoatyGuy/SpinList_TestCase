package org.camunda.bpm.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.plugin.variable.value.XmlValue;
import org.camunda.spin.xml.SpinXmlElement;

/**
 * Does something cool.
 */
public class DoSomethingCool implements JavaDelegate {

    public void execute(final DelegateExecution execution) throws Exception {

        // Fetch the independent child element from execution.
        final XmlValue spinValue = execution.getVariableTyped("singleElement", true);
        final SpinXmlElement singleElement = spinValue.getValue();

        // Do something cool.
        System.out.println(singleElement.toString());
    }
}
