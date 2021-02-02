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
package de.ckthomas.smarthome.camunda.connectors.homeassistant.common;

import org.camunda.connect.impl.AbstractRequestInvocation;
import org.camunda.connect.spi.ConnectorRequestInterceptor;
import java.util.Arrays;

import java.util.List;

public class CommonInvocation extends AbstractRequestInvocation<String> {

  //protected final MailService mailService;

  public CommonInvocation(String dummyFolder, CommonRequest request,
                          List<ConnectorRequestInterceptor> requestInterceptors) {
    super("DUMMY STRING - SIEHE TEMPLATE", request, requestInterceptors);


  }

  @Override
  public Object invokeTarget() throws Exception {
    // poll only messages which are not deleted
    //Message[] messages = target.search(new FlagTerm(new Flags(Flag.DELETED), false));

    return null; //Arrays.asList(messages);
  }

}
