/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.unittest;

import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;

/**
 * I had problems building this test, so I kept it simple. So even if tests succeed, they actually aren't. Look at the logs, please.
 * <br><br>
 * So my question is: Is there a way to use multi instance together with a list of spin objects?
 * <br><br>
 * ArrayList&lt;SpinXmlElement&gt; doesn't work.
 * <br>
 * SpinList&lt;SpinXmlElement&gt; doesn't work.
 * <br>
 * ObjectValue doesn't work.
 */
@RunWith(Parameterized.class)
public class SimpleTestCase {

    /**
     * Determines how the SpinList will be transferred to the next task.
     * @return The transferType.
     */
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {"objectValue"},
            {"spinList"},
            {"arrayList"}
        });
    }

    /**
     * Field for the test case.
     */
    private final String transferType;

    @Rule
    public ProcessEngineRule rule = new ProcessEngineRule();

    /**
     * Constructor with the basic data provided.
     * @param transferType Determines how the SpinList will be transferred to the next task.
     */
    public SimpleTestCase(final String transferType) {
        this.transferType = transferType;
    }



    /**
     * By checking the logs, one can see, that either a {@link java.util.NoSuchElementException} occurs (for the ObjectValue attempt)
     * or a {@link java.io.NotSerializableException} occurs (for the SpinList and ArrayList attempts).
     */
    @Test
    @Deployment(resources = {"spinListTest.bpmn"})
    public void spinListTest_throwsException() {

        // Determines the way of transferring the List of SpinXmlElements.
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("transferType", transferType);

        // Starting the process.
        runtimeService().startProcessInstanceByKey("spinListTest", params);
    }

}
