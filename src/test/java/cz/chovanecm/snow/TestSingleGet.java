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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.chovanecm.snow;

import org.junit.Test;

import java.io.IOException;

public class TestSingleGet {

    public TestSingleGet() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testSingleGet() throws IOException {
       /* SnowClient client = new SnowClient("https://demo019.service-now.com", "admin", "admin", null, null);
        SnowScript script = client.getRecordBySysId(new ScriptSnowTable("sys_script_include", "script", "name"), "3cdeec09a997b940234722fdcbb24210", SnowScript.class);
        System.out.println(script.getScript());
        Files.write(Paths.get("D:/test.js"), script.getScript().getBytes());*/
    }
}
