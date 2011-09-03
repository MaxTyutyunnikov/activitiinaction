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
package org.bpmnwithactiviti.chapter8.workflow.ui.popup;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author Joram Barrez
 */
public class UploadPopupWindow extends Window {

	protected static final long serialVersionUID = 1L;
	
	protected String fileName;
  protected ByteArrayOutputStream byteArrayOutputStream;
  protected String mimeType;
  protected UploadPopupWindow uploadWindow;

  protected static final String TITLE = "Upload";

  public UploadPopupWindow() {
    setModal(true);
    setWidth("400");
    setHeight("200");
    center();
    setCaption(TITLE);

    VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);
    Upload upload = new Upload(null, new Receiver() {
    	
      private static final long serialVersionUID = 1L;
      
      public OutputStream receiveUpload(String filename, String mType) {
        fileName = filename;
        mimeType = mType;
        
        byteArrayOutputStream = new ByteArrayOutputStream();
        return byteArrayOutputStream;
      }
    });
    
    upload.setImmediate(true);
    
    upload.addListener(new FinishedListener() {
			
      private static final long serialVersionUID = 1L;

			@Override
			public void uploadFinished(FinishedEvent event) {
				uploadWindow.close();
			}
		});

    layout.addComponent(upload);
    addComponent(layout);
    uploadWindow = this;
  }

	public String getFileName() {
  	return fileName;
  }

	public void setFileName(String fileName) {
  	this.fileName = fileName;
  }

	public ByteArrayOutputStream getByteArrayOutputStream() {
  	return byteArrayOutputStream;
  }

	public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
  	this.byteArrayOutputStream = byteArrayOutputStream;
  }

	public String getMimeType() {
  	return mimeType;
  }

	public void setMimeType(String mimeType) {
  	this.mimeType = mimeType;
  }
}
