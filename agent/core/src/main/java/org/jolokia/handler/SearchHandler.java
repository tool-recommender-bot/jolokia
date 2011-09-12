package org.jolokia.handler;

import org.jolokia.request.*;
import org.jolokia.restrictor.Restrictor;
import org.jolokia.util.RequestType;

import javax.management.*;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/*
 *  Copyright 2009-2010 Roland Huss
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


/**
 * Handler responsible for searching for MBean names.
 * @author roland
 * @since Jun 18, 2009
 */
public class SearchHandler extends JsonRequestHandler<JmxSearchRequest> {

    public SearchHandler(Restrictor pRestrictor) {
        super(pRestrictor);
    }

    @Override
    public RequestType getType() {
        return RequestType.SEARCH;
    }

    @Override
    @SuppressWarnings("PMD.ReplaceHashtableWithMap")
    public Object doHandleRequest(Set<MBeanServerConnection> servers, JmxSearchRequest request)
            throws MBeanException, IOException {
        Set<String> ret = new HashSet<String>();

        for (MBeanServerConnection server : servers) {
            Set<ObjectName> names = server.queryNames(request.getObjectName(),null);
            for (ObjectName name : names) {
                    ret.add(name.getCanonicalName());
            }
        }
        return new ArrayList<String>(ret);
    }

    @Override
    public boolean handleAllServersAtOnce(JmxSearchRequest pRequest) {
        return true;
    }

    @Override
    protected Object doHandleRequest(MBeanServerConnection server, JmxSearchRequest request) throws InstanceNotFoundException, AttributeNotFoundException, ReflectionException, MBeanException, IOException {
        throw new UnsupportedOperationException("Internal: Method must not be called when all MBeanServers are handled at once");
    }
}
