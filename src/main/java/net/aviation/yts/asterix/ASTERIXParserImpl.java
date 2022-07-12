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

import javax.xml.bind.*;
import java.io.*;
import java.text.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ASTERIXParserImpl implements IASTERIXParser
{
	private Logger log = LoggerFactory.getLogger(ASTERIXParserImpl.class);
    private int cat_no = -1;
    private String file = null;
    private ASTERIXCategory category;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    private Calendar cal = Calendar.getInstance();
    private ASTERIXDecoderUtils asterixUtils = new ASTERIXDecoderUtils();
    
    public ASTERIXParserImpl(String catno, String file)
    {
        try
        {
            this.cat_no = Integer.parseInt(catno);
            this.file = file;
            category = asterixUtils.getASTERIXCategoryBySpec(this.file);
        }
        catch(Exception e)
        {}
    }
    
    public ASTERIXCategory getCategory() { return this.category; }
    
    private void saveAsterixToFile(String filename, byte[] data)
    {
    	FileOutputStream fos = null;

        try
        {
            fos = new FileOutputStream(filename);
            fos.write(data);
        }
        catch(Exception e)
        {}
        finally
        {
            if(fos != null)
            {
                try{ fos.close(); } catch(Exception ex){}
            }
        }
        
    }
    
    public List<Map<String, String>> parse(byte[] data)
    {
    	if(data == null || data.length < 5) return null;
    	
        return parseForNormal(data);
        
    }
    
    private void saveFileForAnalyze(byte[] data, String filename)
    {
    	if(data == null) return;
    	
    	FileOutputStream fos = null;
    	try
    	{
    		fos = new FileOutputStream(filename);
    		fos.write(data);
    	}
    	catch(Exception e)
    	{}
    	finally
    	{
    		if(fos != null)
    		{
    			try { fos.close();}
    			catch(Exception ex) {}
    		}
    	}
    }
    
    private List<Map<String, String>> parseForNormal(byte[] data)
    {
    	
        List<Map<String, String>> result_list = new ArrayList<Map<String, String>>();
        
        int total_len = data.length;
        int index = 0;
        byte[] b_len = new byte[2];
        byte[] real_data = null;
        
        while(total_len > index)
        {
            int cat = data[index] & 0xff;
            
            b_len[0] = data[index+1];
            b_len[1] = data[index+2];
            
            int len = asterixUtils.unsignedToInt(b_len);
            
            if(len <= 0) return result_list;
            real_data = new byte[len];
            
            try
            {
            	if((index+len) > total_len) len = total_len-index;
            	
            	System.arraycopy(data, index, real_data, 0, len);
            
            	List<Map<String,String>> list = parsePacket(real_data);
            	
            	if(list != null) result_list.addAll(list);
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        	System.out.println("exception : index , len, total_len =  " + index + ", " + len + ", " + total_len);
	        	return result_list;
	        }
            
            index += len;
        }
        
        return result_list;
        
    }
    
    private List<Map<String, String>> parsePacket(byte[] data) throws Exception
    {
        if(this.cat_no == -1 || file == null || data == null || data.length == 0) return null;
        
        int total_len = data.length;
        int body_len = data.length - 3;
        byte[] body_data = new byte[body_len];
        
        System.arraycopy(data, 3, body_data, 0, body_len);
        
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Map<Integer, ASTERIXDataItem> uap = null;
        Map<String, String> result_map = null;
        
        int index = 0;
        int data_index = 0;
        int idx;
        
        while(index < body_len-1)
        {
            int to_read = body_len - index;
            uap = new LinkedHashMap<Integer, ASTERIXDataItem>();
            
            for(idx=0; idx<to_read;idx++)
            {
                int fspec = body_data[index+idx] & 0xff;
                if(!asterixUtils.makeUAP(uap, idx, fspec, category)) break;
            }
            
            int offset = idx+1;
            int new_len = body_len - index - offset;
            if(new_len < 0) break;
            
            byte[] real_data = new byte[new_len];
            
            System.arraycopy(body_data, body_len-new_len, real_data, 0, new_len);
            
            Iterator<Integer> it = uap.keySet().iterator();
            result_map = new HashMap<String, String>();
            
            data_index = 0;
            
            while(it.hasNext())
            {
                Integer frn = it.next();
                ASTERIXDataItem di = uap.get(frn);
                if(di == null) continue;

                data_index = asterixUtils.makeResult(result_map, di, real_data, data_index, category);
                if(data_index < 0) break;
            }
            
            if(result_map != null && result_map.size() > 0)
            {
                result.add(result_map);
            }
            
            if(data_index < 0) break;
            index = index + offset + data_index;
        }
        
        return result;
    }
}
