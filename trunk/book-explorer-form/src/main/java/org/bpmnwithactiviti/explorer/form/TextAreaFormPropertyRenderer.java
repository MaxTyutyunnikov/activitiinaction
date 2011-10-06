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

package org.bpmnwithactiviti.explorer.form;

import org.activiti.engine.form.FormProperty;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ui.form.AbstractFormPropertyRenderer;

import com.vaadin.ui.Field;
import com.vaadin.ui.TextArea;

public class TextAreaFormPropertyRenderer extends AbstractFormPropertyRenderer {

  public TextAreaFormPropertyRenderer() {
    super(TextAreaFormType.class);
  }

  @Override
  public Field getPropertyField(FormProperty formProperty) {
    TextArea textArea = new TextArea(getPropertyLabel(formProperty));
    textArea.setRequired(formProperty.isRequired());
    textArea.setEnabled(formProperty.isWritable());
    textArea.setRows(10);
    textArea.setColumns(50);
    textArea.setRequiredError(getMessage(Messages.FORM_FIELD_REQUIRED, getPropertyLabel(formProperty)));

    if (formProperty.getValue() != null) {
    	textArea.setValue(formProperty.getValue());
    }

    return textArea;
  }

}
