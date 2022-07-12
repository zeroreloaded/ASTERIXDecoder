/****************************************************************************

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
    @author T.Y.Shin (https://github.com/zeroreloaded)
    
******************************************************************************/
package net.aviation.yts.asterix;
 
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

public class AppTest 
{
    @BeforeAll
    public static void runOnceBeforeClass() 
    {
    }

    @AfterAll
    public static void runOnceAfterClass() 
    {
    }

    @BeforeEach
    public void runBeforeTestMethod() 
    {
    }

    @AfterEach
    public void runAfterTestMethod() 
    {
    }

    @Disabled
    public void TestParse_CATA10()
    {
        testParse("conf/asterix_cat10.ast");
    }

    @Disabled
    public void TestParse_CATA21()
    {
        testParse("conf/asterix_cat21.ast");
    }

    @Test
    public void TestParse_CATA62()
    {
        testParse("conf/asterix_cat62.ast");
    }

    
    private void testParse(String data_file)
    {
    	byte[] data = null;
    	
    	try
        {
            FileInputStream fis = new FileInputStream(new File(data_file));
            byte[] buf = new byte[10000];
            int read = fis.read(buf);
            
            data = new byte[read];
            
            System.arraycopy(buf, 0, data, 0, read);
            fis.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    	ASTERIXDecoder decoder = new ASTERIXDecoder();
    	
        List<Map<String, String>> result = decoder.decode(data);
        result.forEach(map -> 
            {
                map.forEach((key, val) -> 
                {
                    System.out.printf("%20s: %-20s\n", key, val);
                });
                System.out.println("---------------------------------------------------");
                
            });

        assertNotNull(result);
    }

    private void printMap(Map<String,String> map)
    {
        if(map == null) return;

        Iterator<String> it = map.keySet().iterator();
        while(it.hasNext())
        {
            String key = it.next();
            String val = map.get(key);
            System.out.printf("%20s: %-20s\n", key, val);
        }

        System.out.println("--------------------------------------------");
    }
}
 
