/*
 * Snow Script Synchronizer is a tool helping developers to write scripts for ServiceNow
 *     Copyright (C) 2015-2017  Martin Chovanec <chovamar@fit.cvut.cz>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cz.chovanecm.snow.api;

import com.github.jsonj.JsonObject;
import com.github.jsonj.tools.JsonParser;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.HttpURLConnection.HTTP_OK;

public class SnowApiGetResponse implements AutoCloseable {

    private final HttpResponse<String> response;

    public SnowApiGetResponse(HttpResponse<String> response) throws IOException {
        if (response.statusCode() != HTTP_OK) {
            throw new IOException("Unexpected response from ServiceNow: " + response.statusCode());
        }
        this.response = response;
    }

    public int getRowCount() {
        return Integer.parseInt(response.headers().firstValue("X-Total-Count").get());
    }

    /**
     * Returns URL pointing to next bunch of records. If there are no more
     * records, returns null;
     *
     * @return
     */
    public String getNextRecordsUrl() {
        String links;
        try {
            links = response.headers().firstValue("Link").get();
        } catch (NullPointerException ex) {
            return null;
        }
        //https://demo018.service-now.com/api/now/v1/table/sys_script?sysparm_limit=1&sysparm_offset=0>;rel="first",<https://demo018.service-now.com/api/now/v1/table/sys_script?sysparm_limit=1&sysparm_offset=-1>;rel="prev",<https://demo018.service-now.com/api/now/v1/table/sys_script?sysparm_limit=1&sysparm_offset=1>;rel="next",<https://demo018.service-now.com/api/now/v1/table/sys_script?sysparm_limit=1&sysparm_offset=1353>;rel="last"
        Pattern pattern = Pattern.compile("<([^>]+)>;rel=\"next\"");
        Matcher matcher = pattern.matcher(links);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public JsonObject getBody() throws IOException {
        JsonObject object;
        var content = response.body().replaceAll("\\\\\\\\", "\\\\\\\\\\\\\\\\");
        object = new JsonParser().parse(content).asObject();
        return object;
    }

    @Override
    public void close() throws Exception {

    }
}
